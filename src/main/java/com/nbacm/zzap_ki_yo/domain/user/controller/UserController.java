package com.nbacm.zzap_ki_yo.domain.user.controller;

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
import org.springframework.http.MediaType;
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

    @PostMapping("/admin/update")
    public ResponseEntity<UserResponseDto> updateAdmin(@RequestBody UserRequestDto userRequestDto, @Auth AuthUser authUser) {
        try {
            String email = authUser.getEmail();
            String targetUserEmail = userRequestDto.getEmail();
            UserResponseDto updateUser = userService.updateAdmin(email, targetUserEmail, userRequestDto);
            return ResponseEntity.ok(updateUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestBody UserRequestDto userRequestDto,@Auth AuthUser authUser) {
        String email  = authUser.getEmail();
        userService.deleteUser(email,userRequestDto.getPassword());
        return ResponseEntity.noContent().build();
    }
    @GetMapping(value = "/oauth2/kakao", produces = "application/json;charset=UTF-8")
    public ResponseEntity<String> kakaoLogin(@RequestParam("code") String code) {
        log.info("Received Kakao auth code: {}", code);
        try {
            // userService.kakaoLogin이 내부적으로 accessToken을 얻고 처리한다고 가정합니다.
            String token = userService.kakaoLogin(code);

            log.info("Kakao login successful. JWT token generated.");

            String responseBody = String.format("{\"message\":\"카카오 인증에 성공했습니다.\", \"token\":\"%s\"}", token);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseBody);
        } catch (Exception e) {
            log.error("Error during Kakao login", e);
            String errorResponse = String.format("{\"error\":\"카카오 로그인 중 오류가 발생했습니다.\", \"details\":\"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }

}
