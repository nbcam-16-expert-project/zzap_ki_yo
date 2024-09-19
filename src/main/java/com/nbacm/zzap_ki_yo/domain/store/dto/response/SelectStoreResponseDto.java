package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectStoreResponseDto {
    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private Integer favoriteCount;
    private List<MenuNamePrice> menus = new ArrayList<>();


    @Builder
    private SelectStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber, List<MenuNamePrice> menus , Integer favoriteCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.menus = menus;
        this.favoriteCount = favoriteCount;
    }

    public static SelectStoreResponseDto selectStore(Store store, List<MenuNamePrice> menus) {
        return SelectStoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeNumber(store.getStoreNumber())
                .favoriteCount(store.getFavoriteCount())
                .menus(menus)
                .build();

    }
}
