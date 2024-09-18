package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDto {

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;


    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 8자 이상이어야 하며, 문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다.")
    private String password;


    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;

    private UserRole userRole;


    public UserRequestDto(String email, String name, String nickname, String password, UserRole userRole) {

        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }
}
