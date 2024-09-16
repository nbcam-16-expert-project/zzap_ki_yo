package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class OrderService {

    private final OrderRepository orderRepository;
    // 나중에 까먹지 말고 필요한 다른 repository 주입하기

    public OrderSaveResponse saveOrder (Long userId, Long storeId, OrderSaveRequest orderSaveRequest) {
        //Order order = Order.createOrder();
        Order order = new Order();

        return new OrderSaveResponse(
                order.getOrderId(),
                order.getOrderType(),
                order.getOrderAddress(),
                order.getStore().getStoreId(),
                order.getUser().getUserId(),
                order.getOrderStatus(),
                order.getOrderedMenuList().get()
        )
    }
}
