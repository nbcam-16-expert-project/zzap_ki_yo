package com.nbacm.zzap_ki_yo.domain.cart.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class CartResponseDto {
    private  String userEmail;
    private  Long storeId;
    private  String storeName;
    private  List<CartItemResponseDto> items;
    private Integer totalPrice;  // totalPrice 필드를 추가



    @JsonCreator
    public CartResponseDto(
            @JsonProperty("userEmail") String userEmail,
            @JsonProperty("storeId") Long storeId,
            @JsonProperty("storeName") String storeName,
            @JsonProperty("items") List<CartItemResponseDto> items,
            @JsonProperty("totalPrice") Integer totalPrice  // totalPrice를 생성자에 포함


    ) {
        this.userEmail = userEmail;
        this.storeId = storeId;
        this.storeName = storeName;
        this.items = items;
        this.totalPrice = totalPrice != null ? totalPrice : calculateTotalPrice();  // 계산된 값 설정
    }
    private Integer calculateTotalPrice() {
        return items.stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
