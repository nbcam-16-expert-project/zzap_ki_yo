package com.nbacm.zzap_ki_yo.domain.user;

import com.nbacm.zzap_ki_yo.domain.favorite.Favorite;
import com.nbacm.zzap_ki_yo.domain.order.Order;
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

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

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

    public void adminUpdate(UserRole userRole, String name, String password, String nickname, String email) {
        if (userRole != null) {
            this.userRole = userRole;
        }
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
        if (nickname != null && !nickname.isEmpty()) {
            this.nickname = nickname;
        }
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }

    public void deleteAccount(){
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
