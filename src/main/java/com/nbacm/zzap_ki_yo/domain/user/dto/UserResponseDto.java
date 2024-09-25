package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    // password는 JSON 응답에서 제외되므로 제거할 수 있음
    @JsonIgnore
    private String password;

    public UserResponseDto(String name, String email, String nickname, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getUserRole()
        );
    }
}
