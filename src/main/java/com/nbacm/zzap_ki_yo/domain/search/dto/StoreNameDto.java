package com.nbacm.zzap_ki_yo.domain.search.dto;

import lombok.Data;

@Data
public class StoreNameDto {

    private String storeName;

    private StoreNameDto(String storeName) {
        this.storeName = storeName;
    }

    public static StoreNameDto of(String storeName) {
        return new StoreNameDto(storeName);
    }
}
