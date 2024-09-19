package com.nbacm.zzap_ki_yo.domain.menu.temporary;

import lombok.Getter;

@Getter
public class SaveMenuRequest {
    private String menuName;
    private Integer price;
    private Long storeId;
}
