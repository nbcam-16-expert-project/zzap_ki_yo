package com.nbacm.zzap_ki_yo.domain.order.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
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
    @JsonBackReference
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Review> reviews;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderedMenu> orderedMenuList;

    @Column(name = "order_at")
    private LocalDateTime orderedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    private Integer totalPrice;

    @Builder
    public Order(
            OrderType orderType,
            String orderAddress,
            Store store, User user,
            OrderStatus orderStatus,
            List<Review> reviews,
            List<OrderedMenu> orderedMenuList,
            Integer totalPrice
    ) {
        this.orderType = orderType;
        this.orderAddress = orderAddress;
        this.store = store;
        this.user = user;
        this.orderStatus = orderStatus;
        this.reviews = reviews;
        this.orderedMenuList = orderedMenuList;
        this.totalPrice = totalPrice;
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
    public String toEventData() {
        return String.format("id:%d\ndata:{\"orderId\":%d,\"status\":\"%s\",\"storeId\":%d}\n\n",
                this.orderId, this.orderId, this.orderStatus, this.store.getStoreId());
    }
}
