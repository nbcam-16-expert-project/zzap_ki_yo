package com.nbacm.zzap_ki_yo.domain.store.admin.controller;


import com.nbacm.zzap_ki_yo.domain.store.admin.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminStoreController {

    private final AdminStoreServiceImpl adminStoreServiceImpl;

    @PostMapping("/stores")
    public ResponseEntity<CreateStoreResponseDto> createStore(@Auth AuthUser authUser, @RequestBody CreateStoreRequestDto request) {
        CreateStoreResponseDto responseDto = adminStoreServiceImpl.createStore(authUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/stores/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStore(@Auth AuthUser authUser, @PathVariable Long storeId, @RequestBody UpdateStoreNameRequest request){
        UpdateStoreResponseDto responseDto = adminStoreServiceImpl.updateStore(authUser,storeId,request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<ClosingStoreResponseDto> closingStore(@Auth AuthUser authUser, @PathVariable Long storeId){
        ClosingStoreResponseDto responseDto = adminStoreServiceImpl.closingStore(authUser, storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<DeleteStoreResponseDto> deleteStore(@Auth AuthUser authUser, @PathVariable Long storeId) {
        DeleteStoreResponseDto responseDto = adminStoreServiceImpl.deleteStore(authUser,storeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseDto);
    }


    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@Auth AuthUser authUser, @PathVariable Long storeId){
        SelectStoreResponseDto responseDto = adminStoreServiceImpl.selectStore(authUser,storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectAllStoreResponseDto>> selectAllStores(@Auth AuthUser authUser, @RequestBody StoreNameRequestDto requestDto) {
        List<SelectAllStoreResponseDto> responseDtos = adminStoreServiceImpl.selectAllStore(authUser, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}