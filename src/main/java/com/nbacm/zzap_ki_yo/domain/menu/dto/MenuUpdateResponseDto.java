package com.nbacm.zzap_ki_yo.domain.menu.dto;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuUpdateResponseDto {
    private String storeName;
    private String menuName;
    private Integer price;

    public static MenuUpdateResponseDto from(Menu menu) {
        return MenuUpdateResponseDto.builder()
                .storeName(menu.getStore().getStoreName())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .build();
    }
}
