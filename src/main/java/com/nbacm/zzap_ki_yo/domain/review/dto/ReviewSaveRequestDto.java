package com.nbacm.zzap_ki_yo.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {
    //리뷰 달 주문
    Long orderId;
    // 별점
    int starPoint;
    //리뷰 내용
    String content;
}
