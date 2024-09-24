package com.nbacm.zzap_ki_yo.domain.menu.dto;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.entity.MenuStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateRequestDto {

    @NotBlank(message = "메뉴 이름은 필수입니다.")
    private String menuName;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    private MenuStatus status;

    public MenuUpdateRequestDto(String menuName, Integer price, Long storeId, Long menuId, MenuStatus status) {
        this.menuName = menuName;
        this.price = price;
        this.status = status;
    }
    public MenuUpdateRequestDto(String menuName, Integer price, MenuStatus status) {
        this.menuName = menuName;
        this.price = price;
        this.status = status;
    }
}
