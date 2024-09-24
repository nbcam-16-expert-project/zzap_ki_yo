package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.favorite.entity.Favorite;
import com.nbacm.zzap_ki_yo.domain.store.entity.Category;
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
    private List<Category> categoryList;


    @Builder
    private SelectStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber, List<MenuNamePrice> menus , Integer favoriteCount, List<Category> categoryList) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.menus = menus;
        this.favoriteCount = favoriteCount;
        this.categoryList = categoryList;
    }

    public static SelectStoreResponseDto selectStore(Store store, List<MenuNamePrice> menus) {
        return SelectStoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeNumber(store.getStoreNumber())
                .favoriteCount(store.getFavoriteCount())
                .menus(menus)
                .categoryList(store.getCategoryList())
                .build();

    }
}
