package com.nbacm.zzap_ki_yo.domain.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.advice.ControllerAdvice;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.ClosingStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.ClosingStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.CreateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.UpdateStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.AdType;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.config.AuthUserArgumentResolver;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtAuthenticationFilter;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminStoreController.class)
public class AdminStoreControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AdminStoreControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AdminStoreController controller;

    @MockBean
    private AdminStoreServiceImpl adminStoreService;

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
    void 가게_저장_테스트() throws Exception {
        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.testData("ads","asd","asd",1,LocalTime.MIN,LocalTime.MAX,AdType.AD);
        CreateStoreResponseDto responseDto = CreateStoreResponseDto.builder()
                .storeId(1L)
                .orderMinPrice(1)
                .storeAddress("asd")
                .storeName("asd")
                .storeNumber("asd")
                .storeType(StoreType.OPENING)
                .build();


        given(adminStoreService.createStore(any(),any(StoreRequestDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/api/v1/admin/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(requestDto)))
                .andExpect(status().isCreated());
    }


    @Test
    void 가게_수정_테스트() throws Exception {
        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.testData("ads","asd","asd",1,LocalTime.MIN,LocalTime.MAX,AdType.AD);
        UpdateStoreResponseDto responseDto = UpdateStoreResponseDto.builder()
                .storeNumber("asd")
                .closingTime(LocalTime.MAX)
                .openingTime(LocalTime.MIN)
                .orderMinPrice(1)
                .storeAddress("asd")
                .storeName("asd")
                .build();

        given(adminStoreService.updateStore(any(),anyLong(),any(StoreRequestDto.class)))
                .willReturn(responseDto);

        mockMvc.perform(put("/api/v1/admin/stores/{storeId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDto))
        ).andExpect(status().isOk());
    }

    @Test
    void 가게_폐업_테스트() throws Exception {
        ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto();
        requestDto.testData("폐업합니다");
        ClosingStoreResponseDto responseDto = ClosingStoreResponseDto.builder()
                .storeType(StoreType.CLOSING)
                .storeName("asd").build();
        long storeId = 1L;

        when(adminStoreService.closingStore(any(AuthUser.class), any(Long.class), any(ClosingStoreRequestDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/admin/stores/{storeId}", storeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void 가게_삭제_테스트() throws Exception {
        willDoNothing().given(adminStoreService).deleteStore(any(),anyLong());

        mockMvc.perform(delete("/api/v1/admin/stores/{storeId}", 1L)).
                andExpect(status().isNoContent());
    }


}
