package com.nbacm.zzap_ki_yo.Review;


import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderStatus;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderType;
import com.nbacm.zzap_ki_yo.domain.order.entity.OrderedMenu;
import com.nbacm.zzap_ki_yo.domain.order.repository.OrderRepository;
import com.nbacm.zzap_ki_yo.domain.review.dto.*;
import com.nbacm.zzap_ki_yo.domain.review.entity.Review;
import com.nbacm.zzap_ki_yo.domain.review.repository.ReviewRepository;
import com.nbacm.zzap_ki_yo.domain.review.service.ReviewService;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    StoreRepository storeRepository;

    @InjectMocks
    private ReviewService reviewService;

//    @BeforeEach

    @Test
    public void review_리뷰_등록_성공 (){
        // given
        String email = "email";
        ReviewSaveRequestDto requestDto =new ReviewSaveRequestDto(1L,1,"리뷰내용");

        
        User user = new User("email", "nickname", "name", UserRole.USER, "password", "kakaoId");
        Store store = mock(Store.class);
        List<Review> reviewList = mock(List.class);
        List<OrderedMenu> orderedMenuList = mock(List.class);

        Order order = new Order(OrderType.DELIVERY, "address", store, user, OrderStatus.COMPLETE, reviewList, orderedMenuList, 1);

        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user);
        when(orderRepository.findByOrderIdOrElseThrow(anyLong())).thenReturn(order);


        Review review = Review.builder(order,requestDto.getContent()).build();

        // when
        ReviewSaveResponseDto result = reviewService.saveReview(email,requestDto);

        //then
        assertNotNull(result);
        assertEquals(requestDto.getContent(),result.getContent());
    }

    @Test
    public void review_리뷰_답글_성공(){
        //given
        String email = "AdminEmail";
        ReviewCommentDto reviewCommentDto = new ReviewCommentDto(1L,"맛있게 드셔서 다행입니다.");

        // 가짜 객체 생성
        User admin = mock(User.class);   // 관리자(Admin) Mock 객체
        Order order = mock(Order.class); // 주문(Order) Mock 객체
        Review parentReview = mock(Review.class); // 리뷰(Review) Mock 객체


        // 가짜 객체 설정 : 어드민권한 유저, 주문,리뷰
        when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(admin);
        when(admin.getUserRole()).thenReturn(UserRole.ADMIN);
        when(orderRepository.findByOrderIdOrElseThrow(anyLong())).thenReturn(order);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(parentReview));

        //when
        ReviewSaveResponseDto result = reviewService.saveReplyReview(1L, email, reviewCommentDto);

        //then
        assertNotNull(result);
        assertEquals(reviewCommentDto.getContent(),result.getContent());
    }


    @Test
    public void review_리뷰_조회_성공(){
        //given
        Long storeId = 1L;
        int pageNo = 0;
        int size = 5;
        int minStarPoint = 1;
        int maxStarPoint = 5;

        // 가짜 객체 생성: 리뷰 2개 생성
        Review review1 = new Review(new Order(),"첫번째 리뷰 내용",null,null,3);
        Review review2 = new Review(new Order(),"두번째 리뷰 내용", null,null,2);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(new Store()));

        // 조회할 리뷰 리스트
        List<Review> reviews = List.of(review1,review2);

        // 페이징
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by("modifiedAt").descending());
        Page<Review> reviewPage = new PageImpl<>(reviews, Pageable.ofSize(size), reviews.size());
        when(reviewRepository.findReviewsByStoreIdAndStarPointRange(storeId, minStarPoint, maxStarPoint, pageable)).thenReturn(reviewPage);

        //when
        Page<ReviewSimpleResponseDto> result = reviewService.getReviewList(storeId, pageNo, size, minStarPoint, maxStarPoint);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements()); // 2개의 리뷰가 조회되었는지 확인
        assertEquals(2, result.getContent().size()); // 리스트 크기가 2인지 확인
        assertEquals("첫번째 리뷰 내용", result.getContent().get(0).getContent()); // 첫 번째 리뷰 내용 확인
        assertEquals("두번째 리뷰 내용", result.getContent().get(1).getContent()); // 두 번째 리뷰 내용 확인
    }




    @Test
    public void review_수정_성공() {
        // given
        Long reviewId = 1L;
        String email = "admin@example.com";
        String beforeContent = "기존 리뷰 내용";
        String content = "수정된 리뷰 내용";

        // 가짜 객체 생성
        User adminUser = mock(User.class);
        Review review = spy(new Review());
        Order order = mock(Order.class);
        Review parentReview = mock(Review.class);

        // 리뷰 내용 설정(수정전)
        review.update(beforeContent);

        // 가짜 객체 설정
        when(userRepository.findByEmailOrElseThrow(email)).thenReturn(adminUser);
        when(adminUser.getUserRole()).thenReturn(UserRole.ADMIN);
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(review.getOrder()).thenReturn(order);
        when(order.getOrderId()).thenReturn(100L);
        when(review.getParentReview()).thenReturn(parentReview);
        when(parentReview.getReviewId()).thenReturn(10L);


        // when
        ReviewUpdateResponseDto result = reviewService.updateReview(reviewId, email, content);

        // then
        assertNotNull(result);

        verify(review).update(content);
    }



}


