package com.nbacm.zzap_ki_yo.domain.search.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StoreNameDto {

    private String storeName;

    @Builder
    private StoreNameDto(String storeName) {
        this.storeName = storeName;
    }

    public static StoreNameDto of(String storeName) {
        return StoreNameDto.builder()
                .storeName(storeName)
                .build();
    }
}
