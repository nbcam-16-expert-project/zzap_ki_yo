package com.nbacm.zzap_ki_yo.domain.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long menuId;

    @Column(name = "menu_name", nullable = false)
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference
    private Store store;

    @Enumerated(EnumType.STRING)
    private MenuStatus status = MenuStatus.AVAILABLE;

    @Builder
    public Menu(String menuName, Integer price, Store store) {
        this.menuName = menuName;
        this.price = price;
        this.store = store;
    }

    public void update(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }

    public void changeStatus(MenuStatus newStatus) {
        this.status = newStatus;
    }



}
