package com.nbacm.zzap_ki_yo.domain.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class AddToCartRequestDto {

    @NotNull(message = "메뉴 ID는 필수입니다.")
    private  Long menuId;

    @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
    private  Integer quantity;

    public AddToCartRequestDto(Long menuId, Integer quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }
}
