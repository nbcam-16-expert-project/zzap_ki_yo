package com.nbacm.zzap_ki_yo.domain.store.admin.service;


import com.nbacm.zzap_ki_yo.domain.store.admin.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.admin.dto.response.CreateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStoreService {

    private final StoreRepository storeRepository;

    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto createStoreRequestDto) {


        Store store = Store.builder()
                .storeAddress(createStoreRequestDto.getStoreAddress())
                .storeNumber(createStoreRequestDto.getStoreNumber())
                .storeName(createStoreRequestDto.getStoreName())
                .favoriteCount(0)
                .build();


        store = storeRepository.save(store);
    }



    private boolean storeCheck(CreateStoreRequestDto createStoreRequestDto) {
        if(createStoreRequestDto.getStoreAddress() == null || createStoreRequestDto.getStoreNumber() == null ||
        createStoreRequestDto.getStoreName() == null){
            throw new
        }

    }

}
