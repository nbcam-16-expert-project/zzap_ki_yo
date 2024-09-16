package com.nbacm.zzap_ki_yo.domain.review;

import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import jakarta.persistence.*;
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

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id2")
    private Review parentReview;

    @OneToMany(mappedBy = "parentReview", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Review> replies;
}
