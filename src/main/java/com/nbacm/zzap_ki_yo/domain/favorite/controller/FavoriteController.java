package com.nbacm.zzap_ki_yo.domain.favorite.controller;

import com.nbacm.zzap_ki_yo.domain.favorite.dto.FavoriteSaveResponseDto;
import com.nbacm.zzap_ki_yo.domain.favorite.service.FavoriteService;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 즐겨찾기 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 즐겨찾기 등록
    @PostMapping("favorites/{storeId}")
    public ResponseEntity<FavoriteSaveResponseDto> saveFavorite (@PathVariable Long storeId,
                                                                 @Auth AuthUser authUser){
        String email = authUser.getEmail();
        return ResponseEntity.ok(favoriteService.saveFavorite(storeId,email));
    }

    // 즐겨찾기 취소(삭제)
    @DeleteMapping("favorites/{favoriteId}")
    public void deleteFavorite (@PathVariable Long favoriteId,
                                @Auth AuthUser authUser){
        String email = authUser.getEmail();
        favoriteService.deleteFavorite(favoriteId,email);
    }
}
