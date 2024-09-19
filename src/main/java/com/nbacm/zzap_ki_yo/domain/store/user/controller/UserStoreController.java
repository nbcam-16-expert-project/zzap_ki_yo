package com.nbacm.zzap_ki_yo.domain.store.user.controller;

import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.user.service.UserStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserStoreController {

    private final UserStoreServiceImpl userStoreServiceImpl;

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@PathVariable Long storeId) {
        SelectStoreResponseDto responseDto = userStoreServiceImpl.selectStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectAllStoreResponseDto>> selectAllStores(@RequestBody StoreNameRequestDto requestDto) {
        List<SelectAllStoreResponseDto> responseDtos = userStoreServiceImpl.selectAllStore(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}

