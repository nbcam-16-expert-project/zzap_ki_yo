package com.nbacm.zzap_ki_yo.domain.review.repository;

import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 리뷰 별점 구간별 조회 메서드
    @Query("SELECT r FROM Review r WHERE r.order.store.storeId = :storeId AND r.starPoint BETWEEN :minStarPoint AND :maxStarPoint")
    Page<Review> findReviewsByStoreIdAndStarPointRange(
            @Param("storeId") Long storeId,
            @Param("minStarPoint") int minStarPoint,
            @Param("maxStarPoint") int maxStarPoint,
            Pageable pageable);

}
