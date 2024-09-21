package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.exception.ForbiddenException;
import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderType;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderUpdateRequest;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderedMenu;
import com.nbacm.zzap_ki_yo.domain.order.exeption.ClosedStoreException;
import com.nbacm.zzap_ki_yo.domain.order.exeption.NotEnoughPriceException;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderedMenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final OrderedMenuRepository orderedMenuRepository;
    private final UserRepository userRepository;

    // 주문하기
    @Transactional
    @Override
    public OrderSaveResponse saveOrder (String eMail, Long storeId, OrderSaveRequest orderSaveRequest) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NotFoundException("해당 가게는 없는 가게입니다."));

        User user = userRepository.findByEmailOrElseThrow(eMail);

        Order order = Order.builder()
                .orderType(OrderType.valueOf(orderSaveRequest.getOrderType()))
                .orderAddress(orderSaveRequest.getOrderAddress())
                .store(store)
                .user(user)
                .orderStatus(OrderStatus.PENDING)
                .reviews(null)
                .orderedMenuList(new ArrayList<>())
                .build();

        // 요청의 메뉴id 리스트를 OrderedMenu(주문-메뉴 중간 테이블)의 리스트로 변경하기
        List<OrderedMenu> orderedMenuList = orderSaveRequest.getMenuList().stream()
                        .map(menuId -> {
                            Menu menu = menuRepository.findById(menuId)
                                    .orElseThrow(()-> new NotFoundException("해당 메뉴는 없는 메뉴입니다."));
                            if(!menu.getStore().getStoreId().equals(storeId)){
                                throw new BadRequestException("다른 가게의 메뉴는 주문할 수 없습니다.");
                            }
                            OrderedMenu orderedMenu = OrderedMenu.builder()
                                    .order(order)
                                    .menu(menu)
                                    .build();
                            order.getOrderedMenuList().add(orderedMenu);
                            return orderedMenu;
                        }).toList();

        // 요청의 메뉴id 리스트에 있는 메뉴들의 가격을 전부 더하고, 그 값이 가게의 최소 주문 금액보다 작으면 예외처리.
        Integer totalPrice = orderSaveRequest.getMenuList().stream().map(menuId -> {
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(()-> new NotFoundException("해당 메뉴는 없는 메뉴입니다."));
            return menu.getPrice();
        }).reduce(0, Integer::sum);
        if(totalPrice < store.getOrderMinPrice()){
            throw new NotEnoughPriceException("최소 주문 금액 미달입니다.");
        }

        //오픈 시간 예외처리
        LocalTime now = LocalTime.now();
        LocalTime open = store.getOpeningTime();
        LocalTime close = store.getClosingTime();
        if(open.isBefore(close)){
            // 시작이 끝 이전일 때(opening is before closing), 즉 영업 시간이 자정을 통과하지 않을 때
            // '현 시각이 끝 이전이며 시작 이후일 때' 가 아닐 때 예외 발생
            if(!(now.isBefore(close) && now.isAfter(open))){
                throw new ClosedStoreException("영업 시간이 아닙니다.");
            }
        }else{ // '시작이 끝 이전일 때(opening is before closing)' 가 아닐 때, 즉 영업 시간이 자정을 통과할 때
            // '현 시각이 시작 이전이거나 끝 이후일 때' 예외 발생
            if (now.isBefore(open) || now.isAfter(close)){
                throw new ClosedStoreException("영업 시간이 아닙니다.");
            }
        }

        orderedMenuRepository.saveAll(orderedMenuList);

        orderRepository.save(order);

        OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(order, orderSaveRequest.getMenuList());

        return orderSaveResponse;
    }

    // 로그인 중인 유저 id를 기준으로 해당되는 모든 주문 조회
    @Override
    public List<OrderSaveResponse> getOrdersByUser (String email) {

        User user = userRepository.findByEmailOrElseThrow(email);
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
    @Override
    public List<OrderSaveResponse> getOrdersByUserAdmin (String email, Long userId) {

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
    @Override
    public OrderSaveResponse getOrderById(Long orderId,String email) {

        User user = userRepository.findByEmailOrElseThrow(email);
        Long userId = user.getUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("해당 주문은 없는 주문입니다."));

        if(!order.getUser().getUserRole().equals(UserRole.ADMIN) && !order.getUser().getUserId().equals(userId)){
            throw new ForbiddenException("다른 사용자의 주문은 조회할 수 없습니다.");
        }

        List<Long> menuIdList = orderedMenuRepository.findMenuIdsByOrder(order);

        OrderSaveResponse orderSaveResponse = OrderSaveResponse.createOrderResponse(order, menuIdList);

        return orderSaveResponse;
    }

    // 주문 id를 기준으로 해당되는 주문 삭제. 주문자 본인과 관리자만 삭제 가능.
    @Transactional
    @Override
    public void deleteOrderById(Long orderId, String email) {

        User user = userRepository.findByEmailOrElseThrow(email);
        Long userId = user.getUserId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("해당 주문은 없는 주문입니다."));

        if(!order.getUser().getUserRole().equals(UserRole.ADMIN) && !order.getUser().getUserId().equals(userId)){
            throw new ForbiddenException("다른 사용자의 주문은 삭제할 수 없습니다.");
        }

        orderRepository.delete(order);
    }

    // ADMIN 혹은 주문받은 가게의 OWNER가 특정 주문 ID를 기준으로 orderStatus 수정.
    @Transactional
    @Override
    public void updateOrder (
            Long storeId,
            Long orderId,
            OrderUpdateRequest orderUpdateRequest,
            String email
    ) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("해당 주문은 없는 주문입니다."));

        User user = userRepository.findByEmailOrElseThrow(email);

        Store store = storeRepository.findById(storeId)
                .orElseThrow(()-> new NotFoundException("해당 가게는 없는 가게입니다."));

        if(!user.getUserId().equals(store.getUser().getUserId()) && !user.getUserRole().equals(UserRole.ADMIN)){
            throw new ForbiddenException("주문을 받은 가게가 아닙니다.");
        }

        String orderStatus = orderUpdateRequest.getOrderStatus();
        order.updateOrderStatus(OrderStatus.valueOf(orderStatus));
    }

    // 주문 '취소'상태로 바꾸기
    @Transactional
    @Override
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new NotFoundException("해당 주문은 없는 주문입니다."));

        order.updateOrderStatus(OrderStatus.CANCELLED);
    }
}


































