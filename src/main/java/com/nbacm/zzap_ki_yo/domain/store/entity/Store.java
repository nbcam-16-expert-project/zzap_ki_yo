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
import java.util.ArrayList;
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

    // 즐겨찾기
    @Column(name = "favorite_count")
    private Integer favoriteCount =0;

    @Column(name = "store_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @Column(name = "order_min_price" , nullable = false)
    private Integer orderMinPrice;
    //가게 공지
    @Column(name = "store_notice")
    private String storeNotice;

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

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "store_categories", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "category")
    private List<Category> categoryList = new ArrayList<>();

    // 즐겨찾기(찜) 추가시 +1
    public void plusFavoriteCount (){
        this.favoriteCount++;
    }


    @Builder
    public Store(String storeName, String storeAddress, String storeNumber, Integer favoriteCount,StoreType storeType, User user,
            Integer orderMinPrice,  LocalTime openingTime, LocalTime closingTime,AdType adType, String storeNotice, List<Category> categoryList
    ,List<Menu> menus, List<Order> orders) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.storeNotice = storeNotice;
        this.favoriteCount = favoriteCount;
        this.storeType = storeType;
        this.user = user;
        this.orderMinPrice = orderMinPrice;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.categoryList = categoryList;
        this.adType = adType;
        this.menus = menus;
        this.orders = orders;
    }

    public void updateStore(StoreRequestDto dto) {
        this.storeName = dto.getStoreName();
        this.storeAddress = dto.getStoreAddress();
        this.storeNumber = dto.getStoreNumber();
        this.openingTime = dto.getOpeningTime();
        this.closingTime = dto.getClosingTime();
        this.orderMinPrice = dto.getOrderMinPrice();
        this.storeNotice = dto.getStoreNotice();
        this.categoryList = dto.getCategoryList();
        this.adType = dto.getAdType();
        this.favoriteCount =dto.getFavoriteCount();
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
                .favoriteCount(dto.getFavoriteCount())
                .user(user)
                .storeType(StoreType.OPENING)
                .storeNotice(dto.getStoreNotice())
                .categoryList(dto.getCategoryList())
                .adType(dto.getAdType())
                .build();

    }
}
