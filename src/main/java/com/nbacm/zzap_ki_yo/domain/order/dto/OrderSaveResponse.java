package com.nbacm.zzap_ki_yo.domain.order.dto;

import com.nbacm.zzap_ki_yo.domain.order.entity.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderType;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
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

    private OrderSaveResponse(Long id, OrderType orderType, String orderAddress, Long storeId, Long userId, OrderStatus orderStatus, List<Long> menuIds) {
        this.id = id;
        this.orderType = orderType;
        this.orderAddress = orderAddress;
        this.storeId = storeId;
        this.userId = userId;
        this.orderStatus = orderStatus;
        this.menuIds = menuIds;
    }

    public static OrderSaveResponse createOrderResponse(Order order, List<Long> menuIds){
        return new OrderSaveResponse(
                order.getOrderId(),
                order.getOrderType(),
                order.getOrderAddress(),
                order.getStore().getStoreId(),
                order.getUser().getUserId(),
                order.getOrderStatus(),
                menuIds
        );
    }
}
