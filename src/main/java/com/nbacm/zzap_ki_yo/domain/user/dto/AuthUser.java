package com.nbacm.zzap_ki_yo.domain.user.dto;

import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {
    private final String email;
    private final UserRole role;

    public AuthUser(String email, UserRole role) {
        this.email = email;
        this.role = role;
    }
}
