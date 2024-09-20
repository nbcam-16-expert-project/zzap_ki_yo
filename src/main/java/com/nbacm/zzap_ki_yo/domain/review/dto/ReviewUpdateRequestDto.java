package com.nbacm.zzap_ki_yo.domain.review.dto;

import lombok.Getter;

// 리뷰 수정 요청값
@Getter
public class ReviewUpdateRequestDto {
    // 주문 Id
    private Long orderId;

    // 수정하는 내용
    private String content;
}
