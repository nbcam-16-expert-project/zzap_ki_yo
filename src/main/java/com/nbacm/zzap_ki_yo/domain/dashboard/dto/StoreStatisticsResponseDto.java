package com.nbacm.zzap_ki_yo.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nbacm.zzap_ki_yo.domain.dashboard.entity.StoreStatistics;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter

public class StoreStatisticsResponseDto {
    private final int totalSales;
    private final int customerCount;

    @JsonCreator
    public StoreStatisticsResponseDto(
            @JsonProperty("totalSales") int totalSales,
            @JsonProperty("customerCount") int customerCount
    ) {
        this.totalSales = totalSales;
        this.customerCount = customerCount;
    }

    // 단일 StoreStatistics로부터 응답 생성
    public static StoreStatisticsResponseDto fromEntity(StoreStatistics statistics) {
        return new StoreStatisticsResponseDto(
                statistics.getTotalSales(),
                statistics.getCustomerCount()
        );
    }

    // 통계 리스트로부터 응답 생성 (월간 통계 합산용)
    public static StoreStatisticsResponseDto from(List<StoreStatistics> statisticsList) {
        int totalSales = statisticsList.stream().mapToInt(StoreStatistics::getTotalSales).sum();
        int customerCount = statisticsList.stream().mapToInt(StoreStatistics::getCustomerCount).sum();

        return new StoreStatisticsResponseDto(totalSales, customerCount);
    }

}
