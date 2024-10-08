package com.nbacm.zzap_ki_yo.domain.order.service;

import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveRequest;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderSaveResponse;
import com.nbacm.zzap_ki_yo.domain.order.dto.OrderUpdateRequest;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface OrderService {

    OrderSaveResponse saveOrder (String eMail, Long storeId, OrderSaveRequest orderSaveRequest);

    List<OrderSaveResponse> getOrdersByUserAdmin (String email, Long userId);

    OrderSaveResponse getOrderById(Long orderId,String email);

    void deleteOrderById(Long orderId, String email);

    void updateOrder (Long storeId, Long orderId, OrderUpdateRequest orderUpdateRequest, String email);

    void cancelOrder(Long orderId);

    StatisticsResponseDto getDailyStatistics(Long storeId, LocalDate date);

    StatisticsResponseDto getMonthlyStatistics(Long storeId, LocalDate startOfMonth, LocalDate endOfMonth);

    StatisticsResponseDto getDailyStatisticsForAllStores(LocalDate date);

    StatisticsResponseDto getMonthlyStatisticsForAllStores(YearMonth yearMonth);



    }
