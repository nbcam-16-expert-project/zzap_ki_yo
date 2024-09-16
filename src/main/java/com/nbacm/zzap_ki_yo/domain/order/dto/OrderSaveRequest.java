package com.nbacm.zzap_ki_yo.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderSaveRequest {

    private String orderType;
    private String orderAddress;
    private List<String> menuList;
}
