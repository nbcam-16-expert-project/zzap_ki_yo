package com.nbacm.zzap_ki_yo.domain.store.controller;

import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.service.UserStoreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserStoreController {

    private final UserStoreServiceImpl userStoreServiceImpl;

    @GetMapping("/stores/{storeName}")
    public ResponseEntity<SelectStoreResponseDto> selectStore(@PathVariable String storeName) {
        SelectStoreResponseDto responseDto = userStoreServiceImpl.selectStore(storeName);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    @GetMapping("/stores")
    public ResponseEntity<List<SelectAllStoreResponseDto>> selectAllStores() {
        List<SelectAllStoreResponseDto> responseDtos = userStoreServiceImpl.selectAllStore();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }
}