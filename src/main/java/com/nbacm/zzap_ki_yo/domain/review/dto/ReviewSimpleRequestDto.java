package com.nbacm.zzap_ki_yo.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewSimpleRequestDto {
 private Long storeId;
 private int minStarPoint;
 private int maxStarPoint;
}