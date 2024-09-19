package com.nbacm.zzap_ki_yo.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

// 리뷰 수정 반환값
@Getter
@Builder
@AllArgsConstructor
public class ReviewUpdateResponseDto {
    // 리뷰가 달려있는 주문
    private final Long orderId;

    // 리뷰 Id
    private final Long reviewId;

    // 리뷰를 수정하는 내용
    private final String content;

    // 리뷰 별점
    private final int starPoint;

    //리뷰가 생성된 시간
    private final LocalDateTime createdAt;

    //리뷰를 수정하는 시간
    private final LocalDateTime modifiedAt;
}
