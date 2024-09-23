package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Data
public class CreateStoreResponseDto {

    private Long storeId;
    private String storeName;
    private StoreType storeType;
    private String storeAddress;
    private String storeNumber;
    private Integer orderMinPrice;

    @Builder
    private CreateStoreResponseDto(Long storeId, String storeName, StoreType storeType, String storeAddress, String storeNumber
    ,Integer orderMinPrice) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.orderMinPrice = orderMinPrice;
    }


    public static CreateStoreResponseDto createStore(Store store) {
        return new CreateStoreResponseDto(
                store.getStoreId(),
                store.getStoreName(),
                store.getStoreType(),
                store.getStoreAddress(),
                store.getStoreNumber(),
                store.getOrderMinPrice()
        );
    }
}
