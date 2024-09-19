package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.nbacm.zzap_ki_yo.domain.order.Order;

import java.util.List;

public class OrderResponseDto {
    private String nickname;

    private List<Order> orderList;

    public OrderResponseDto(String nickname, List<Order> orderList) {
        this.nickname = nickname;
        this.orderList = orderList;
    }
}


