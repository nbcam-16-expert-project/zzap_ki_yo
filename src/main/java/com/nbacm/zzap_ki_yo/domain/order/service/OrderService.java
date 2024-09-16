package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class OrderService {

    private final OrderRepository orderRepository;
    //private final MenuRepository menuRepository;
    // 나중에 까먹지 말고 필요한 다른 repository 주입하기

    public OrderSaveResponse saveOrder (Long userId, Long storeId, OrderSaveRequest orderSaveRequest) {



        Order order = Order.createOrder(
                orderSaveRequest.getOrderType(),
                orderSaveRequest.getOrderAddress(),
                //다른 repository 생기면 완성하기

        );
        //Order order = new Order(); // 임시 오더임. 잊지 말고 수정하기
        List<Menu> orderMenu = new ArrayList<>();
        for(Long menuId : orderSaveRequest.getMenuList()){
            orderMenu.add(menuRepository.findById(menuId)
                    .orElseThrow(()-> new NotFoundException("주문에 포함된 " + menuId + "번 메뉴는 없는 메뉴입니다.")));
        }

        return new OrderSaveResponse(
                order.getOrderId(),
                order.getOrderType(),
                order.getOrderAddress(),
                order.getStore().getStoreId(),
                order.getUser().getUserId(),
                order.getOrderStatus(),
                orderSaveRequest.getMenuList()

        );
    }
}
