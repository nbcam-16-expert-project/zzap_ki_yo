package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import lombok.Data;

@Data
public class ClosingStoreResponseDto {

    private String message;
    private int status;


    private ClosingStoreResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public static ClosingStoreResponseDto closingStore(String message, int status) {
        return new ClosingStoreResponseDto(message, status);
    }
}

