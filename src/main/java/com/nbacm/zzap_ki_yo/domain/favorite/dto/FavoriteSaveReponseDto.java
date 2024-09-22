package com.nbacm.zzap_ki_yo.domain.favorite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//찜 등록 반환값
@Getter
@Builder
@AllArgsConstructor
public class FavoriteSaveReponseDto {
    private final long favoriteId;
    private final Long userId;
    private final Long storeId;
}
