package com.nbacm.zzap_ki_yo.domain.review.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {
    //리뷰 달 주문
    private Long orderId;
    // 별점
    private int starPoint;
    //리뷰 내용
    private String content;
}
