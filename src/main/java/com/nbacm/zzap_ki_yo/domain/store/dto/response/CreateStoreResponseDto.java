package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Data
@AllArgsConstructor
public class CreateStoreResponseDto {

    private Long storeId;
    private String storeName;
    private String message;
    private int status;




    private CreateStoreResponseDto(Long storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.message = "가게 등록 성공";
        this.status = HttpStatus.CREATED.value();
    }


    public static CreateStoreResponseDto createStore(Long storeId, String storeName) {
        return new CreateStoreResponseDto(storeId, storeName);
    }
}
