package com.nbacm.zzap_ki_yo.domain.store.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
public class MenuNamePrice {

    private String menuName;
    private Integer price;

    @Builder
    public MenuNamePrice(String menuName, Integer price){
        this.menuName = menuName;
        this.price = price;
    }


}
