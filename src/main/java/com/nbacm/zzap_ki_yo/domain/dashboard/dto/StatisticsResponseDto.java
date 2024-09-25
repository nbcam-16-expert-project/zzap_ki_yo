package com.nbacm.zzap_ki_yo.domain.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nbacm.zzap_ki_yo.domain.dashboard.entity.Statistics;
import lombok.Getter;

import java.util.List;

@Getter
public class StatisticsResponseDto {
    private final int totalSales;
    private final int customerCount;

    @JsonCreator
    public StatisticsResponseDto(
            @JsonProperty("totalSales") int totalSales,
            @JsonProperty("customerCount") int customerCount
    ) {
        this.totalSales = totalSales;
        this.customerCount = customerCount;
    }

    // 단일 StoreStatistics로부터 응답 생성
    public static StatisticsResponseDto fromEntity(Statistics statistics) {
        return new StatisticsResponseDto(
                statistics.getTotalSales(),
                statistics.getCustomerCount()
        );
    }

    // 통계 리스트로부터 응답 생성 (월간 통계 합산용)
    public static StatisticsResponseDto from(List<Statistics> statisticsList) {
        int totalSales = statisticsList.stream().mapToInt(Statistics::getTotalSales).sum();
        int customerCount = statisticsList.stream().mapToInt(Statistics::getCustomerCount).sum();

        return new StatisticsResponseDto(totalSales, customerCount);
    }

}
