package com.nbacm.zzap_ki_yo.domain.store.dto.request;

import com.nbacm.zzap_ki_yo.domain.store.entity.Category;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class StoreRequestDto {

    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private Integer orderMinPrice;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String storeNotice;
    private List<Category> categoryList;
}

