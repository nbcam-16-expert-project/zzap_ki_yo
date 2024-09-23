package com.nbacm.zzap_ki_yo.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Getter
@Builder
@NoArgsConstructor
public class CartItemResponseDto implements Serializable {
    private  Long menuId;
    private  String menuName;
    private  Integer price;
    private  Integer quantity;

    // Jackson이 객체를 역직렬화할 때 사용할 생성자
    @JsonCreator
    public CartItemResponseDto(
            @JsonProperty("menuId") Long menuId,
            @JsonProperty("menuName") String menuName,
            @JsonProperty("price") Integer price,
            @JsonProperty("quantity") Integer quantity) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.quantity = quantity;
    }
}
