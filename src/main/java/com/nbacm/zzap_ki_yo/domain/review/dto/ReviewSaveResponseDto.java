package com.nbacm.zzap_ki_yo.domain.review.dto;

import com.nbacm.zzap_ki_yo.domain.order.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewSaveResponseDto {

    // 리뷰를 작성하려는 주문
    private final Long oderId;

    // 리뷰 Id
    private final Long reviewId;

    // 부모 리뷰 Id
    private final Long parentReviewId;

    // 리뷰 내용
    private final String content;

    // 별점
    private final int starPoint;

    // 리뷰 작성시간
    private final LocalDateTime createdAt;

    // 리뷰 수정시간
    private final LocalDateTime modifiedAt;



}

