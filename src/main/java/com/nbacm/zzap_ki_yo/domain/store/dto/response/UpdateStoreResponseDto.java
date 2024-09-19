package com.nbacm.zzap_ki_yo.domain.store.dto.response;


import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UpdateStoreResponseDto {
    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private String message;
    private int status;



    @Builder
    private UpdateStoreResponseDto(String storeName, String message, String storeAddress, String storeNumber,  int status) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.message = message;
        this.status = status;
    }

    public static UpdateStoreResponseDto updateStoreName(Store store) {
        return new UpdateStoreResponseDto(store.getStoreName(),"가게 수정 성공", store.getStoreAddress(), store.getStoreNumber(), HttpStatus.OK.value());
    }

}
