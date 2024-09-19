package com.nbacm.zzap_ki_yo.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {
    private String menuName;
    private Integer price;

    public MenuUpdateRequestDto(Long menuId, String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }
}
