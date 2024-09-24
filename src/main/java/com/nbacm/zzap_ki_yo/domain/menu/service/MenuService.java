package com.nbacm.zzap_ki_yo.domain.menu.service;

import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.springframework.transaction.annotation.Transactional;

public interface MenuService {

     MenuResponseDto createMenu(MenuRequestDto menuRequestDto, String email, UserRole role, Long storeId);

     // 메뉴 수정
     MenuResponseDto updateMenu(String email, UserRole role, Long menuId, Long storeId, MenuUpdateRequestDto menuUpdateRequestDto);

}
