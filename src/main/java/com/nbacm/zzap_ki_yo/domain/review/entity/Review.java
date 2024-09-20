package com.nbacm.zzap_ki_yo.domain.review.entity;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor()
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    // 리뷰 수정 시간 추가.
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id2")
    private Review parentReview;

    @OneToMany(mappedBy = "parentReview", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Review> replies;

    // 별점
    @Column(nullable = false)
    private int starPoint;
    // 필수 입력값을 가진 builder 메서드
    public static ReviewBuilder builder(Order order, String content) {
        // 필수값 확인
        if(order == null || content == null)
            throw new IllegalArgumentException("주문과 리뷰 내용은 필수입니다.");

        return new ReviewBuilder().order(order).content(content);}


    // 리뷰 생성자
    @Builder
    public Review(Order order, String content,Review parentReview, List<Review> replies, int starPoint){
        this.order = order;
        this.content = content;
        this.parentReview =parentReview;
        this.replies = replies;
        this.starPoint =starPoint;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    // 리뷰 수정 메서드
    public void update (String content){
        this.content =content;
        this.modifiedAt = LocalDateTime.now();
    }
}
