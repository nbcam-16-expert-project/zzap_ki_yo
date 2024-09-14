package com.nbacm.zzap_ki_yo.domain.store;

import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import com.nbacm.zzap_ki_yo.domain.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

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

    @Column(name = "favorite_conut", nullable = false)
    private Integer favoriteCount;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Menu> menus;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Order> orders;

}
