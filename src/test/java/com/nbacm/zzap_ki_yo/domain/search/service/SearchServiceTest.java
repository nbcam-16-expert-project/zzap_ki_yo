package com.nbacm.zzap_ki_yo.domain.search.service;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.search.dto.PopularWordResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.SearchResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.entity.PopularWord;
import com.nbacm.zzap_ki_yo.domain.search.exception.SearchNotFoundException;
import com.nbacm.zzap_ki_yo.domain.search.exception.SearchWordBadRequestException;
import com.nbacm.zzap_ki_yo.domain.search.repository.PopularWordRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private PopularWordRepository popularWordRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private SearchService searchService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        int page = 1;
        int size = 10;
        pageable = PageRequest.of(page,size);
    }

    @Nested
    class SearchTest{
        @Test
        void 검색_공백_오류_테스트() {
            String word = " ";

            SearchWordBadRequestException exception = assertThrows(SearchWordBadRequestException.class, () ->
                    searchService.search(word, pageable)
            );

            assertEquals("공백은 검색 할 수 없습니다.",exception.getMessage());
        }

        @Test
        void 검색_결과_오류_테스트(){
            String word = "asd";
            PopularWord popularWord = PopularWord.builder()
                            .word("asd")
                            .popularity(1L).build();
            given(popularWordRepository.findByWord(anyString())).willReturn(popularWord);

            given(storeRepository.findByStoreNameContainingAndStoreType(any(),any(StoreType.class),any(Pageable.class)))
                    .willReturn(Page.empty());
            given(menuRepository.findByMenuNameContaining(anyString(),any(Pageable.class)))
                    .willReturn(Page.empty());

            SearchNotFoundException exception = assertThrows(SearchNotFoundException.class,() ->
                    searchService.search(word,pageable)
                    );

            assertEquals("검색 결과가 없습니다.",exception.getMessage());
        }

        @Test
        void 검색_가게_리스트_없고_메뉴_리스트_있는_테스트_정상(){
            String word = "asd";
            PopularWord popularWord = PopularWord.builder()
                    .word("asd")
                    .popularity(1L).build();
            given(popularWordRepository.findByWord(anyString())).willReturn(popularWord);

            given(storeRepository.findByStoreNameContainingAndStoreType(any(),any(StoreType.class),any(Pageable.class)))
                    .willReturn(Page.empty());
            Menu menu = new Menu();
            Page<Menu> menuPage = new PageImpl<>(List.of(menu));
            given(menuRepository.findByMenuNameContaining(anyString(),any(Pageable.class)))
                    .willReturn(menuPage);

            SearchResponseDto responseDto = searchService.search(word,pageable);
            assertNotNull(responseDto);
            assertNotNull(responseDto.getStores());
            assertNotNull(responseDto.getMenuNamePrices());
        }
        @Test
        void 검색_메뉴_리스트_없고_가게_리스트_있는_테스트_정상(){
            String word = "asd";
            PopularWord popularWord = PopularWord.builder()
                    .word("asd")
                    .popularity(1L).build();
            given(popularWordRepository.findByWord(anyString())).willReturn(popularWord);

            Store store = new Store();
            Page<Store> storePage = new PageImpl<>(List.of(store));
            given(storeRepository.findByStoreNameContainingAndStoreType(any(),any(StoreType.class),any(Pageable.class)))
                    .willReturn(storePage);
            given(menuRepository.findByMenuNameContaining(anyString(),any(Pageable.class)))
                    .willReturn(Page.empty());

            SearchResponseDto responseDto = searchService.search(word,pageable);
            assertNotNull(responseDto);
            assertNotNull(responseDto.getStores());
            assertNotNull(responseDto.getMenuNamePrices());
        }
        @Test
        public void 검색_결과가_없을_때_새로운_PopularWord_생성_저장() {
            String word = "asd";
            given(popularWordRepository.findByWord(anyString())).willReturn(null);

            Store store = new Store();
            Page<Store> storePage = new PageImpl<>(List.of(store));
            given(storeRepository.findByStoreNameContainingAndStoreType(any(),any(StoreType.class),any(Pageable.class)))
                    .willReturn(storePage);
            Menu menu = new Menu();
            Page<Menu> menuPage = new PageImpl<>(List.of(menu));
            given(menuRepository.findByMenuNameContaining(anyString(),any(Pageable.class)))
                    .willReturn(menuPage);

            SearchResponseDto responseDto = searchService.search(word,pageable);
            verify(popularWordRepository, times(1)).save(any(PopularWord.class));
            assertNotNull(responseDto);
            assertNotNull(responseDto.getStores());
            assertNotNull(responseDto.getMenuNamePrices());

        }

    }

    @Nested
    class GetPopularWordTest{
        @Test
        void 인기_검색어_조회_검색어_오류_테스트(){
            List<PopularWord> popularWords = new ArrayList<>();
            given(popularWordRepository.findTop10ByOrderByPopularityDesc())
                    .willReturn(popularWords);

            SearchNotFoundException exception = assertThrows(SearchNotFoundException.class, () ->
                    searchService.getPopularWord()
                    );
            assertEquals("인기 검색어가 없습니다.",exception.getMessage());
        }

        @Test
        void 인기_검색어_조회_테스트_정상(){
            PopularWord popularWord = PopularWord.builder()
                    .word("Asd")
                    .popularity(1L).build();

            given(popularWordRepository.findTop10ByOrderByPopularityDesc()).willReturn(List.of(popularWord));

            List<PopularWordResponseDto> responseDtos = searchService.getPopularWord();

            assertNotNull(responseDtos);
            assertNotNull(responseDtos.get(0).getWord());
        }
    }

  
}