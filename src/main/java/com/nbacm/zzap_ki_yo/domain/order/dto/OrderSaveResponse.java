package com.nbacm.zzap_ki_yo.domain.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderSaveResponse {
    private final Long id;
    private final String orderType;
    private final String orderAddress;
    private final Long storeId;
    private final Long userId;
    private final String orderStatus;
    private final List<Long> menuIds;

    public OrderSaveResponse(Long id, String orderType, String orderAddress, Long storeId, Long userId, String orderStatus, List<Long> menuIds) {
        this.id = id;
        this.orderType = orderType;
        this.orderAddress = orderAddress;
        this.storeId = storeId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.menuIds = menuIds;
    }
}
