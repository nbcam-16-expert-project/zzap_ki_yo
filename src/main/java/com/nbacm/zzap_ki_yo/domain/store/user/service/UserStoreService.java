package com.nbacm.zzap_ki_yo.domain.store.user.service;

import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreNameRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;

import java.util.List;

public interface UserStoreService {

    SelectStoreResponseDto selectStore(Long storeId);

    List<SelectAllStoreResponseDto> selectAllStore(StoreNameRequestDto requestDto);
}

