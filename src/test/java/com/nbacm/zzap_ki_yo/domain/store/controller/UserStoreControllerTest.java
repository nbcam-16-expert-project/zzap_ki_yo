package com.nbacm.zzap_ki_yo.domain.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.advice.ControllerAdvice;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.AdType;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.service.UserStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.config.AuthUserArgumentResolver;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtAuthenticationFilter;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserStoreController.class)
public class UserStoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserStoreController controller;

    @MockBean
    private UserStoreServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

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
    void 가게_조회_테스트() throws Exception {
        String storeName = "qwe";

        SelectStoreResponseDto responseDto = SelectStoreResponseDto.builder()
                .storeName(storeName)
                .storeAddress("asd")
                .storeId(1L)
                .favoriteCount(1)
                .storeNumber("asd")
                .build();

        given(service.selectStore(anyString())).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/users/stores/{storeName}", storeName)
        ).andExpect(status().isOk());
    }

    @Test
    void 모든_가게_조회_테스트() throws Exception {
        SelectAllStoreResponseDto responseDto = SelectAllStoreResponseDto.builder()
                .storeNumber("asd")
                .storeType(StoreType.OPENING)
                .storeId(1L)
                .favoriteCount(1)
                .storeName("asd")
                .storeAddress("Asd")
                .adType(AdType.AD)
                .build();

        given(service.selectAllStore()).willReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/users/stores")
        ).andExpect(status().isOk());
    }
}
