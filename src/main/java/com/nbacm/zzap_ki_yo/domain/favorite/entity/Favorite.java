package com.nbacm.zzap_ki_yo.domain.favorite.entity;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
// 즐겨찾기 (찜)
@Entity
@Getter
@Table(name = "favorite")
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Builder
    public Favorite(User user, Store store){
        this.user = user;
        this.store = store;
    }
}
