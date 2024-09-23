package com.nbacm.zzap_ki_yo.domain.store;

import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectAllStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.SelectStoreResponseDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreForbiddenException;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.store.service.UserStoreService;
import com.nbacm.zzap_ki_yo.domain.store.service.UserStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserStoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private UserStoreServiceImpl userStoreService;

    @Nested
    class SelectStore{
        @Test
        void 가게_조회_가게_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            long storeId = 1L;

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                    userStoreService.selectStore(storeId)
            );

            assertEquals("가게를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        void 가게_조회_폐업_오류_테스트(){

            Store store = new Store("qwe","qwe","qwe",12, StoreType.CLOSING,new User(),1, LocalTime.MIN,LocalTime.MAX,new ArrayList<>(), new ArrayList<>());

            given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
                    userStoreService.selectStore(1L)
            );

            assertEquals("폐업한 가게는 조회할 수 없습니다.", exception.getMessage());
        }

        @Test
        void 가게_조회_테스트_정상() {

            List<Menu> menus = new ArrayList<>();
            Menu menu = Menu.builder()
                    .store(new Store())
                    .menuName("qw")
                    .price(12)
                    .build();
            menus.add(menu);

            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, new User(), 1,
                    LocalTime.MIN, LocalTime.MAX, menus, new ArrayList<>());

            given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

            SelectStoreResponseDto responseDto = userStoreService.selectStore(1L);

            assertNotNull(responseDto);
            assertNotNull(responseDto.getStoreName());
            assertNotNull(responseDto.getStoreAddress());
            assertNotNull(responseDto.getStoreNumber());
            assertNotNull(responseDto.getFavoriteCount());
            assertNotNull(responseDto.getMenus().get(0).getMenuName());
            assertNotNull(responseDto.getMenus().get(0).getPrice());
        }
    }


    @Nested
    class SelectAllStore{

        @Test
        void 모든가게_조회_가게_오류_테스트(){

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                    userStoreService.selectAllStore()
            );

            assertEquals("가게를 찾지 못했습니다.", exception.getMessage());
        }

        @Test
        void 모든가게_조회_테스트_정상(){
            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, new User(), 1,
                    LocalTime.MIN, LocalTime.MAX, new ArrayList<>(), new ArrayList<>());

            given(storeRepository.findAllByStoreType(any(StoreType.class))).willReturn(List.of(store));

            List<SelectAllStoreResponseDto> responseDtos = userStoreService.selectAllStore();

            assertNotNull(responseDtos);
            assertNotNull(responseDtos.get(0).getStoreName());
            assertNotNull(responseDtos.get(0).getStoreAddress());
            assertNotNull(responseDtos.get(0).getStoreNumber());
            assertNotNull(responseDtos.get(0).getFavoriteCount());
            assertNotNull(responseDtos.get(0).getStoreType());
        }
    }

}
