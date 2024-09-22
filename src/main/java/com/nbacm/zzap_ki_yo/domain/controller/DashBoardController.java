package com.nbacm.zzap_ki_yo.domain.controller;

import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StoreStatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class DashBoardController {
    private final AdminStoreServiceImpl adminStoreService;

    // 일간 통계 조회 (사장님 또는 관리자)
    @GetMapping("{storeId}/statistics/daily")
    public ResponseEntity<StoreStatisticsResponseDto> getDailyStatistics(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {

        // AdminStoreServiceImpl의 getDailyStatistics 호출
        StoreStatisticsResponseDto statistics = adminStoreService.getDailyStatistics(storeId, authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }

    // 월간 통계 조회 (사장님 또는 관리자)
    @GetMapping("{storeId}/statistics/monthly")
    public ResponseEntity<StoreStatisticsResponseDto> getMonthlyStatistics(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {

        // AdminStoreServiceImpl의 getMonthlyStatistics 호출
        StoreStatisticsResponseDto statistics = adminStoreService.getMonthlyStatistics(storeId, authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }
    // 전체 배달 어플리케이션의 일간 통계 조회 (관리자 전용)
    @GetMapping("/daily-all")
    public ResponseEntity<StoreStatisticsResponseDto> getDailyAllStatistics(
            @Auth AuthUser authUser) {

        // 관리자 권한이 필요
        StoreStatisticsResponseDto statistics = adminStoreService.getDailyAllStatistics(authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }

    // 전체 배달 어플리케이션의 월간 통계 조회 (관리자 전용)
    @GetMapping("/monthly-all")
    public ResponseEntity<StoreStatisticsResponseDto> getMonthlyAllStatistics(
            @Auth AuthUser authUser) {

        // AdminStoreServiceImpl의 getMonthlyAllStatistics 호출 (관리자만 가능)
        StoreStatisticsResponseDto statistics = adminStoreService.getMonthlyAllStatistics(authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }
}
