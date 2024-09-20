package com.nbacm.zzap_ki_yo.domain.store.controller;


import com.nbacm.zzap_ki_yo.domain.store.dto.request.ClosingStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminStoreController {

    private final AdminStoreServiceImpl adminStoreServiceImpl;

    @PostMapping("/stores")
    public ResponseEntity<CreateStoreResponseDto> createStore(@Auth AuthUser authUser, @RequestBody StoreRequestDto request) {
        CreateStoreResponseDto responseDto = adminStoreServiceImpl.createStore(authUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/stores/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStore(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody StoreRequestDto request){
        UpdateStoreResponseDto responseDto = adminStoreServiceImpl.updateStore(authUser,storeId,request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // DB반영은 되는데 출력이 안 됨 뭐가 문제인지 모르겠음 ////////////////////// 밥 먹고 와서 물어보기
    // (해결 !!!!! dto에 @Data 안 달아줘서 그랬음)
    @PatchMapping(value = "/stores/{storeId}")
    public ResponseEntity<ClosingStoreResponseDto> closingStore(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody ClosingStoreRequestDto requestDto){
        ClosingStoreResponseDto responseDto = adminStoreServiceImpl.closingStore(authUser, storeId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<Void> deleteStore(@Auth AuthUser authUser, @PathVariable Long storeId) {
        adminStoreServiceImpl.deleteStore(authUser,storeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@Auth AuthUser authUser, @PathVariable Long storeId){
        SelectStoreResponseDto responseDto = adminStoreServiceImpl.selectStore(authUser,storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectAllStoreResponseDto>> selectAllStores(@Auth AuthUser authUser) {
        List<SelectAllStoreResponseDto> responseDtos = adminStoreServiceImpl.selectAllStore(authUser);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}