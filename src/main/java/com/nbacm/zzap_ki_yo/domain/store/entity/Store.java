package com.nbacm.zzap_ki_yo.domain.store.entity;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.order.Order;
import com.nbacm.zzap_ki_yo.domain.user.User;
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
    private LocalTime openingTime;

    @Column(name = "close_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime closingTime;

    @Column(name = "favorite_conut", nullable = false)
    private Integer favoriteCount;

    @Column(name = "store_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;


    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Menu> menus;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Order> orders;


    @Builder

    public Store(String storeName, String storeAddress, String storeNumber, Integer favoriteCount,StoreType storeType, User user, LocalTime openingTime, LocalTime closingTime) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
        this.favoriteCount = favoriteCount;
        this.storeType = storeType;
        this.user = user;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public void updateStore(String storeName, String storeAddress, String storeNumber) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeNumber = storeNumber;
    }


    public void closingStore(StoreType storeType){
        this.storeType = storeType;
    }
}
