package com.nbacm.zzap_ki_yo.domain.user;

import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {
    private final UserServiceImpl userService;

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
}
