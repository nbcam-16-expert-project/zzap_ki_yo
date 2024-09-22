package com.nbacm.zzap_ki_yo.domain.review.service;


import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.exception.UncompletedException;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.review.dto.ReviewSaveRequestDto;
import com.nbacm.zzap_ki_yo.domain.review.dto.ReviewSaveResponseDto;
import com.nbacm.zzap_ki_yo.domain.review.dto.ReviewSimpleResponseDto;
import com.nbacm.zzap_ki_yo.domain.review.dto.ReviewUpdateResponseDto;
import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import com.nbacm.zzap_ki_yo.domain.review.repository.ReviewRepository;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    // 리뷰 등록
    @Transactional
    public ReviewSaveResponseDto saveReview(AuthUser authUser,
                                            ReviewSaveRequestDto reviewSaveRequestDto) {

        // 해당 주문이 존재하는지 확인
        Order order = orderRepository.findById(reviewSaveRequestDto.getOrderId()).
                orElseThrow(()-> new NotFoundException("주문을 찾을 수 없습니다."));

        // 주문 상태 확인
        if (!order.getOrderStatus().equals(OrderStatus.COMPLETE)){
            throw new UncompletedException("아직 완료되지 않은 주문입니다.");
        }
        // 주문한 고객이 맞는지 확인
        if (!authUser.getRole().equals(UserRole.USER) || !order.getUser().equals(authUser)){
            throw new UnauthorizedException("주문한 고객이 아닙니다.");
        }

        // 리뷰 만들기 필수값 반영
        Review newReview = Review.builder(order,reviewSaveRequestDto.getContent()).
                starPoint(reviewSaveRequestDto.getStarPoint()).build();

        // 별점은 0~5 점 사이 점수만 줄 수 있도록 예외처리
        if (reviewSaveRequestDto.getStarPoint()<0 || reviewSaveRequestDto.getStarPoint()>5){
            throw new RuntimeException("별점은 0~5 점 사이의 정수만 입력할 수 있습니다.");
        }

        reviewRepository.save(newReview);
        return  ReviewSaveResponseDto.builder().
                orderId(newReview.getOrder().getOrderId()).
                reviewId(newReview.getReviewId()).
                content(newReview.getContent()).
                starPoint(newReview.getStarPoint()).
                createdAt(newReview.getCreatedAt()).
                modifiedAt(newReview.getModifiedAt()).build();
    }

    // 리뷰에 답글 등록
    @Transactional
    public ReviewSaveResponseDto saveReplyReview(Long reviewId,
                                                 AuthUser authUser,
                                                 ReviewSaveRequestDto reviewSaveRequestDto) {

        //주문이 있는지 확인
        Order order = orderRepository.findById(reviewSaveRequestDto.getOrderId()).
                orElseThrow(()-> new NotFoundException("주문을 찾을 수 없습니다."));

        // 리뷰가 있는지 확인 (부모 리뷰)
        Review parentReview = reviewRepository.findById(reviewId).
                orElseThrow(()-> new NotFoundException("리뷰를 찾을 수 없습니다."));

        // 본인 리뷰에 본인이 답글
        if (parentReview.getOrder().getUser().equals(authUser)){
            throw new UnauthorizedException("본인이 작성한 리뷰에는 답글을 작성할 수 없습니다.");
        }


        // 부모 리뷰에 다는 리뷰
        Review newReview = Review.builder(order,reviewSaveRequestDto.getContent()).
                parentReview(parentReview).build();

        reviewRepository.save(newReview);
        return ReviewSaveResponseDto.builder().
                orderId(newReview.getOrder().getOrderId()).
                reviewId(newReview.getReviewId()).
                parentReviewId(newReview.getParentReview().getReviewId()).
                content(newReview.getContent()).
                starPoint(newReview.getStarPoint()).
                createdAt(newReview.getCreatedAt()).
                modifiedAt(newReview.getModifiedAt()).build();
    }

    // 리뷰 조회
    public Page<ReviewSimpleResponseDto> getReviewList(Long storeId,
                                                       int pageNo,
                                                       int size,
                                                       int minStarPoint,
                                                       int maxStarPoint) {
        // 가게가 존재하는지 확인
        storeRepository.findById(storeId).
                orElseThrow(()-> new NotFoundException("가게가 존재하지 않습니다."));

        // 페이징 + 리뷰를 가장 최근에 수정한 순서대로 정렬
        Pageable pageable = PageRequest.of(pageNo,size, Sort.by("modifiedAt").descending());

        // 가게에 있는 별점으로 조회할 구간 설정 따로 입력하지 않으면 0~5 모두 보여줌
        Page<Review> reviewList = reviewRepository.
                findBystarPoint(storeId,minStarPoint,maxStarPoint,pageable);

        // 찾은 리뷰를 Dto 에 담아서 리턴
        return reviewList.map(ReviewSimpleResponseDto::new);
    }



    // 리뷰수정
    @Transactional
   public ReviewUpdateResponseDto updateReview(Long reviewId,AuthUser authUser, String content) {

        // 리뷰가 있는지 확인 예외처리 관련해서 확인 필요
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(()-> new NotFoundException("리뷰를 찾을 수 없습니다."));

        // 수정 권한 확인
        if(!authUser.getRole().equals(UserRole.ADMIN)){
            throw new UnauthorizedException("리뷰 수정은 관리자만 가능합니다. 짭기요에 문의해주세요.");
        }
        // 리뷰 수정
        review.update(content);

        // 수정된 내용 Dto 에 담아서 리턴. 빌더 사용하는 방법 확인 후 수정
        return ReviewUpdateResponseDto.builder().
                orderId(review.getOrder().getOrderId()).
                reviewId(reviewId).
                parentReviewId(review.getParentReview().getReviewId()).
                content(review.getContent()).
                starPoint(review.getStarPoint()).
                createdAt(review.getCreatedAt()).
                modifiedAt(review.getModifiedAt()).build();
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId,AuthUser authUser) {

        // 리뷰가 있는지 확인
        Review review = reviewRepository.findById(reviewId).
                orElseThrow(()-> new NotFoundException("리뷰를 찾을 수 없습니다."));

        // 리뷰를 삭제할 권한이 있는 유저인지 확인 admin only
        if (!authUser.getRole().equals(UserRole.ADMIN)){
            throw new UnauthorizedException("리뷰 삭제는 관리자만 가능합니다. 짭기요에 문의해주세요.");
        }

        // 리뷰 삭제
        reviewRepository.delete(review);
    }



}
