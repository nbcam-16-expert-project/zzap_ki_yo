package com.nbacm.zzap_ki_yo.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClosingStoreRequestDto {
    @NotBlank(message = "\"페업합니다\" 를 입력 해주세요")
    private String message;
}
