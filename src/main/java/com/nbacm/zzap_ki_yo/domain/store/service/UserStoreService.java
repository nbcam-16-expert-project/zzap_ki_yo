package com.nbacm.zzap_ki_yo.domain.store.service;

import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;

import java.util.List;

public interface UserStoreService {

    SelectStoreResponseDto selectStore(Long storeId);

    List<SelectAllStoreResponseDto> selectAllStore();


}

