package com.nbacm.zzap_ki_yo.domain.favorite.service;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.favorite.dto.FavoriteSaveResponseDto;
import com.nbacm.zzap_ki_yo.domain.favorite.entity.Favorite;
import com.nbacm.zzap_ki_yo.domain.favorite.exception.FavoriteNotFoundException;
import com.nbacm.zzap_ki_yo.domain.favorite.repository.FavoriteRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 찜 서비스
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    // 찜 등록
    @Transactional
    public FavoriteSaveResponseDto saveFavorite(Long storeId, String email) {

        // email 정보로 유저 객체 추출
        User authUser = userRepository.findByEmailOrElseThrow(email);

        // 가게가 존재하는지 확인
        Store store = storeRepository.findById(storeId).
                orElseThrow(()-> new StoreNotFoundException("가게를 찾을 수 없습니다."));

        // 유저인지 확인
        if (!authUser.getUserRole().equals(UserRole.USER)){
            throw new UnauthorizedException("유저가 아니면 찜 기능을 이용할 수 없습니다.");
        }

        // 찜 생성
        Favorite favorite = Favorite.builder().
                store(store).
                user(authUser).
                build();

        // 해당 store 의 찜 +1
        store.plusFavoriteCount();

        // 찜 저장
        favoriteRepository.save(favorite);

        return FavoriteSaveResponseDto.builder().
                storeId(storeId).
                userId(authUser.getUserId()).
                favoriteId(favorite.getFavoriteId()).build();
    }

    // 찜 취소(삭제)
    @Transactional
    public void deleteFavorite(Long favoriteId, String email) {

        // email 정보로 유저 객체 추출
        User authUser = userRepository.findByEmailOrElseThrow(email);

        // 찜이 존재하는지 확인
        Favorite favorite = favoriteRepository.findById(favoriteId).
                orElseThrow(()-> new FavoriteNotFoundException("등록된 찜이 없습니다."));

        // 삭제 권한 확인 authUser != authUser 월요일에 확인 필요!!
        if (!favorite.getUser().equals(authUser)){
            throw new UnauthorizedException("다른 사람의 찜을 취소할 수 없습니다.");
        }

        // 찜 삭제(취소)
        favoriteRepository.delete(favorite);
    }

}
