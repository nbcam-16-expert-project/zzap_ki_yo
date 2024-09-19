package com.nbacm.zzap_ki_yo.domain.menu.dto;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuResponseDto {
    private String menuName;
    private Integer price;

    public MenuResponseDto(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }

    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getPrice()
        );
    }
}
