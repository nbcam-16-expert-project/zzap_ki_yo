package com.nbacm.zzap_ki_yo.domain.store.user.service;


import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.MenuNamePrice;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.User;
import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreServiceImpl implements UserStoreService{

    private final StoreRepository storeRepository;


    @Override
    public SelectStoreResponseDto selectStore(Long storeId) {

//        Store store = storeRepository.findByStoreId(storeId);
//
//        if(store == null){
//            throw new StoreNotFoundException("가게를 찾을 수 없습니다.");
//        }

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾을 수 없습니다.")
        );

        if(store.getStoreType().equals(StoreType.CLOSING)){
            throw new UnauthorizedException("폐업한 가게는 조회할 수 없습니다.");
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
    public List<SelectAllStoreResponseDto> selectAllStore(StoreNameRequestDto requestDto) {
        List<Store> storeList = storeRepository.
                findAllByStoreNameContainingAndStoreType(requestDto.getStoreName(), StoreType.OPENING).stream().toList();

        if(storeList.isEmpty()){
            throw new StoreNotFoundException("가게를 찾지 못했습니다.");
        }

        List<SelectAllStoreResponseDto> responseDto = new ArrayList<>();

        for (Store store : storeList) {
            SelectAllStoreResponseDto selectStoreResponseDto = SelectAllStoreResponseDto.selectAllStore(store);
            responseDto.add(selectStoreResponseDto);
        }

        return responseDto;
    }




}
