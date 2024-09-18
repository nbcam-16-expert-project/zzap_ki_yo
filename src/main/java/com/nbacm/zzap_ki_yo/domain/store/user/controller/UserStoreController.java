package com.nbacm.zzap_ki_yo.domain.store.user.controller;

import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.user.service.UserStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserStoreController {

    private final UserStoreService userStoreService;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@PathVariable Long storeId){
        SelectStoreResponseDto responseDto = userStoreService.selectStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectStoreResponseDto>> selectAllStores() {
        List<SelectStoreResponseDto> responseDtos = userStoreService.selectAllStore();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}
