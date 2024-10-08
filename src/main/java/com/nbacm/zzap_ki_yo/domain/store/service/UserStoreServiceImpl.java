package com.nbacm.zzap_ki_yo.domain.store.service;


import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.MenuNamePrice;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreForbiddenException;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreServiceImpl implements UserStoreService{

    private final StoreRepository storeRepository;


    @Override
    public SelectStoreResponseDto selectStore(String storeName) {

        Store store = storeRepository.findByStoreName(storeName).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾을 수 없습니다.")
        );

        if(store.getStoreType().equals(StoreType.CLOSING)){
            throw new StoreForbiddenException("폐업한 가게는 조회할 수 없습니다.");
        }

        List<Menu> menus = store.getMenus();

        List<MenuNamePrice> menuNamePrices = new ArrayList<>();
        for (Menu menu : menus) {
            MenuNamePrice menuNamePrice = MenuNamePrice.builder()
                    .menuName(menu.getMenuName())
                    .price(menu.getPrice())
                    .build();

            menuNamePrices.add(menuNamePrice);
        }

        return SelectStoreResponseDto.selectStore(store, menuNamePrices);
    }

    @Override
    public List<SelectAllStoreResponseDto> selectAllStore() {
        List<Store> stores = storeRepository.findAllByStoreTypeOrderByAdTypeAndId(StoreType.OPENING);

        return stores.stream()
                .map(SelectAllStoreResponseDto::selectAllStore)
                .toList();
    }
}