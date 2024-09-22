package com.nbacm.zzap_ki_yo.domain.dashboard.entity;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@BatchSize(size = 20)
public class StoreStatistics {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private LocalDate date; // 날짜별 통계를 관리하기 위한 필드

    private int totalSales; // 총 매출

    private int customerCount; // 고객 수

    @Builder
    public StoreStatistics(Store store, LocalDate date, int totalSales, int customerCount) {
        this.store = store;
        this.date = date;
        this.totalSales = totalSales;
        this.customerCount = customerCount;
    }
    public void updateSalesAndCustomers(int totalSales, int customerCount) {
        this.totalSales = totalSales;
        this.customerCount = customerCount;
    }
}
