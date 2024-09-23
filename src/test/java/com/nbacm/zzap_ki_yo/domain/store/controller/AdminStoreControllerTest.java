package com.nbacm.zzap_ki_yo.domain.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.advice.ControllerAdvice;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.ClosingStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.config.AuthUserArgumentResolver;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtAuthenticationFilter;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminStoreController.class)
public class AdminStoreControllerTest {

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
        StoreRequestDto requestDto = new StoreRequestDto("qwe","qwe","qwe",1, LocalTime.MIN,LocalTime.MAX);
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
    void 가게_수정_태스트() throws Exception {
        StoreRequestDto requestDto = new StoreRequestDto("qwe","qwe","qwe",1, LocalTime.MIN,LocalTime.MAX);
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
        ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("페업합니다");
        ClosingStoreResponseDto responseDto = new ClosingStoreResponseDto("asd",StoreType.CLOSING);

        given(adminStoreService.closingStore(any(AuthUser.class),anyLong(),any(ClosingStoreRequestDto.class)))
                .willReturn(responseDto);

        mockMvc.perform(patch("/api/v1/admin/stores/{storeId}", 1L)
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

    @Test
    void 가게_조회_테스트() throws Exception {
        SelectStoreResponseDto responseDto = SelectStoreResponseDto.builder()
                .menus(new ArrayList<>())
                .storeId(1L)
                .storeNumber("Asd")
                .favoriteCount(1)
                .storeAddress("aasd")
                .storeName("Asd")
                .build();

        given(adminStoreService.selectStore(any(),anyLong())).willReturn(responseDto);

        mockMvc.perform(get("/api/v1/admin/stores/{storeId}", 1L)
                ).andExpect(status().isOk());
    }

    @Test
    void 모든_가게_조회_테스트() throws Exception {
        SelectAllStoreResponseDto responseDto = SelectAllStoreResponseDto.builder()
                .storeType(StoreType.OPENING)
                .storeId(1L)
                .storeAddress("asd")
                .storeName("asd")
                .favoriteCount(1)
                .storeNumber("asd")
                .build();

        given(adminStoreService.selectAllStore(any())).willReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/admin/stores")
        ).andExpect(status().isOk());
    }


}
