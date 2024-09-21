package com.nbacm.zzap_ki_yo.domain.dashboard.repository;

import com.nbacm.zzap_ki_yo.domain.dashboard.entity.StoreStatistics;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StoreStatisticsRepository extends JpaRepository<StoreStatistics, Long> {
    // 가게 ID와 날짜 범위로 통계 조회 (일간/월간 모두 처리 가능)
    // fetch join을 사용해 Store 엔티티를 함께 조회
    @Query("SELECT ss FROM StoreStatistics ss JOIN FETCH ss.store WHERE ss.store.storeId = :storeId AND ss.date BETWEEN :startDate AND :endDate")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<StoreStatistics> findByStoreIdAndDateBetween(Long storeId, LocalDate startDate, LocalDate endDate);

    // 가게 ID와 특정 날짜로 일간 통계 조회
    @Query("SELECT ss FROM StoreStatistics ss JOIN FETCH ss.store WHERE ss.store.storeId = :storeId AND ss.date = :date")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<StoreStatistics> findByStoreIdAndDate(Long storeId, LocalDate date);


}
