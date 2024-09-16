package com.nbacm.zzap_ki_yo.domain.order.dto;

import com.nbacm.zzap_ki_yo.domain.order.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.OrderType;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderSaveResponse {
    private final Long id;
    private final OrderType orderType;
    private final String orderAddress;
    private final Long storeId;
    private final Long userId;
    private final OrderStatus orderStatus;
    private final List<Long> menuIds;

    public OrderSaveResponse(Long id, OrderType orderType, String orderAddress, Long storeId, Long userId, OrderStatus orderStatus, List<Long> menuIds) {
        this.id = id;
        this.orderType = orderType;
        this.orderAddress = orderAddress;
        this.storeId = storeId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.menuIds = menuIds;
    }
}
