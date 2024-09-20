package com.nbacm.zzap_ki_yo.domain.menu.dto;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.entity.MenuStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class MenuResponseDto {
    private String menuName;
    private Integer price;
    private MenuStatus status;

    public MenuResponseDto(String menuName, Integer price, MenuStatus status) {
        this.menuName = menuName;
        this.price = price;
        this.status = status;
    }

    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuName(),
                menu.getPrice(),
                menu.getStatus()

        );
    }
}
