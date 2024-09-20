package com.nbacm.zzap_ki_yo.domain.review.dto;

import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 리뷰 전체 조회
@Getter
@AllArgsConstructor
public class ReviewSimpleResponseDto {
    private final Long orderId;
    private final Long reviewId;
    private final String content;
    private final int starPoint;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public ReviewSimpleResponseDto(Review review){
        this.orderId = review.getOrder().getOrderId();
        this.reviewId = review.getReviewId();
        this.content = review.getContent();
        this.starPoint = review.getStarPoint();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt =review.getModifiedAt();
    }
}
