package com.nbacm.zzap_ki_yo.domain.store.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateStoreNameRequest {
    private String storeName;
    private String storeAddress;
    private String storeNumber;
}

