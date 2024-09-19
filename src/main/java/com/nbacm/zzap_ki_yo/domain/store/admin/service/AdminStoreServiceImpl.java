package com.nbacm.zzap_ki_yo.domain.store.admin.service;


import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreForbiddenException;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStoreServiceImpl implements AdminStoreService {

    private final StoreRepository storeRepository;

    @Transactional
    @Override
    public CreateStoreResponseDto createStore(CreateStoreRequestDto createStoreRequestDto) {

        if(createStoreRequestDto.getStoreAddress() == null
                || createStoreRequestDto.getStoreNumber() == null
                || createStoreRequestDto.getStoreName() == null){
            throw new BadRequestException("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.");
        }

        // user 테이블에서 가게 등록할 유저 찾아와야 함

        Store store = Store.builder()
                .storeAddress(createStoreRequestDto.getStoreAddress())
                .storeNumber(createStoreRequestDto.getStoreNumber())
                .storeName(createStoreRequestDto.getStoreName())
                .favoriteCount(0)
                .storeType(StoreType.OPENING)
                .openingTime(createStoreRequestDto.getOpeningTime())
                .closingTime(createStoreRequestDto.getClosingTime())
                .build();

        List<Store> stores = storeRepository.findAllByUserAndStoreType(store.getUser(), StoreType.OPENING);

        if(stores.size() > 3){
            throw new StoreForbiddenException("가게는 3개까지 운영 가능합니다.");
        }

        store = storeRepository.save(store);

        return CreateStoreResponseDto.createStore(store.getStoreId(), store.getStoreName());
    }


    @Transactional
    @Override
    public UpdateStoreResponseDto updateStore(Long storeId, UpdateStoreNameRequest request) {

        if(request.getStoreName() == null
        || request.getStoreAddress() == null
        || request.getStoreNumber() == null){
            throw new BadRequestException("수정할 가게 이름, 주소, 번호가 없으면 안 됩니다.");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다.")
        );

        store.updateStoreName(request.getStoreName());

        return UpdateStoreResponseDto.updateStoreName(store);
    }


    @Transactional
    @Override
    public DeleteStoreResponseDto deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다.")
                );

        storeRepository.delete(store);

        return DeleteStoreResponseDto.delete("가게 제거 성공", HttpStatus.NO_CONTENT.value());
        
    }


    @Override
    public SelectStoreResponseDto selectStore(Long storeId) {
        Store store = storeRepository.findByStoreId(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다")
        );

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
        List<Store> storeList = storeRepository.findAllByStoreNameContainingAndStoreType(requestDto.getStoreName(), StoreType.OPENING).stream().toList();

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

    @Transactional
    @Override
    public ClosingStoreResponseDto closingStore(Long storeId) {
        // 폐업할 가게를 운영중인 유저 가져와야 함
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다.")
        );

        store.closingStore(StoreType.CLOSING);
        return ClosingStoreResponseDto.closingStore(store.getStoreName(), store.getStoreType());
    }
}
