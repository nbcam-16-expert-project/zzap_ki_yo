package com.nbacm.zzap_ki_yo.domain.menu;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nbacm.zzap_ki_yo.domain.menu.controller.MenuController;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuResponseDto;
import com.nbacm.zzap_ki_yo.domain.menu.dto.MenuUpdateRequestDto;
import com.nbacm.zzap_ki_yo.domain.menu.entity.MenuStatus;
import com.nbacm.zzap_ki_yo.domain.menu.service.MenuServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.common.Auth;
import com.nbacm.zzap_ki_yo.domain.user.common.util.JwtUtils;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.mockito.BDDMockito.given;

@WebMvcTest(MenuController.class)
public class MenuControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @MockBean
    private MenuServiceImpl menuService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        // Custom Argument Resolver 추가
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuController(menuService))
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType().equals(AuthUser.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return new AuthUser("owner@test.com", UserRole.OWNER);
                    }
                })
                .build();
    }

    @Test
    void createMenu_성공() throws Exception {
        // given
        Long storeId = 1L;
        MenuRequestDto menuRequestDto = new MenuRequestDto("Pizza", 10000);
        MenuResponseDto menuResponseDto = new MenuResponseDto("Pizza", 10000, MenuStatus.AVAILABLE);

        // MenuService의 createMenu() 메서드 모킹
        given(menuService.createMenu(anyString(), any(UserRole.class), eq(storeId), any(MenuRequestDto.class)))
                .willReturn(menuResponseDto);

        // when & then
        mockMvc.perform(post("/api/v1/menus/{storeId}", storeId)
                        .header("Authorization", "Bearer test-token") // 필요 시 Authorization 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menuName").value("Pizza"))
                .andExpect(jsonPath("$.price").value(10000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andDo(print());
    }

    @Test
    void updateMenu_성공() throws Exception {
        // given
        Long storeId = 1L;
        Long menuId = 1L;
        MenuUpdateRequestDto menuUpdateRequestDto = new MenuUpdateRequestDto("Burger", 15000, MenuStatus.AVAILABLE);
        MenuResponseDto menuResponseDto = new MenuResponseDto("Burger", 15000, MenuStatus.AVAILABLE);

        // MenuService의 updateMenu() 메서드 모킹
        given(menuService.updateMenu(anyString(), any(UserRole.class), eq(menuId), eq(storeId), any(MenuUpdateRequestDto.class)))
                .willReturn(menuResponseDto);

        // when & then
        mockMvc.perform(patch("/api/v1/menus/update/{storeId}/{menuId}", storeId, menuId)
                        .header("Authorization", "Bearer test-token") // 필요 시 Authorization 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.menuName").value("Burger"))
                .andExpect(jsonPath("$.price").value(15000))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andDo(print());
    }
}
