package com.nbacm.zzap_ki_yo.domain.store.admin.dto.response;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class CreateStoreResponseDto {

    private Long storeId;
    private String storeName;
    private String storeAddress;
    private String storeNumber;



    private CreateStoreResponseDto(Long storeId, String storeName, String storeAddress, String storeNumber) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
    }


    public static CreateStoreResponseDto createStore(Long storeId, String storeName, String storeAddress, String storeNumber) {
        return new CreateStoreResponseDto(storeId, storeName, storeAddress, storeNumber);
    }
}
