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

@RestController
@RequestMapping("/api/v1/stores/{storeId}/statistics")
@RequiredArgsConstructor
public class DashBoardController {
    private final AdminStoreServiceImpl adminStoreService;

    // 일간 통계 조회 (사장님 또는 관리자)
    @GetMapping("/daily")
    public ResponseEntity<StoreStatisticsResponseDto> getDailyStatistics(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {

        // AdminStoreServiceImpl의 getDailyStatistics 호출
        StoreStatisticsResponseDto statistics = adminStoreService.getDailyStatistics(storeId, authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }

    // 월간 통계 조회 (사장님 또는 관리자)
    @GetMapping("/monthly")
    public ResponseEntity<StoreStatisticsResponseDto> getMonthlyStatistics(
            @PathVariable Long storeId,
            @Auth AuthUser authUser) {

        // AdminStoreServiceImpl의 getMonthlyStatistics 호출
        StoreStatisticsResponseDto statistics = adminStoreService.getMonthlyStatistics(storeId, authUser.getEmail());
        return ResponseEntity.ok(statistics);
    }
}
