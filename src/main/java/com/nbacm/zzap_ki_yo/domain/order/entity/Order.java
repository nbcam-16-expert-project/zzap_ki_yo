package com.nbacm.zzap_ki_yo.domain.order.entity;

import com.nbacm.zzap_ki_yo.domain.order.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.OrderType;
import com.nbacm.zzap_ki_yo.domain.review.Review;
import com.nbacm.zzap_ki_yo.domain.store.Store;
import com.nbacm.zzap_ki_yo.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")  // 테이블 이름을 'orders'로 변경
@NoArgsConstructor
public class Order{
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

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public Order(
            OrderType orderType,
            String orderAddress,
            Store store, User user,
            OrderStatus orderStatus,
            List<Review> reviews,
            List<OrderedMenu> orderedMenuList
    ) {
        this.orderType = orderType;
        this.orderAddress = orderAddress;
        this.store = store;
        this.user = user;
        this.orderStatus = orderStatus;
        this.reviews = reviews;
        this.orderedMenuList = orderedMenuList;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @PrePersist
    public void recordOrderedAt(){
        this.orderedAt = LocalDateTime.now();
    }

    public void recordCompletedAt(OrderStatus orderStatus){
        this.orderStatus = orderStatus;
        if(orderStatus.equals(OrderStatus.COMPLETE)){
            this.completedAt = LocalDateTime.now();
        }
    }







    // 롬복을 사용하지 않고 써 본 빌더(원리 이해용)
    /*public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private OrderType orderType;
        private String orderAddress;
        private Store store;
        private User user;
        private OrderStatus orderStatus;
        private List<Review> reviews;
        private List<OrderedMenu> orderedMenuList;

        public Builder orderType(OrderType orderType) {
            this.orderType = orderType;
            return this;
        }

        public Builder orderAddress(String orderAddress) {
            this.orderAddress = orderAddress;
            return this;
        }

        public Builder store(Store store) {
            this.store = store;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder reviews(List<Review> reviews) {
            this.reviews = reviews;
            return this;
        }

        public Builder orderedMenuList(List<OrderedMenu> orderedMenuList) {
            this.orderedMenuList = orderedMenuList;
            return this;
        }
    }*/



    /*public static Order createOrder(String orderType, String orderAddress, Store store, User user, String orderStatus) {
        Order order = new Order();
        order.orderType = OrderType.valueOf(orderType);
        order.orderAddress = orderAddress;
        order.store = store;
        order.user = user;
        order.orderStatus = OrderStatus.valueOf(orderStatus);
        return order;
    }*/

    /*public static Order addMenu(Order order, List<OrderedMenu> orderedMenuList){
        order.orderedMenuList = orderedMenuList;
        return order;
    }*/
}
