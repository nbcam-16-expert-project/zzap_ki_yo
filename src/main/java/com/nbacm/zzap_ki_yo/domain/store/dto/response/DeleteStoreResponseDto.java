package com.nbacm.zzap_ki_yo.domain.store.dto.response;


import lombok.Data;

@Data
public class DeleteStoreResponseDto {
    private String message;
    private int status;


    private DeleteStoreResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }


    public static DeleteStoreResponseDto delete(String message, int status) {
        return new DeleteStoreResponseDto(message, status);
    }
}
