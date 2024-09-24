package com.nbacm.zzap_ki_yo.domain.menu;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.entity.MenuStatus;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.menu.service.MenuServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    @Test
    void createMenu_성공() {
        // given
        String email = "owner@test.com";
        UserRole role = UserRole.OWNER;
        Long storeId = 1L;
        MenuRequestDto menuRequestDto = new MenuRequestDto("Burger", 5000);

        Store store = Store.builder()
                .user(User.builder().email(email).build())
                .build();

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        // when
        MenuResponseDto response = menuService.createMenu(email, role, storeId, menuRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMenuName()).isEqualTo("Burger");
        assertThat(response.getPrice()).isEqualTo(5000);

        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    void createMenu_실패_잘못된_역할() {
        // given
        String email = "owner@test.com";
        UserRole role = UserRole.USER; // OWNER가 아닌 역할
        Long storeId = 1L;
        MenuRequestDto menuRequestDto = new MenuRequestDto("Burger", 5000);

        // when & then
        assertThatThrownBy(() -> menuService.createMenu(email, role, storeId, menuRequestDto))
                .isInstanceOf(InvalidRoleException.class)
                .hasMessage("관리자 만 접근이 가능합니다.");

        verify(menuRepository, never()).save(any(Menu.class)); // save 메서드가 호출되지 않음
    }

    @Test
    void updateMenu_성공() {
        // given
        String email = "owner@test.com";
        UserRole role = UserRole.OWNER;
        Long menuId = 1L;
        Long storeId = 1L;

        MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto("Pizza", 7000, MenuStatus.AVAILABLE);

        Store store = Store.builder()
                .user(User.builder().email(email).build())
                .build();
        ReflectionTestUtils.setField(store, "storeId", storeId);

        Menu menu = Menu.builder()
                .menuName("Burger")
                .price(5000)
                .store(store)
                .build();
        ReflectionTestUtils.setField(menu, "menuId", menuId);

        when(storeRepository.findByIdWithUser(storeId)).thenReturn(Optional.of(store));
        when(menuRepository.findMenuWithStore(menuId)).thenReturn(Optional.of(menu));

        // when
        MenuResponseDto response = menuService.updateMenu(email, role, menuId, storeId, menuUpdateRequestDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getMenuName()).isEqualTo("Pizza");
        assertThat(response.getPrice()).isEqualTo(7000);

        // 엔티티 상태 변경 확인
        assertThat(menu.getMenuName()).isEqualTo("Pizza");
        assertThat(menu.getPrice()).isEqualTo(7000);
        assertThat(menu.getStatus()).isEqualTo(MenuStatus.AVAILABLE);
    }

    @Test
    void updateMenu_실패_다른_매장() {
        // given
        String email = "owner@test.com";
        UserRole role = UserRole.OWNER;
        Long menuId = 1L;
        Long storeId = 1L;
        MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto("Pizza", 7000, null);

        Store store = Store.builder()
                .user(User.builder().email("different_owner@test.com").build()) // 다른 소유자
                .build();
        ReflectionTestUtils.setField(store, "storeId", storeId);


        when(storeRepository.findByIdWithUser(storeId)).thenReturn(Optional.of(store));

        // when & then
        assertThatThrownBy(() -> menuService.updateMenu(email, role, menuId, storeId, menuUpdateRequestDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("자신의 매장에 대해서만  업데이트 가능합니다.");

        verify(menuRepository, never()).save(any(Menu.class)); // 메뉴 저장이 호출되지 않음
    }
}
