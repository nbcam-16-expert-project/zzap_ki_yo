package com.nbacm.zzap_ki_yo.domain.user;

import com.nbacm.zzap_ki_yo.domain.favorite.Favorite;
import com.nbacm.zzap_ki_yo.domain.order.entity.Order;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id")
    private Long userId;


    private String email;


    private String nickname;


    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Store> stores;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Order> order;

    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Favorite> favorites;

    @Builder
    public User(String email, String nickname, String name, UserRole userRole,String password) {
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.password = password;
        this.userRole = userRole;
    }


    public void update(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

    }
    public void AdminUpdate(String email,String name){
        this.email = email;
        this.name = name;

    }

    public void deleteAccount(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
