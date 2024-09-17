package com.nbacm.zzap_ki_yo.domain.order.entity;

import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class OrderedMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public static OrderedMenu create(Order order, Menu menu) {
        OrderedMenu orderedMenu = new OrderedMenu();
        orderedMenu.order = order;
        orderedMenu.menu = menu;
        return orderedMenu;
    }
}
