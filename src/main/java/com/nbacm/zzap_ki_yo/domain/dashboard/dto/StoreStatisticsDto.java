package com.nbacm.zzap_ki_yo.domain.dashboard.dto;

import lombok.Data;

@Data
public class StoreStatisticsDto {
    private Long storeId;
    private String storeName;
    private int totalSales;
    private int customerCount;

    public StoreStatisticsDto(Long storeId, String storeName, int totalSales, int customerCount) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.totalSales = totalSales;
        this.customerCount = customerCount;
    }
}
