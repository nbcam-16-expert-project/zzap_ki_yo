package com.nbacm.zzap_ki_yo.domain.review.repository;

import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    //가게 Id로 리뷰 찾는 메서드
    Boolean findByStoreId(Long storeId);

    // 리뷰 별점 구간별 조회 메서드
    Page<Review> findBystarPoint(Long storeId, int minStarPoint, int maxStarPoint, Pageable pageable);


}
