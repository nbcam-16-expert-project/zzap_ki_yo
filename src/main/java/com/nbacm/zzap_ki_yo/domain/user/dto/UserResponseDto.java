package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String email;

    private String name;

    private String nickname;

    private UserRole userRole;

    public UserResponseDto(String name, String email, String nickname, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getUserRole()

        );
    }
}
