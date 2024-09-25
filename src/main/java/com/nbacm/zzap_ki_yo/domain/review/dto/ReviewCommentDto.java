package com.nbacm.zzap_ki_yo.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewCommentDto {

    //주문 Id
    private Long orderId;
    //답글 내용
    private String content;
}
