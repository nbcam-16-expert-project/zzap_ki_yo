package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.user.User;

import java.util.List;

public class OrderResponseDto {
    private String nickname;

    private List<Order> orderList;

    public OrderResponseDto(String nickname, List<Order> orderList) {
        this.nickname = nickname;
        this.orderList = orderList;
    }
}


