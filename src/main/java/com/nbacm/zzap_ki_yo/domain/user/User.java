package com.nbacm.zzap_ki_yo.domain.user;

import com.nbacm.zzap_ki_yo.domain.favorite.Favorite;
import com.nbacm.zzap_ki_yo.domain.order.Order;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_Id")
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "UserRole", nullable = false)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    private List<Order> orders;

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
    public void update(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
