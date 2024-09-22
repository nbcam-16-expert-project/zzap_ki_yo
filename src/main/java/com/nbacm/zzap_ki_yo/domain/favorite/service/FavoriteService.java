package com.nbacm.zzap_ki_yo.domain.favorite.service;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.favorite.dto.FavoriteSaveReponseDto;
import com.nbacm.zzap_ki_yo.domain.favorite.entity.Favorite;
import com.nbacm.zzap_ki_yo.domain.favorite.repository.FavoriteRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 찜 서비스
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;

    // 찜 등록
    @Transactional
    public FavoriteSaveReponseDto saveFavorite(Long storeId, AuthUser authUser) {

        // 가게가 존재하는지 확인
        Store store = storeRepository.findById(storeId).
                orElseThrow(()-> new NotFoundException("가게를 찾을 수 없습니다."));

        // 유저인지 확인
        if (!authUser.getRole().equals(UserRole.USER)){
            throw new UnauthorizedException("유저가 아니면 찜 기능을 이용할 수 없습니다.");
        }

        // 찜 생성 유저 정보를 받을 수 있게되면 수정 필요 !!!!!
        Favorite favorite = Favorite.builder().
                store(store).
                build();

        // 찜 저장 마찬가지로 유저 정보 받게되면 수정 필요!
        favoriteRepository.save(favorite);
        return FavoriteSaveReponseDto.builder().
                storeId(storeId).
                favoriteId(favorite.getFavoriteId()).build();
    }

    // 찜 취소(삭제)
    @Transactional
    public void deleteFavorite(Long favoriteId, AuthUser authUser) {

        // 찜이 존재하는지 확인
        Favorite favorite = favoriteRepository.findById(favoriteId).
                orElseThrow(()-> new NotFoundException("등록된 찜이 없습니다."));

        // 삭제 권한 확인 authUser != authUser 월요일에 확인 필요!!
        if (!favorite.getUser().equals(authUser)){
            throw new UnauthorizedException("다른 사람의 찜을 취소할 수 없습니다.");
        }

        // 찜 삭제(취소)
        favoriteRepository.delete(favorite);
    }

}
