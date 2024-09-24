package com.nbacm.zzap_ki_yo.domain.user.entity;

import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void testUserBuilder() {
        // User 빌더를 사용하여 객체 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("testNick")
                .name("Test User")
                .password("password123")
                .userRole(UserRole.USER)
                .build();

        // User 엔티티의 필드가 예상대로 설정되었는지 확인
        assertEquals("test@test.com", user.getEmail());
        assertEquals("testNick", user.getNickname());
        assertEquals("Test User", user.getName());
        assertEquals("password123", user.getPassword());
        assertEquals(UserRole.USER, user.getUserRole());
        assertFalse(user.isDeleted());  // 기본 값이 false인지 확인
    }

    @Test
    void testUserUpdate() {
        // User 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("oldNick")
                .name("Test User")
                .password("oldPassword")
                .userRole(UserRole.USER)
                .build();

        // update 메서드를 사용하여 닉네임과 비밀번호 업데이트
        user.update("newNick", "newPassword");

        // 변경된 필드 값 확인
        assertEquals("newNick", user.getNickname());
        assertEquals("newPassword", user.getPassword());
    }
    @Test
    void testAdminUpdate_withNullValues() {
        // User 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("oldNick")
                .name("Test User")
                .password("oldPassword")
                .userRole(UserRole.USER)
                .build();

        // adminUpdate 메서드를 사용하여 일부 필드 업데이트 (null 값을 전달)
        user.adminUpdate(null, null, null, "", "");

        // null 값 또는 빈 문자열이므로 변경되지 않은 필드 확인
        assertEquals(UserRole.USER, user.getUserRole());  // userRole이 null이므로 변경되지 않음
        assertEquals("Test User", user.getName());        // name이 null이므로 변경되지 않음
        assertEquals("oldPassword", user.getPassword());  // password가 null이므로 변경되지 않음
        assertEquals("oldNick", user.getNickname());      // nickname이 빈 문자열이므로 변경되지 않음
        assertEquals("test@test.com", user.getEmail());   // email이 빈 문자열이므로 변경되지 않음
    }
    @Test
    void testAdminUpdate_withValidValues() {
        // User 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("oldNick")
                .name("Test User")
                .password("oldPassword")
                .userRole(UserRole.USER)
                .build();

        // adminUpdate 메서드를 사용하여 모든 필드 업데이트 (유효한 값 전달)
        user.adminUpdate(UserRole.ADMIN, "New Name", "newPassword", "newNick", "new@test.com");

        // 변경된 필드 값 확인
        assertEquals(UserRole.ADMIN, user.getUserRole());  // userRole이 ADMIN으로 변경
        assertEquals("New Name", user.getName());          // name이 "New Name"으로 변경
        assertEquals("newPassword", user.getPassword());   // password가 "newPassword"로 변경
        assertEquals("newNick", user.getNickname());       // nickname이 "newNick"으로 변경
        assertEquals("new@test.com", user.getEmail());     // email이 "new@test.com"으로 변경
    }

    @Test
    void testAdminUpdate() {
        // User 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("oldNick")
                .name("Test User")
                .password("oldPassword")
                .userRole(UserRole.USER)
                .build();

        // adminUpdate 메서드를 사용하여 여러 필드 업데이트
        user.adminUpdate(UserRole.ADMIN, "New Name", "newPassword", "newNick", "new@test.com");

        // 변경된 필드 값 확인
        assertEquals(UserRole.ADMIN, user.getUserRole());
        assertEquals("New Name", user.getName());
        assertEquals("newPassword", user.getPassword());
        assertEquals("newNick", user.getNickname());
        assertEquals("new@test.com", user.getEmail());
    }

    @Test
    void testDeleteAccount() {
        // User 생성
        User user = User.builder()
                .email("test@test.com")
                .nickname("testNick")
                .name("Test User")
                .password("password123")
                .userRole(UserRole.USER)
                .build();

        // 계정 삭제 처리
        user.deleteAccount();

        // 삭제 여부 확인
        assertTrue(user.isDeleted());
        assertNotNull(user.getDeletedAt());
        assertTrue(user.getDeletedAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void testCreateKakaoUser() {
        // KakaoUser 생성
        User kakaoUser = User.createKakaoUser("kakao@test.com", "kakaoNick", "kakaoId123");

        // 생성된 KakaoUser 필드 값 확인
        assertEquals("kakao@test.com", kakaoUser.getEmail());
        assertEquals("kakaoNick", kakaoUser.getNickname());
        assertEquals(UserRole.USER, kakaoUser.getUserRole());
        assertEquals("kakaoUser", kakaoUser.getPassword());  // 임시 비밀번호 확인
        assertEquals("kakaoId123", kakaoUser.getKakaoId());
    }
}
