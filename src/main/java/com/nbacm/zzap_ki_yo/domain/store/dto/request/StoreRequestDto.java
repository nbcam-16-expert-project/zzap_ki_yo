package com.nbacm.zzap_ki_yo.domain.store.dto.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class StoreRequestDto {

    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private Integer orderMinPrice;
    private LocalTime openingTime;
    private LocalTime closingTime;
}

