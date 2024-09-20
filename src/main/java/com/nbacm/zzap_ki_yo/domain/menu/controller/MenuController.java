package com.nbacm.zzap_ki_yo.domain.menu.controller;

import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.service.MenuServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
@Slf4j
public class MenuController {

    private final MenuServiceImpl menuService;

    @PostMapping("/{storeId}")
    public ResponseEntity<MenuResponseDto> createMenu(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody MenuRequestDto menuRequestDto) {
         MenuResponseDto createMenu = menuService.createMenu(authUser.getEmail(),authUser.getRole(),storeId,menuRequestDto);
         log.info("menu:{}",createMenu);
         return ResponseEntity.ok(createMenu);
     }

     @PatchMapping("/update/{storeId}/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(@Auth AuthUser authUser,
                                                      @PathVariable Long menuId,
                                                      @PathVariable Long storeId,
                                                      @RequestBody MenuUpdateRequestDto menuUpdateRequestDto) {
        MenuResponseDto updateMenu = menuService.updateMenu(authUser.getEmail(),authUser.getRole(),menuId,storeId,menuUpdateRequestDto);
        return ResponseEntity.ok(updateMenu);
     }


}
