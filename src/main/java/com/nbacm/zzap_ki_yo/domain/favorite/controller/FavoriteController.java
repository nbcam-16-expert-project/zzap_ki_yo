package com.nbacm.zzap_ki_yo.domain.favorite.controller;

import com.nbacm.zzap_ki_yo.domain.favorite.dto.FavoriteSaveReponseDto;
import com.nbacm.zzap_ki_yo.domain.favorite.service.FavoriteService;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//찜 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 찜 등록
    @PostMapping("favorites/{storeId}")
    public ResponseEntity<FavoriteSaveReponseDto> saveFavorite (@PathVariable Long storeId,
                                                                @Auth AuthUser authUser){
        return ResponseEntity.ok(favoriteService.saveFavorite(storeId,authUser));
    }

    // 찜 취소(삭제)
    @DeleteMapping("favorites/{favoriteId}")
    public void deleteFavorite (@PathVariable Long favoriteId,
                                @Auth AuthUser authUser){
        favoriteService.deleteFavorite(favoriteId,authUser);
    }
}
