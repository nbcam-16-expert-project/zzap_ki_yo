package com.nbacm.zzap_ki_yo.domain.search.controller;

import com.nbacm.zzap_ki_yo.domain.advice.ControllerAdvice;
import com.nbacm.zzap_ki_yo.domain.search.dto.PopularWordResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.SearchResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.StoreNameDto;
import com.nbacm.zzap_ki_yo.domain.search.service.SearchService;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.MenuNamePrice;
import com.nbacm.zzap_ki_yo.domain.user.common.config.AuthUserArgumentResolver;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtAuthenticationFilter;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private SearchController controller;

    @MockBean
    private SearchService searchService;

    @Mock
    private AuthUserArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerAdvice())
                .setCustomArgumentResolvers(resolver)
                .build();
    }

    @Test
    void 검색_테스트() throws Exception {
        StoreNameDto storeNameDto = StoreNameDto.of("asd");
        List<StoreNameDto> storeNameDtos = new ArrayList<>();
        storeNameDtos.add(storeNameDto);
        MenuNamePrice menuNamePrice = MenuNamePrice.builder()
                .price(12)
                .menuName("asd")
                .build();
        List<MenuNamePrice> menuNamePrices = new ArrayList<>();
        menuNamePrices.add(menuNamePrice);

        SearchResponseDto responseDto = SearchResponseDto.build(storeNameDtos, menuNamePrices);
        given(searchService.search(anyString(),any(Pageable.class))).willReturn(responseDto);

         mockMvc.perform(get("/api/v1/search")
                         .param("keyword","asd")
                         .param("page","0")
                         .param("size","10"))
                .andExpect(status().isOk());
    }


    @Test
    void 인기_검색어_테스트() throws Exception {
        PopularWordResponseDto responseDto = PopularWordResponseDto.builder()
                .word("asd")
                .build();
        given(searchService.getPopularWord()).willReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/popular-word")
        ).andExpect(status().isOk());
    }
}
