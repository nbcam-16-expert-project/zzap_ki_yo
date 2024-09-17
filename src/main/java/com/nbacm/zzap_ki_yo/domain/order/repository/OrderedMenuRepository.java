package com.nbacm.zzap_ki_yo.domain.order.repository;

import com.nbacm.zzap_ki_yo.domain.menu.Menu;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderedMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderedMenuRepository extends JpaRepository<OrderedMenu, Long> {
    @Query("SELECT om.menu FROM OrderedMenu om WHERE om.order = :order")
    List<Menu> findMenuByOrder(@Param("order") Order order);

}
