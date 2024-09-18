package com.nbacm.zzap_ki_yo.domain.store.admin.controller;


import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.CreateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.DeleteStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.UpdateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.admin.service.AdminStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    @PostMapping("/stores")
    public ResponseEntity<CreateStoreResponseDto> createStore(@RequestBody CreateStoreRequestDto request) {
        CreateStoreResponseDto responseDto = adminStoreService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PutMapping("/stores/{storeId}")
    public ResponseEntity<UpdateStoreResponseDto> updateStore(@PathVariable Long storeId, @RequestBody UpdateStoreNameRequest request){
        UpdateStoreResponseDto responseDto = adminStoreService.updateStore(storeId,request);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<DeleteStoreResponseDto> deleteStore(@PathVariable Long storeId) {
        DeleteStoreResponseDto responseDto = adminStoreService.deleteStore(storeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseDto);
    }

    @GetMapping("/stores/{storeId}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@PathVariable Long storeId){
        SelectStoreResponseDto responseDto = adminStoreService.selectStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectStoreResponseDto>> selectAllStores() {
        List<SelectStoreResponseDto> responseDtos = adminStoreService.selectAllStore();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}