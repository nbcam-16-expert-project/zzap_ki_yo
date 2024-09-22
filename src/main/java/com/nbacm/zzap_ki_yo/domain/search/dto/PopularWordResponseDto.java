package com.nbacm.zzap_ki_yo.domain.search.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class PopularWordResponseDto {
    private String word;

    @Builder
    private PopularWordResponseDto(String word) {
        this.word = word;
    }

    public static PopularWordResponseDto of(String word) {
        return PopularWordResponseDto.builder().word(word).build();
    }

}
