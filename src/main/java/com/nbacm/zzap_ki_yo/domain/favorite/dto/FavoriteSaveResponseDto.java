package com.nbacm.zzap_ki_yo.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// 즐겨찾기 등록 반환값
@Getter
@Builder
@AllArgsConstructor
public class FavoriteSaveResponseDto {
    private final long favoriteId;
    private final Long userId;
    private final Long storeId;
}
