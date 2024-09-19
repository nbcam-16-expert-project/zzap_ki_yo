package com.nbacm.zzap_ki_yo.domain.user.service;

import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.common.util.PasswordUtil;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.exception.AlreadyExistsException;
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
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

    @Override
    @Transactional
    public UserResponseDto updateUser(String email,UserRequestDto userRequestDto) {
        User user = userRepository.findByEmailOrElseThrow(email);
        UserRole role = user.getUserRole();
        if (role == null) {
            throw new IllegalArgumentException("사용자의 역할이 정의되지 않았습니다.");
        }

        if (role == UserRole.USER) {
            user.update(userRequestDto.getNickname(), passwordUtil.encode(userRequestDto.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return UserResponseDto.from(updatedUser);
    }

    @Override
    public UserResponseDto updateAdmin(String email,String targetUserEmail, UserRequestDto userRequestDto) {

        User user = userRepository.findByEmailOrElseThrow(email);
        if(user.getUserRole() != UserRole.ADMIN){
        throw new InvalidRoleException("관리자 권한이 필요 합니다.");
        }
        User targetUser = userRepository.findByEmailOrElseThrow(targetUserEmail);
        // 비밀번호 인코딩 (비밀번호가 제공된 경우에만)
        String encodedPassword = null;
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isEmpty()) {
            encodedPassword = passwordUtil.encode(userRequestDto.getPassword());
        }
        targetUser.adminUpdate(
                userRequestDto.getUserRole(),
                userRequestDto.getName(),
                encodedPassword,
                userRequestDto.getNickname(),
                userRequestDto.getEmail()
        );
        User updatedUser = userRepository.save(targetUser);
        return UserResponseDto.from(updatedUser);

    }


    @Override
    @Transactional
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmailOrElseThrow(email);
        if(!passwordUtil.matches(password, user.getPassword())){
            throw new UserNotFoundException("비밀번호가 일치 하지 않습니다");
        }
        user.deleteAccount();
        userRepository.save(user);
        redisTemplate.delete("RT:"+user.getEmail());

    }

}
