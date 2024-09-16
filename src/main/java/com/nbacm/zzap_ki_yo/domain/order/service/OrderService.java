package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class OrderService {

    private final OrderRepository orderRepository;
    // 나중에 다른 repository 주입하기
}
