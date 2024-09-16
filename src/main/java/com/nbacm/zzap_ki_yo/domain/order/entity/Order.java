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
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "order_type")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(name = "order_adress", nullable = false)
    private String orderAddress;

    @Column(name = "order_at", nullable = false)
    private LocalDateTime orderAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "orderStatus", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Review> reviews;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderedMenu> orderedMenuList;

    public static Order createOrder(OrderType orderType, String orderAddress, LocalDateTime orderAt, Store store, User user, OrderStatus orderStatus) {
        Order order = new Order();
        order.orderType = orderType;
        order.orderAddress = orderAddress;
        order.orderAt = orderAt;
        order.store = store;
        order.user = user;
        order.orderStatus = orderStatus;
        return order;
    }
}
