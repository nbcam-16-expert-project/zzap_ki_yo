package com.nbacm.zzap_ki_yo.domain.order.repository;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
