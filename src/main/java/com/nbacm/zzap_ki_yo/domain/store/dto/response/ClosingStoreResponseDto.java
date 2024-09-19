package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;

public class ClosingStoreResponseDto {

    private String storeName;
    private StoreType storeType;


    private ClosingStoreResponseDto(String storeName, StoreType storeType) {
        this.storeName = storeName;
        this.storeType = storeType;
    }

    public static ClosingStoreResponseDto closingStore(String storeName, StoreType storeType) {
        return new ClosingStoreResponseDto(storeName, storeType);
    }
}

