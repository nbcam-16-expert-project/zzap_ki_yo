package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;
import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderedMenu;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderedMenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.User;
import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
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
    private final OrderedMenuRepository orderedMenuRepository;
    private final UserRepository userRepository;

    // 주문하기
    @Transactional
    public OrderSaveResponse saveOrder (String eMail, Long storeId, OrderSaveRequest orderSaveRequest) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException(storeId + "번 가게는 없는 가게입니다."));

        User user = userRepository.findByEmail(eMail).get();

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

        Order orderPlusMenu = Order.addMenu(order, orderedMenuList);
        orderRepository.save(orderPlusMenu);

        OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(orderPlusMenu, orderSaveRequest.getMenuList());

        return orderSaveResponse;
    }

    // 로그인 중인 유저 id를 기준으로 해당되는 모든 주문 조회
    public List<OrderSaveResponse> getOrdersByUser (String email) {

        User user = userRepository.findByEmail(email).get();
        Long userId = user.getUserId();

        List<Order> orderList = orderRepository.findAllByUserId(userId);

        List<OrderSaveResponse> orderSaveResponseList = new ArrayList<>();
        for (Order order : orderList) {
            List<Long> menuIdList = orderedMenuRepository.findMenuIdsByOrder(order);
            OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(order, menuIdList);
            orderSaveResponseList.add(orderSaveResponse);
        }

        return orderSaveResponseList;
    }

    // 특정 유저 id를 기준으로 해당되는 모든 주문 조회(관리자 권한 필요)
    public List<OrderSaveResponse> getOrdersByUserAdmin (String email, Long userId) {

        User admin = userRepository.findByEmail(email).get();
        if(!admin.getUserRole().equals(UserRole.ADMIN)){
            throw new ForbiddenException(admin.getUserId()+ "번 유저는 관리자가 아닙니다.");
        }

        List<Order> orderList = orderRepository.findAllByUserId(userId);

        List<OrderSaveResponse> orderSaveResponseList = new ArrayList<>();
        for (Order order : orderList) {
            List<Long> menuIdList = orderedMenuRepository.findMenuIdsByOrder(order);
            OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(order, menuIdList);
            orderSaveResponseList.add(orderSaveResponse);
        }

        return orderSaveResponseList;
    }

    // 주문 id를 기준으로 해당되는 주문 단건 조회
    public OrderSaveResponse getOrderById(Long orderId,String email) {

        User user = userRepository.findByEmail(email).get();
        Long userId = user.getUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(orderId + "번 주문은 없는 주문입니다."));

        if(!order.getUser().getUserRole().equals(UserRole.ADMIN) && !order.getUser().getUserId().equals(userId)){
            throw new ForbiddenException("다른 사용자의 주문은 조회할 수 없습니다.");
        }

        List<Long> menuIdList = orderedMenuRepository.findMenuIdsByOrder(order);

        OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(order, menuIdList);

        return orderSaveResponse;
    }

    // 주문 id를 기준으로 해당되는 주문 삭제
    @Transactional
    public void deleteOrderById(Long orderId, String email) {

        User user = userRepository.findByEmail(email).get();
        Long userId = user.getUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException(orderId + "번 주문은 없는 주문입니다."));

        if(!order.getUser().getUserRole().equals(UserRole.ADMIN) && !order.getUser().getUserId().equals(userId)){
            throw new ForbiddenException("다른 사용자의 주문은 삭제할 수 없습니다.");
        }

        orderRepository.delete(order);
    }
}
