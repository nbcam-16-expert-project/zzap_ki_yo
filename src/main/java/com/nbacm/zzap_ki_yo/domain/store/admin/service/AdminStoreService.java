package com.nbacm.zzap_ki_yo.domain.store.admin.service;

import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;

import java.util.List;


public interface AdminStoreService {

    CreateStoreResponseDto createStore(AuthUser authUser, CreateStoreRequestDto createStoreRequestDto);

    UpdateStoreResponseDto updateStore(AuthUser authUser, Long storeId, UpdateStoreNameRequest request);

    DeleteStoreResponseDto deleteStore(AuthUser authUser, Long storeId);

    SelectStoreResponseDto selectStore(AuthUser authUser, Long storeId);

    List<SelectAllStoreResponseDto> selectAllStore(AuthUser authUser, StoreNameRequestDto requestDto);

    ClosingStoreResponseDto closingStore(AuthUser authUser, Long storeId);
}
