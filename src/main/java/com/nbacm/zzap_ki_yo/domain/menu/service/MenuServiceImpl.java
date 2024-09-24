package com.nbacm.zzap_ki_yo.domain.menu.service;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.exception.MenuNotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.exception.RoleInvalidException;
import com.nbacm.zzap_ki_yo.domain.menu.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService{

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MenuResponseDto createMenu(MenuRequestDto menuRequestDto, String email, UserRole role, Long storeId) {
        if(role != UserRole.OWNER && role != UserRole.ADMIN) {
            throw new RoleInvalidException("관리자만 접근이 가능합니다");
        }

        Store store = storeRepository.findById(storeId).orElseThrow(()-> new StoreNotFoundException("올바르지 않은 가게입니다"));

        if(!store.getUser().getEmail().equals(email)) {
            throw new UserNotFoundException("자신의 가게에만 설정이 가능합니다");
        }

        Menu createMenu = Menu.builder()
                .menuName(menuRequestDto.getMenuName())
                .price(menuRequestDto.getPrice())
                .store(store)
                .build();

        menuRepository.save(createMenu);

        return MenuResponseDto.from(createMenu);
    }

    @Override
    @Transactional
    public MenuResponseDto updateMenu(String email, UserRole role, Long menuId, Long storeId, MenuUpdateRequestDto menuUpdateRequestDto) {

        if (role != UserRole.OWNER && role != UserRole.ADMIN) {
            throw new InvalidRoleException("관리자만 접근이 가능합니다");
        }
        Store store = storeRepository.findByIdWithUser(storeId)
                .orElseThrow(() -> new UserNotFoundException("매장을 찾을 수 없습니다."));
        if (!store.getUser().getEmail().equals(email)) {
            throw new UserNotFoundException("자신의 매장에 대해서만  업데이트 가능합니다.");
        }
        Menu menu = menuRepository.findMenuWithStore(menuId)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을수 없습니다."));

// 메뉴와 매장 간 일관성 확인 (해당 매장의 메뉴인지 검증)
        if (!menu.getStore().getStoreId().equals(storeId)) {
            throw new NotFoundException("해당 매장에 해당하는 메뉴를 찾을 수 없습니다.");
        }
        menu.updateMenu(menuUpdateRequestDto.getMenuName(), menuUpdateRequestDto.getPrice());
        if (menuUpdateRequestDto.getStatus() != null) {
            menu.changeStatus(menuUpdateRequestDto.getStatus());
        }
        return MenuResponseDto.from(menu);
    }


}
