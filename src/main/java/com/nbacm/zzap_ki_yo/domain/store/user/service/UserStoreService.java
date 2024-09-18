package com.nbacm.zzap_ki_yo.domain.store.user.service;


import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStoreService {

    private final StoreRepository storeRepository;


    public SelectStoreResponseDto selectStore(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new NotFoundException("가게를 찾지 못했습니다")
        );

        return SelectStoreResponseDto.selectStore(store);
    }

    public List<SelectStoreResponseDto> selectAllStore() {
        List<Store> storeList = storeRepository.findAll();

        if(storeList.isEmpty()){
            throw new StoreNotFoundException("가게를 찾지 못했습니다.");
        }

        List<SelectStoreResponseDto> responseDto = new ArrayList<>();

        for (Store store : storeList) {
            SelectStoreResponseDto selectStoreResponseDto = SelectStoreResponseDto.selectStore(store);
            responseDto.add(selectStoreResponseDto);
        }

        return responseDto;
    }
}
