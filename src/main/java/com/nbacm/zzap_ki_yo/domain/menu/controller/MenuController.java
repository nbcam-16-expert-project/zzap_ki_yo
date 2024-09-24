package com.nbacm.zzap_ki_yo.domain.menu.controller;

import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.service.MenuService;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{storeId}")
    public ResponseEntity<MenuResponseDto> createMenu(
            @Auth AuthUser authUser, @RequestBody MenuRequestDto menuRequestDto, @PathVariable Long storeId) {
        MenuResponseDto menuResponseDto = menuService.createMenu(
                menuRequestDto, authUser.getEmail(), authUser.getRole(), storeId);
        return ResponseEntity.ok(menuResponseDto);
    }

    @PatchMapping("/update/{storeId}/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@Auth AuthUser authUser,
                                                      @PathVariable Long storeId,
                                                      @PathVariable Long menuId,
                                                      @RequestBody MenuUpdateRequestDto menuUpdateRequestDto) {
        MenuResponseDto updateMenu = menuService.updateMenu(authUser.getEmail(),authUser.getRole(),
                menuId, storeId, menuUpdateRequestDto);
        return ResponseEntity.ok(updateMenu);
    }

}
