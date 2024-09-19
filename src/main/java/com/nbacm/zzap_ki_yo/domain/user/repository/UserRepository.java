package com.nbacm.zzap_ki_yo.domain.user.repository;

import com.nbacm.zzap_ki_yo.domain.user.User;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickName);

    default User findByEmailOrElseThrow(String email) {
        return this.findByEmail(email).orElseThrow(()->new UserNotFoundException("유저를 찾을수 없습니다"));
    }
}
