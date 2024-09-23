package com.nbacm.zzap_ki_yo.domain.store.service;

import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.ClosingStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;

import java.util.List;


public interface AdminStoreService {

    CreateStoreResponseDto createStore(AuthUser authUser, StoreRequestDto storeRequestDto);

    UpdateStoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreRequestDto request);

    void deleteStore(AuthUser authUser, Long storeId);

    SelectStoreResponseDto selectStore(AuthUser authUser, Long storeId);

    List<SelectAllStoreResponseDto> selectAllStore(AuthUser authUser);

    ClosingStoreResponseDto closingStore(AuthUser authUser, Long storeId, ClosingStoreRequestDto requestDto);

    StatisticsResponseDto getDailyStatistics(Long storeId, String email);

    StatisticsResponseDto getMonthlyStatistics(Long storeId, String email);

    StatisticsResponseDto getDailyAllStatistics(String email);

    StatisticsResponseDto getMonthlyAllStatistics(String email);
}
