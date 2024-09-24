package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.AdType;
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
    private AdType adType;


    @Builder
    public SelectAllStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber,
                                     StoreType storeType, Integer favoriteCount, AdType adType) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.storeType = storeType;
        this.favoriteCount = favoriteCount;
        this.adType = adType;
    }


    public static SelectAllStoreResponseDto selectAllStore(Store store) {
        return SelectAllStoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeNumber(store.getStoreNumber())
                .storeType(store.getStoreType())
                .favoriteCount(store.getFavoriteCount())
                .adType(store.getAdType())
                .build();
    }
}
