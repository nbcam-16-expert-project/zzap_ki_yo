package com.nbacm.zzap_ki_yo.domain.store.dto.response;


import com.nbacm.zzap_ki_yo.domain.store.entity.AdType;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

@Data
public class UpdateStoreResponseDto {
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private Integer orderMinPrice;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String storeNotice;
    private AdType adType;


    @Builder
    private UpdateStoreResponseDto(String storeName, String storeAddress, String storeNumber
    , Integer orderMinPrice, LocalTime openingTime, LocalTime closingTime, String storeNotice,AdType adType){
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.orderMinPrice = orderMinPrice;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.storeNotice = storeNotice;
        this.adType = adType;
    }

    public static UpdateStoreResponseDto updateStoreName(Store store) {
        return new UpdateStoreResponseDto(store.getStoreName(), store.getStoreAddress(), store.getStoreNumber()
        , store.getOrderMinPrice(), store.getOpeningTime(), store.getClosingTime(), store.getStoreNotice(), store.getAdType());
    }

}
