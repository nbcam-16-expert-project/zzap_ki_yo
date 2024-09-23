package com.nbacm.zzap_ki_yo.domain.store.service;


import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.ClosingStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.dto.response.*;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreForbiddenException;
import com.nbacm.zzap_ki_yo.domain.store.exception.StoreNotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminStoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminStoreServiceImpl adminStoreServiceImpl;

    @Nested
    class CreateStoreTest {

        @Test
        void 가게_등록_번호_null오류_테스트(){

            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);

            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    "name",
                    "address",
                    null,
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }

        @Test
        void 가게_등록_이름_null오류_테스트(){

            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    null,
                    "address",
                    "number",
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );
            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }

        @Test
        void 가게_등록_주소_null오류_테스트(){

            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    "name",
                    null,
                    "number",
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );
            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }

        @Test
        void 가게_등록_갯수제한_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = User.builder()
                    .email(user.getEmail())
                    .userRole(user.getRole())
                    .build();
            List<Store> stores = new ArrayList<>();
            stores.add(new Store());
            stores.add(new Store());
            stores.add(new Store());
            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    "name",
                    "address",
                    "number",
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );

            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);
            given(storeRepository.findAllByUserAndStoreType(any(User.class), any(StoreType.class))).willReturn(stores);

            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
            );

            assertEquals("가게는 3개까지 운영 가능합니다.", exception.getMessage());
        }


        @Test
        void 가게_등록_테스트_정상(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = User.builder()
                    .email(user.getEmail())
                    .userRole(user.getRole())
                    .build();
            List<Store> stores = new ArrayList<>();
            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);
            given(storeRepository.findAllByUserAndStoreType(any(User.class), any(StoreType.class))).willReturn(stores);

            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    "name",
                    "address",
                    "number",
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );

            Store store = Store.builder()
                    .storeName(createStoreRequestDto.getStoreName())
                    .storeAddress(createStoreRequestDto.getStoreAddress())
                    .storeNumber(createStoreRequestDto.getStoreNumber())
                    .user(user1)
                    .build();
            given(storeRepository.save(any(Store.class))).willReturn(store);
            CreateStoreResponseDto createStoreResponseDto = adminStoreServiceImpl.createStore(user,createStoreRequestDto);

            assertNotNull(createStoreResponseDto);

        }

        @Test
        void 가게_등록_일반사용자_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.USER);
            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
                    "name",
                    "address",
                    "number",
                    10000,
                    LocalTime.of(9,0),
                    LocalTime.of(18,0)
            );

            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
                    );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }
    }

    @Nested
    class UpdateStoreTest{
        @Test
        void 가게_수정_가게_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            StoreRequestDto requestDto = new StoreRequestDto(
                    "storeName",
                    "aaa",
                    "aaa",
                    123345687,
                    LocalTime.of(1,0),
                    LocalTime.of(12,0)
            );

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                    adminStoreServiceImpl.updateStore(user,1L,requestDto)
                    );

            assertEquals("가게를 찾지 못했습니다.", exception.getMessage());
        }


        @Test
        void 가게_수정_폐업_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe","qwe","qwe",UserRole.OWNER,"asd","asd");
            StoreRequestDto requestDto = new StoreRequestDto(
                    "storeName",
                    "aaa",
                    "aaa",
                    123345687,
                    LocalTime.of(1,0),
                    LocalTime.of(12,0)
            );
            Store store = new Store(requestDto.getStoreName(),
                    "123","123",12,StoreType.CLOSING,user1,1,requestDto.getOpeningTime(),requestDto.getClosingTime()
                    ,new ArrayList<>(), new ArrayList<>()
            );

            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);
            given(storeRepository.findByStoreIdAndUser(anyLong(),any(User.class))).willReturn(store);

            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
                    adminStoreServiceImpl.updateStore(user,1L,requestDto)
            );

            assertEquals("페업한 가게는 수정을 할 수 없습니다.", exception.getMessage());
        }

        @Test
        void 가게_수정_유저_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            StoreRequestDto requestDto = new StoreRequestDto(
                    "storeName",
                    "aaa",
                    "aaa",
                    123345687,
                    LocalTime.of(1,0),
                    LocalTime.of(12,0)
            );

            given(userRepository.findByEmailOrElseThrow(anyString()))
                    .willThrow(new UserNotFoundException("유저를 찾을수 없습니다"));

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                adminStoreServiceImpl.updateStore(authUser, 1L, requestDto);
            });
            assertEquals("유저를 찾을수 없습니다", exception.getMessage());
        }


        @Test
        void 가게_수정_테스트_정상(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe","qwe","qwe",UserRole.OWNER,"asd","asd");
            StoreRequestDto requestDto = new StoreRequestDto(
                    "storeName",
                    "aaa",
                    "aaa",
                    123345687,
                    LocalTime.of(1,0),
                    LocalTime.of(12,0)
            );
            Store store = new Store(requestDto.getStoreName(),
                    "123","123",12,StoreType.OPENING,user1,1,requestDto.getOpeningTime(),requestDto.getClosingTime()
                    ,new ArrayList<>(), new ArrayList<>()
            );

            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);
            given(storeRepository.findByStoreIdAndUser(anyLong(),any(User.class))).willReturn(store);

            UpdateStoreResponseDto responseDto = adminStoreServiceImpl.updateStore(user,1L,requestDto);

            assertNotNull(responseDto);
            assertNotNull(responseDto.getStoreName());
            assertNotNull(responseDto.getStoreAddress());
            assertNotNull(responseDto.getStoreNumber());
            assertNotNull(responseDto.getOpeningTime());
            assertNotNull(responseDto.getClosingTime());
            assertNotNull(responseDto.getOrderMinPrice());
        }

        @Test
        void 가게_수정_일반_사용자_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.USER);

            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                    adminStoreServiceImpl.updateStore(user,1L,null)
            );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }
    }


    @Nested
    class DeleteStoreTest{

        @Test
        void 가게_삭제_테스트_정상(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe","qwe","qwe",UserRole.ADMIN,"asd","asd");
            Store store = new Store("qwe","qwe","qwe",1,StoreType.OPENING,user1,1,LocalTime.MIN,LocalTime.MAX,new ArrayList<>(), new ArrayList<>());
            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);
            given(storeRepository.findByStoreIdAndUser(anyLong(),any(User.class))).willReturn(store);

            adminStoreServiceImpl.deleteStore(user,1L);

            verify(storeRepository, times(1)).delete(store);
        }

        @Test
        void 가게_삭제_가게_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe","qwe","qwe",UserRole.OWNER,"asd","asd");

            given(userRepository.findByEmailOrElseThrow(anyString())).willReturn(user1);

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                    adminStoreServiceImpl.deleteStore(user,1L)
            );

            assertEquals("가게를 찾지 못했습니다.", exception.getMessage());
        }

        @Test
        void 가게_삭제_유저_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);

            given(userRepository.findByEmailOrElseThrow(anyString()))
                    .willThrow(new UserNotFoundException("유저를 찾을수 없습니다"));

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                adminStoreServiceImpl.deleteStore(authUser, 1L);
            });
            assertEquals("유저를 찾을수 없습니다", exception.getMessage());
        }

        @Test
        void 가게_삭제_일반_사용자_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.USER);

            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                    adminStoreServiceImpl.deleteStore(user,1L)
            );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }
    }

    @Nested
    class SelectStore{
        @Test
        void 가게_조회_가게_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            long storeId = 1L;

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                adminStoreServiceImpl.selectStore(authUser,storeId)
            );

            assertEquals("가게를 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        void 가게_조회_일반_사용자_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.USER);

            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                    adminStoreServiceImpl.selectStore(user,1L)
            );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }
        @Test
        void 가게_조회_폐업_오류_테스트(){
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe","qwe","qwe",UserRole.OWNER,"asd","asd");
            lenient().when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user1);

            Store store = new Store("qwe","qwe","qwe",12,StoreType.CLOSING,user1,1,LocalTime.MIN,LocalTime.MAX,new ArrayList<>(), new ArrayList<>());

            given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
                    adminStoreServiceImpl.selectStore(user,1L)
                    );

            assertEquals("폐업한 가게는 조회할 수 없습니다.", exception.getMessage());
        }

        @Test
        void 가게_조회_테스트_정상() {
            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("qwe", "qwe", "qwe", UserRole.OWNER, "asd", "asd");
            lenient().when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user1);

            List<Menu> menus = new ArrayList<>();
            Menu menu = Menu.builder()
                    .store(new Store())
                    .menuName("qw")
                    .price(12)
                    .build();
            menus.add(menu);

            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, user1, 1,
                    LocalTime.MIN, LocalTime.MAX, menus, new ArrayList<>());

            given(storeRepository.findById(anyLong())).willReturn(Optional.of(store));

            SelectStoreResponseDto responseDto = adminStoreServiceImpl.selectStore(user, 1L);

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
        void 모든가게_조회_일반_사용자_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.USER);

            UnauthorizedException exception = assertThrows(UnauthorizedException.class,() ->
                    adminStoreServiceImpl.selectAllStore(authUser)
                    );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }

        @Test
        void 모든가게_조회_가게_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);

            StoreNotFoundException exception = assertThrows(StoreNotFoundException.class, () ->
                    adminStoreServiceImpl.selectAllStore(authUser)
                    );

            assertEquals("가게를 찾지 못했습니다.", exception.getMessage());
        }

        @Test
        void 모든가게_조회_테스트_정상(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("asd","asd","asd",UserRole.OWNER,"asd","asd");
            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, user1, 1,
                    LocalTime.MIN, LocalTime.MAX, new ArrayList<>(), new ArrayList<>());

            given(storeRepository.findAllByStoreType(any(StoreType.class))).willReturn(List.of(store));

            List<SelectAllStoreResponseDto> responseDtos = adminStoreServiceImpl.selectAllStore(authUser);

            assertNotNull(responseDtos);
            assertNotNull(responseDtos.get(0).getStoreName());
            assertNotNull(responseDtos.get(0).getStoreAddress());
            assertNotNull(responseDtos.get(0).getStoreNumber());
            assertNotNull(responseDtos.get(0).getFavoriteCount());
            assertNotNull(responseDtos.get(0).getStoreType());
        }
    }


    @Nested
    class ClosingStoreTest{
        @Test
        void 가게_폐업_이미_폐업_오류_테스트(){
            ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("폐업합니다");

            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("asd","asd","asd",UserRole.OWNER,"asd","asd");
            long storeId = 1L;
            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.CLOSING, user1, 1,
                    LocalTime.MIN, LocalTime.MAX, new ArrayList<>(), new ArrayList<>());
            lenient().when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user1);

            given(storeRepository.findByStoreIdAndUser(anyLong(), any(User.class))).willReturn(store);


            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
                    adminStoreServiceImpl.closingStore(authUser,storeId,requestDto)
                    );

            assertEquals("이미 폐업 되었습니다.", exception.getMessage());

        }
        @Test
        void 가게_폐업_일반_사용자_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.USER);
            ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("폐업합니다");

            UnauthorizedException exception = assertThrows(UnauthorizedException.class,() ->
                    adminStoreServiceImpl.closingStore(authUser,1L,requestDto)
            );

            assertEquals("어드민 사용자만 이용할 수 있습니다.", exception.getMessage());
        }
        @Test
        void 가게_폐업_유저_오류_테스트(){
            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("폐업합니다");

            given(userRepository.findByEmailOrElseThrow(anyString()))
                    .willThrow(new UserNotFoundException("유저를 찾을수 없습니다"));

            UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
                adminStoreServiceImpl.closingStore(authUser,1L,requestDto);
            });
            assertEquals("유저를 찾을수 없습니다", exception.getMessage());
        }


        @Test
        void 가게_폐업_테스트_정상(){
            ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("폐업합니다");

            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("asd","asd","asd",UserRole.OWNER,"asd","asd");
            long storeId = 1L;
            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, user1, 1,
                    LocalTime.MIN, LocalTime.MAX, new ArrayList<>(), new ArrayList<>());
            lenient().when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user1);

            given(storeRepository.findByStoreIdAndUser(anyLong(), any(User.class))).willReturn(store);

            ClosingStoreResponseDto responseDto = adminStoreServiceImpl.closingStore(authUser,storeId,requestDto);

            assertNotNull(responseDto);
            assertNotNull(responseDto.getStoreName());
            assertEquals(StoreType.CLOSING, responseDto.getStoreType());
        }

        @Test
        void 가게_페업_테스트_실패(){
            ClosingStoreRequestDto requestDto = new ClosingStoreRequestDto("안 함");

            AuthUser authUser = new AuthUser("email@mail.com", UserRole.OWNER);
            User user1 = new User("asd","asd","asd",UserRole.OWNER,"asd","asd");
            long storeId = 1L;
            Store store = new Store("qwe", "qwe", "qwe", 12, StoreType.OPENING, user1, 1,
                    LocalTime.MIN, LocalTime.MAX, new ArrayList<>(), new ArrayList<>());
            lenient().when(userRepository.findByEmailOrElseThrow(anyString())).thenReturn(user1);

            given(storeRepository.findByStoreIdAndUser(anyLong(), any(User.class))).willReturn(store);

            ClosingStoreResponseDto responseDto = adminStoreServiceImpl.closingStore(authUser,storeId,requestDto);

            assertNotNull(responseDto);
            assertNotNull(responseDto.getStoreName());
            assertEquals(StoreType.OPENING, responseDto.getStoreType());
        }
    }



}
