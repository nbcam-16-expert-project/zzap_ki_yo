package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderUpdateRequest;

import java.util.List;

public interface OrderService {

    OrderSaveResponse saveOrder (String eMail, Long storeId, OrderSaveRequest orderSaveRequest);

    List<OrderSaveResponse> getOrdersByUser (String email);

    List<OrderSaveResponse> getOrdersByUserAdmin (String email, Long userId);

    OrderSaveResponse getOrderById(Long orderId,String email);

    void deleteOrderById(Long orderId, String email);

    void updateOrder (Long storeId, Long orderId, OrderUpdateRequest orderUpdateRequest, String email);
}
