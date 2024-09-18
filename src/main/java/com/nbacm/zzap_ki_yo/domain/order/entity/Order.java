package com.nbacm.zzap_ki_yo.domain.order.entity;

import com.nbacm.zzap_ki_yo.domain.order.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.OrderType;
import com.nbacm.zzap_ki_yo.domain.review.Review;
import com.nbacm.zzap_ki_yo.domain.store.Store;
import com.nbacm.zzap_ki_yo.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")  // 테이블 이름을 'orders'로 변경
@NoArgsConstructor
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "order_adress", nullable = false)
    private String orderAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Review> reviews;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderedMenu> orderedMenuList;



    public static Order createOrder(String orderType, String orderAddress, Store store, User user, String orderStatus) {
        Order order = new Order();
        order.orderType = OrderType.valueOf(orderType);
        order.orderAddress = orderAddress;
        order.store = store;
        order.user = user;
        order.orderStatus = OrderStatus.valueOf(orderStatus);
        return order;
    }

    public static Order addMenu(Order order, List<OrderedMenu> orderedMenuList){
        order.orderedMenuList = orderedMenuList;
        return order;
    }
}
