package com.nbacm.zzap_ki_yo.domain.store.admin.controller;


import com.nbacm.zzap_ki_yo.domain.store.admin.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
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
    public ResponseEntity<CreateStoreResponseDto> createStore(@RequestBody CreateStoreRequestDto request) {
        CreateStoreResponseDto responseDto = adminStoreServiceImpl.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/stores/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStore(@PathVariable Long storeId, @RequestBody UpdateStoreNameRequest request){
        UpdateStoreResponseDto responseDto = adminStoreServiceImpl.updateStore(storeId,request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PatchMapping("/stores/{storeId}")
    public ResponseEntity<ClosingStoreResponseDto> closingStore(@PathVariable Long storeId){
        ClosingStoreResponseDto responseDto = adminStoreServiceImpl.closingStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<DeleteStoreResponseDto> deleteStore(@PathVariable Long storeId) {
        DeleteStoreResponseDto responseDto = adminStoreServiceImpl.deleteStore(storeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseDto);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@PathVariable Long storeId){
        SelectStoreResponseDto responseDto = adminStoreServiceImpl.selectStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectAllStoreResponseDto>> selectAllStores(@RequestBody StoreNameRequestDto requestDto) {
        List<SelectAllStoreResponseDto> responseDtos = adminStoreServiceImpl.selectAllStore(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}