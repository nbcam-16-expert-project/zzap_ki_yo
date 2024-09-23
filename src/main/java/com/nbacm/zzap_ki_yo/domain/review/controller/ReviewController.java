package com.nbacm.zzap_ki_yo.domain.review.controller;

import com.nbacm.zzap_ki_yo.domain.review.dto.*;
import com.nbacm.zzap_ki_yo.domain.review.service.ReviewService;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록
    @PostMapping
    public ResponseEntity<ReviewSaveResponseDto> saveReview (@Auth AuthUser authUser,
                                                             @RequestBody ReviewSaveRequestDto reviewSaveRequestDto){
        String email = authUser.getEmail();
        return ResponseEntity.ok(reviewService.saveReview(email,reviewSaveRequestDto));
    }

    // 리뷰에 리뷰 등록
    @PostMapping("/{reviewId}")
    public ResponseEntity<ReviewSaveResponseDto> saveReplyReview (@PathVariable Long reviewId,
                                                                  @Auth AuthUser authUser,
                                                                  @RequestBody ReviewSaveRequestDto reviewSaveRequestDto){
        String email = authUser.getEmail();
        return ResponseEntity.ok(reviewService.saveReplyReview(reviewId,email,reviewSaveRequestDto));
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
                                                                 @Auth AuthUser authUser,
                                                                 @RequestBody ReviewUpdateRequestDto reviewUpdate){
        String email = authUser.getEmail();
        return ResponseEntity.ok(reviewService.updateReview(reviewId,email,reviewUpdate.getContent()));
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public void deleteReview (@PathVariable Long reviewId,
                              @Auth AuthUser authUser){
        String email = authUser.getEmail();
        reviewService.deleteReview(reviewId,email);
    }
}
