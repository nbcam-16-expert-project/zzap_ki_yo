package com.nbacm.zzap_ki_yo.domain.user.service;

import com.nbacm.zzap_ki_yo.domain.user.User;
import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.common.util.PasswordUtil;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.exception.AlreadyExistsException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtils jwtUtils;


    @Transactional
    @Override
    public UserResponseDto signUp(UserRequestDto userRequestDto) {
        log.info("Email: {}, Nickname: {}", userRequestDto.getEmail(), userRequestDto.getNickname());

        UserRole role = Optional.ofNullable(userRequestDto.getUserRole()).orElse(UserRole.USER);
        User user = User.builder()
                .email(userRequestDto.getEmail())
                .password(passwordUtil.encode(userRequestDto.getPassword()))
                .name(userRequestDto.getName())
                .nickname(userRequestDto.getNickname())
                .userRole(role).build();
        userRepository.save(user);
        return UserResponseDto.from(user);
    }

    @Transactional
    @Override
    public String login(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmailOrElseThrow(userRequestDto.getEmail());
        if(!passwordUtil.matches(userRequestDto.getPassword(), user.getPassword())){
            throw new UserNotFoundException("비밀번호가 일치 하지 않습니다.");
        }
        if(user.isDeleted()){
            throw new AlreadyExistsException("이미 탈퇴한 계정입니다.");
        }
        String accessToken = jwtUtils.generateToken(user.getEmail(),user.getUserRole());
        String refreshToken = jwtUtils.generateRefreshToken(user.getEmail(),user.getUserRole());
        // Redis에 RefreshToken 저장
        redisTemplate.opsForValue().set(
                "RT:" + user.getEmail(),
                refreshToken,
                jwtUtils.getRefreshTokenExpirationTime(),
                TimeUnit.MILLISECONDS
        );
        return accessToken;
    }

    @Override
    public String logout(String accessToken) {
        jwtUtils.expireAccessToken(accessToken);
        String email = jwtUtils.getUserEmailFromToken(accessToken);
        redisTemplate.delete("RT:"+email);
        return null;
    }


}
