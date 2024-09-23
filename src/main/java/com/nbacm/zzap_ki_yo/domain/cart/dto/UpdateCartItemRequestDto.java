package com.nbacm.zzap_ki_yo.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class UpdateCartItemRequestDto {
    @NotNull(message = "메뉴 ID는 필수입니다.")
    private  Long menuId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    private  Integer quantity;


    public UpdateCartItemRequestDto(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
