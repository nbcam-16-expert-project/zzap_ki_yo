package com.nbacm.zzap_ki_yo.domain.order.repository;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);
}
