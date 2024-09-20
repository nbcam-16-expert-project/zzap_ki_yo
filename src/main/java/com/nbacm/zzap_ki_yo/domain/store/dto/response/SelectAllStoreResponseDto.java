package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import lombok.Builder;
import lombok.Data;

@Data
public class SelectAllStoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private StoreType storeType;
    private Integer favoriteCount;


    @Builder
    public SelectAllStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber,
                                     StoreType storeType, Integer favoriteCount){
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.storeType = storeType;
        this.favoriteCount = favoriteCount;
    }


    public static SelectAllStoreResponseDto selectAllStore(Store store) {
        return SelectAllStoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeNumber(store.getStoreNumber())
                .storeType(store.getStoreType())
                .favoriteCount(store.getFavoriteCount())
                .build();
    }
}
