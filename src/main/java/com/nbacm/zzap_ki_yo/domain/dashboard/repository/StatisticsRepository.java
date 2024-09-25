package com.nbacm.zzap_ki_yo.domain.dashboard.repository;

import com.nbacm.zzap_ki_yo.domain.dashboard.entity.Statistics;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    // 가게 ID와 날짜 범위로 통계 조회 (일간/월간 모두 처리 가능)
    // fetch join을 사용해 Store 엔티티를 함께 조회
    @Query("SELECT s FROM Statistics s JOIN FETCH s.store WHERE s.store.storeId = :storeId AND s.date BETWEEN :startDate AND :endDate")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Statistics> findByStoreIdAndDateBetween(Long storeId, LocalDate startDate, LocalDate endDate);

    // 가게 ID와 특정 날짜로 일간 통계 조회
    @Query("SELECT s FROM Statistics s JOIN FETCH s.store WHERE s.store.storeId = :storeId AND s.date = :date")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Statistics> findByStoreIdAndDate(Long storeId, LocalDate date);

    // 특정 날짜의 모든 통계 조회 (Store와 함께 페치 조인)
    @Query("SELECT s FROM Statistics s JOIN FETCH s.store WHERE s.date = :date")
    List<Statistics> findAllByDate(LocalDate date);

    // 특정 기간(월간)의 모든 통계 조회 (Store와 함께 페치 조인)
    @Query("SELECT s FROM Statistics s JOIN FETCH s.store WHERE s.date BETWEEN :startOfMonth AND :endOfMonth")
    List<Statistics> findAllByDateBetween(LocalDate startOfMonth, LocalDate endOfMonth);
}
