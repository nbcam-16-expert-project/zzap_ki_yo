package com.nbacm.zzap_ki_yo.domain.store.admin.service;

import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.UpdateStoreNameRequest;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;

import java.util.List;

public interface AdminStoreService {

    CreateStoreResponseDto createStore(CreateStoreRequestDto createStoreRequestDto);

    UpdateStoreResponseDto updateStore(Long storeId, UpdateStoreNameRequest request);

    DeleteStoreResponseDto deleteStore(Long storeId);

    SelectStoreResponseDto selectStore(Long storeId);

    List<SelectAllStoreResponseDto> selectAllStore(StoreNameRequestDto requestDto);

    ClosingStoreResponseDto closingStore(Long storeId);
}
