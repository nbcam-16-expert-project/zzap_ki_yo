package com.nbacm.zzap_ki_yo.domain.user;

import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Slf4j
public class UserController {
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;


    @PostMapping
    public ResponseEntity<UserResponseDto> signUp(@RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto user = userService.signUp(userRequestDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.login(userRequestDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        String expiredToken = userService.logout(token.replace(JwtUtils.BEARER_PREFIX,""));
        return ResponseEntity.ok()
                .header(JwtUtils.AUTHORIZATION_HEADER,JwtUtils.BEARER_PREFIX+expiredToken)
                .body(expiredToken);
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponseDto> update(@RequestBody UserRequestDto userRequestDto, @Auth AuthUser authUser) {
        String email = authUser.getEmail();
        log.info("email:{}",email);
        UserResponseDto updateUser = userService.updateUser(email, userRequestDto);
        System.out.println(email);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestBody UserRequestDto userRequestDto,@Auth AuthUser authUser) {
        String email  = authUser.getEmail();
        userService.deleteUser(email,userRequestDto.getPassword());
        return ResponseEntity.noContent().build();
    }

}
