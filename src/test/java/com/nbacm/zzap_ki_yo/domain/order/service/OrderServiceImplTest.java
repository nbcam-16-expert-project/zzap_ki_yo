package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.coupon.repository.CouponRepository;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderedMenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private OrderedMenuRepository orderedMenuRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CouponRepository couponRepository;

    Long userId = 1L;
    User user = new User();
    Long storeId = 1L;
    Store store = new Store();
    List<Long> menuIds = new ArrayList<>();
    Menu menu = new Menu();
    String email = "test@gmail.com";
    Long menuId = 1L;
    OrderSaveRequest orderSaveRequest = new OrderSaveRequest();

    @BeforeEach
    void setUp() {

        setField(user, "email", email);

        setField(store, "storeId", storeId);
        setField(store, "orderMinPrice", 2000);
        setField(store, "openingTime", LocalTime.of(0, 0));
        setField(store, "closingTime", LocalTime.of(23, 59));


        setField(orderSaveRequest, "orderType", "DELIVERY");
        setField(orderSaveRequest, "orderAddress", "1234");

        menuIds.add(menuId);
        setField(orderSaveRequest, "menuList", menuIds);
        setField(orderSaveRequest, "couponId", null);

        setField(menu, "menuId", 1L);
        setField(menu, "price", 50000);
        setField(menu, "store", store);
    }

    @Test
    void saveOrder() {
        //g
        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(userRepository.findByEmailOrElseThrow(email)).willReturn(user);
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));
        //w
        OrderSaveResponse response = orderService.saveOrder(email, storeId, orderSaveRequest);
        //t
        assertNotNull(response);
    }

    @Test
    void getOrdersByUserAdmin() {
        //g
        given(userRepository.findByEmailOrElseThrow(email)).willReturn(user);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        Order order = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        setField(order, "user", user);
        setField(order2, "user", user);
        setField(order3, "user", user);
        setField(order, "store", store);
        setField(order2, "store", store);
        setField(order3, "store", store);
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order2);
        orders.add(order3);
        given(orderRepository.findAllByUserId(1L)).willReturn(orders);
        List<Long> menuIds = new ArrayList<>();
        menuIds.add(1L);
        given(orderedMenuRepository.findMenuIdsByOrder(order)).willReturn(menuIds);
        setField(user, "userRole", UserRole.USER);
        //w
        List<OrderSaveResponse> responseList = orderService.getOrdersByUserAdmin(email, userId);
        //t
        assertNotNull(responseList);
    }

    @Test
    void getOrderById() {
    }

    @Test
    void updateOrder() {
    }
}