package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderedMenu;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.store.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.User;
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
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    // 나중에 까먹지 말고 필요한 다른 repository 주입하기

    @Transactional
    public OrderSaveResponse saveOrder (Long userId, Long storeId, OrderSaveRequest orderSaveRequest) {

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundException(storeId + "번 가게는 없는 가게입니다."));
        User user = new User();// 임시 객체. 로그인된 유저를 가져오는 것으로 바꾸기!!

        Order order = Order.createOrder(
                orderSaveRequest.getOrderType(),
                orderSaveRequest.getOrderAddress(),
                store,
                user,
                "PENDING"
        );

        List<OrderedMenu> orderedMenuList = new ArrayList<>();
        for (Long menuId : orderSaveRequest.getMenuList()) {
            orderedMenuList.add(OrderedMenu.create(
                    order, menuRepository.findById(menuId)
                            .orElseThrow(() -> new NotFoundException("주문에 포함된 " + menuId + "번 메뉴는 없는 메뉴입니다."))
            ));
        }

        Order.addMenu(order, orderedMenuList);

        orderRepository.save(order);

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
