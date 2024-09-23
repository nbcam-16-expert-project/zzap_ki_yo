package com.nbacm.zzap_ki_yo.domain.store.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "store_address", nullable = false)
    private String storeAddress;

    @Column(name = "store_number", nullable = false)
    private String storeNumber;

    @Column(name = "open_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime openingTime;

    @Column(name = "close_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime closingTime;

    @Column(name = "favorite_count", nullable = false)
    private Integer favoriteCount;

    @Column(name = "store_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Column(name = "order_min_price" , nullable = false)
    private Integer orderMinPrice;

    @Enumerated(EnumType.STRING)
    AdType adType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    @JsonBackReference
    private User user;


    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @JsonManagedReference
    private List<Menu> menus;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @JsonManagedReference
    private List<Order> orders;


    @Builder
    public Store(String storeName, String storeAddress, String storeNumber, Integer favoriteCount,StoreType storeType, User user,
            Integer orderMinPrice,  LocalTime openingTime, LocalTime closingTime,AdType adType) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.favoriteCount = favoriteCount;
        this.storeType = storeType;
        this.user = user;
        this.orderMinPrice = orderMinPrice;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.adType = adType;
    }

    public void updateStore(StoreRequestDto dto) {
        this.storeName = dto.getStoreName();
        this.storeAddress = dto.getStoreAddress();
        this.storeNumber = dto.getStoreNumber();
        this.openingTime = dto.getOpeningTime();
        this.closingTime = dto.getClosingTime();
        this.orderMinPrice = dto.getOrderMinPrice();
        this.adType = dto.getAdType();
    }


    public void closingStore(StoreType storeType){
        this.storeType = storeType;
    }

    public static Store createStore(StoreRequestDto dto, User user){
        return Store.builder()
                .storeName(dto.getStoreName())
                .storeAddress(dto.getStoreAddress())
                .storeNumber(dto.getStoreNumber())
                .openingTime(dto.getOpeningTime())
                .closingTime(dto.getClosingTime())
                .orderMinPrice(dto.getOrderMinPrice())
                .favoriteCount(0)
                .user(user)
                .storeType(StoreType.OPENING)
                .adType(dto.getAdType())
                .build();

    }
}
