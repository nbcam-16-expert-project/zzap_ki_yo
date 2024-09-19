package com.nbacm.zzap_ki_yo.domain.menu.service;


import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.Repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;


    @Override
    @Transactional
    public MenuResponseDto createMenu(String email, UserRole role, Long storeId, MenuRequestDto menuRequestDto) {
        log.info("info:{}{}{}{}",email,role,storeId,menuRequestDto);
        if (role != UserRole.OWNER && role != UserRole.ADMIN) {
            throw new InvalidRoleException("관리자 만 접근이 가능합니다.");
        }
        Store store = storeRepository.findById(storeId).orElseThrow(() -> new StoreNotFoundException("가게가 존재 하지 않습니다"));

        if(!store.getUser().getEmail().equals(email)) {
            throw new UserNotFoundException("자신의 매장에 대해서만 메뉴를 업데이트할 수 있습니다.");
        }
        Menu menu = Menu.builder()
                .menuName(menuRequestDto.getMenuName())
                .price(menuRequestDto.getPrice())
                .store(store)
                .build();
        menuRepository.save(menu);
        return MenuResponseDto.from(menu);
    }

    @Override
    @Transactional
    public MenuResponseDto updateMenu(String email, UserRole role,Long menuId, MenuRequestDto menuRequestDto) {
        if(role != UserRole.OWNER && role != UserRole.ADMIN) {
            throw new InvalidRoleException("관리자만 접근이 가능합니다");
        }
        Menu menu = menuRepository.findById(menuId).orElseThrow(()->new NotFoundException("메뉴를 찾을수 없습니다."));
        if(!menu.getStore().getUser().getEmail().equals(email)) {
            throw new UserNotFoundException("자신의 매장에 대해서만 메뉴를 업데이트 할수 있습니다");
        }
        menu.update(menuRequestDto.getMenuName(), menuRequestDto.getPrice());
        menuRepository.save(menu);
        return MenuResponseDto.from(menu);
    }
}