package com.nbacm.zzap_ki_yo.domain.search.service;


import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.search.dto.PopularWordResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.SearchResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.StoreNameDto;
import com.nbacm.zzap_ki_yo.domain.search.entity.PopularWord;
import com.nbacm.zzap_ki_yo.domain.search.repository.PopularWordRepository;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.MenuNamePrice;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SearchService {

    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final PopularWordRepository popularWordRepository;

    @Transactional
    public SearchResponseDto search(String keyword) {

        PopularWord popularWord = popularWordRepository.findByWord(keyword);

        if(popularWord == null) {
            PopularWord word = PopularWord.create(keyword, 1L);
            popularWordRepository.save(word);
        }else {
            popularWord.countUp(1L);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> stores = storeRepository.findByStoreNameContainingAndStoreType(keyword,StoreType.OPENING, pageable);

        List<StoreNameDto> storeNameDtos = stores.stream().map(store ->
                StoreNameDto.of(store.getStoreName())).toList();

        Page<Menu> menus = menuRepository.findByMenuNameContaining(keyword,pageable);
        List<MenuNamePrice> menuNamePrices = menus.stream().map(menu ->
                MenuNamePrice.builder().menuName(menu.getMenuName()).price(menu.getPrice()).build()).toList();

        if(storeNameDtos.isEmpty()){
            storeNameDtos = new ArrayList<>();
        }

        if(menuNamePrices.isEmpty()){
            menuNamePrices = new ArrayList<>();
        }

        if(storeNameDtos.isEmpty() && menuNamePrices.isEmpty()) {
            throw new NotFoundException("검색 결과가 없습니다.");
        }

        return SearchResponseDto.build(storeNameDtos,menuNamePrices);
    }


    public List<PopularWordResponseDto> getPopularWord() {
        List<PopularWord> popularWord = popularWordRepository.findTop10ByOrderByPopularityDesc();

        if(popularWord.isEmpty()) {
            throw new NotFoundException("인기 검색어가 없습니다.");
        }

        List<PopularWordResponseDto> responseDtos = new ArrayList<>();

        for (PopularWord word : popularWord) {
            PopularWordResponseDto responseDto = PopularWordResponseDto.of(word.getWord());

            responseDtos.add(responseDto);
        }

        return responseDtos;
    }
}
