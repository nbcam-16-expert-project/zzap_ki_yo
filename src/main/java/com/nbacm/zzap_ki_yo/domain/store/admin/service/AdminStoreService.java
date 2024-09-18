package com.nbacm.zzap_ki_yo.domain.store.admin.service;


import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.CreateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.DeleteStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.UpdateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
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
public class AdminStoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto createStoreRequestDto) {

        if(createStoreRequestDto.getStoreAddress() == null
                || createStoreRequestDto.getStoreNumber() == null
                || createStoreRequestDto.getStoreName() == null){
            throw new BadRequestException("가게 주소와 번호, 이름이 없으면 안 됩니다.");
        }

        Store store = Store.builder()
                .storeAddress(createStoreRequestDto.getStoreAddress())
                .storeNumber(createStoreRequestDto.getStoreNumber())
                .storeName(createStoreRequestDto.getStoreName())
                .favoriteCount(0)
                .build();


        store = storeRepository.save(store);

        return CreateStoreResponseDto.createStore(store.getStoreId(), store.getStoreName());
    }


    @Transactional
    public UpdateStoreResponseDto updateStore(Long storeId, UpdateStoreNameRequest request) {

        if(request.getStoreName() == null
        || request.getStoreAddress() == null
        || request.getStoreNumber() == null){
            throw new BadRequestException("수정할 가게 이름이 없으면 안 됩니다.");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다.")
        );

        store.updateStoreName(request.getStoreName());

        return UpdateStoreResponseDto.updateStoreName(store);
    }


    @Transactional
    public DeleteStoreResponseDto deleteStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾지 못했습니다.")
                );

        storeRepository.delete(store);

        return DeleteStoreResponseDto.delete("가게 제거 성공", HttpStatus.NO_CONTENT.value());
    }


    public SelectStoreResponseDto selectStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new NotFoundException("가게를 찾지 못했습니다")
        );

        return SelectStoreResponseDto.selectStore(store);
    }

    public List<SelectStoreResponseDto> selectAllStore() {
        List<Store> stores = storeRepository.findAll();

        if(stores.isEmpty()){
            throw new StoreNotFoundException("가게를 찾지 못했습니다.");
        }
        List<SelectStoreResponseDto> responseDto = new ArrayList<>();
        for (Store store : stores) {
            SelectStoreResponseDto selectStoreResponseDto = SelectStoreResponseDto.selectStore(store);
            responseDto.add(selectStoreResponseDto);
        }

        return responseDto;
    }
}
