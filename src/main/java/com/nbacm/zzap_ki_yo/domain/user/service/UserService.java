package com.nbacm.zzap_ki_yo.domain.user.service;

import com.nbacm.zzap_ki_yo.domain.user.dto.UserRequestDto;
import com.nbacm.zzap_ki_yo.domain.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto signUp(UserRequestDto userRequestDto);

    String login(UserRequestDto userRequestDto);

    String logout(String accessToken);

    UserResponseDto updateUser(String email,UserRequestDto userRequestDto);

    UserResponseDto updateAdmin(String email,String targetUserEmail,UserRequestDto userRequestDto);

    void deleteUser(String email,String password);

    String kakaoLogin(String code);


}
