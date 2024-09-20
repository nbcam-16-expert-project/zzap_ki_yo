package com.nbacm.zzap_ki_yo.domain.review.controller;

import com.nbacm.zzap_ki_yo.domain.review.dto.*;
import com.nbacm.zzap_ki_yo.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/reviews")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewSaveResponseDto> saveReview (@RequestBody ReviewSaveRequestDto reviewSaveRequestDto){
        return ResponseEntity.ok(reviewService.saveReview(reviewSaveRequestDto));
    }

    // 리뷰에 리뷰 등록
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewSaveResponseDto> saveReplyReview (@PathVariable Long reviewId,
                                                                  @RequestBody ReviewSaveRequestDto reviewSaveRequestDto){
        return ResponseEntity.ok(reviewService.saveReplyReview(reviewId,reviewSaveRequestDto));
    }


    // 리뷰 조회
    @GetMapping
    public ResponseEntity<Page<ReviewSimpleResponseDto>> getReviewList (@RequestBody Long storeId,
                                                                     @RequestParam (defaultValue = "0", required = false) int pageNo,
                                                                     @RequestParam (defaultValue = "10", required = false) int size,
                                                                        @RequestParam (defaultValue = "0", required = false) int minStarPoint,
                                                                        @RequestParam (defaultValue = "5", required = false) int maxStarPoint){
        return ResponseEntity.ok(reviewService.getReviewList(storeId,minStarPoint,maxStarPoint,pageNo,size));
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewUpdateResponseDto> updateReview (@PathVariable Long reviewId,
                                                                 @RequestBody ReviewUpdateRequestDto reviewUpdate){
        return ResponseEntity.ok(reviewService.updateReview(reviewId,reviewUpdate.getContent()));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReview (@PathVariable Long reviewId){
        reviewService.deleteReview(reviewId);
    }
}
