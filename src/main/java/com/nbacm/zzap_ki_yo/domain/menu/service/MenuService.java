package com.nbacm.zzap_ki_yo.domain.menu.service;

import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;

public interface MenuService {
     MenuResponseDto createMenu(String email, UserRole role,Long StoreId, MenuRequestDto menuRequestDto);
     MenuResponseDto updateMenu(String email, UserRole role, Long menuId,MenuRequestDto menuRequestDto);
}
