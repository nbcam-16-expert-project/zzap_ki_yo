package com.nbacm.zzap_ki_yo.domain.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CreateStoreRequestDto {

    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeNumber;

}
