package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import lombok.Data;

@Data
public class SelectStoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private Integer favoriteCount;


    private SelectStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber, Integer favoriteCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.favoriteCount = favoriteCount;
    }


    public static SelectStoreResponseDto selectStore(Store store) {
        return new SelectStoreResponseDto(store.getStoreId(), store.getStoreName(), store.getStoreAddress(), store.getStoreNumber(), store.getFavoriteCount());
    }
}
