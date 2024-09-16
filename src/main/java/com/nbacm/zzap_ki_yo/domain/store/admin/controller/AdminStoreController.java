package com.nbacm.zzap_ki_yo.domain.store.admin.controller;


import com.nbacm.zzap_ki_yo.domain.store.admin.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.admin.dto.response.CreateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.admin.service.AdminStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    @PostMapping("/stores")
    public ResponseEntity<CreateStoreResponseDto> createStore(CreateStoreRequestDto request) {
        CreateStoreResponseDto responseDto = adminStoreService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}