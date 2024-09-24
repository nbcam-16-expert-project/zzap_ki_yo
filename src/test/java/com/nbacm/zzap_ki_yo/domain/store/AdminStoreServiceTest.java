//package com.nbacm.zzap_ki_yo.store;
//
//
//import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
//import com.nbacm.zzap_ki_yo.domain.store.service.AdminStoreServiceImpl;
//import com.nbacm.zzap_ki_yo.domain.store.dto.request.StoreRequestDto;
//import com.nbacm.zzap_ki_yo.domain.store.dto.response.CreateStoreResponseDto;
//import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
//import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
//import com.nbacm.zzap_ki_yo.domain.store.exception.StoreForbiddenException;
//import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
//import com.nbacm.zzap_ki_yo.domain.user.entity.User;
//import com.nbacm.zzap_ki_yo.domain.user.entity.UserRole;
//import com.nbacm.zzap_ki_yo.domain.user.dto.AuthUser;
//import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//
//@ExtendWith(MockitoExtension.class)
//public class AdminStoreServiceTest {
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private AdminStoreServiceImpl adminStoreServiceImpl;
//
//
//
//    @Nested
//    class CreateStoreTest {
//
//        @Test
//        void 가게_등록_번호_null오류_테스트(){
//
//            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
//
//            StoreRequestDto createStoreRequestDto = new StoreRequestDto(
//                    "name",
//                    "address",
//                    null,
//
//            );
//
//            BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
//            );
//
//            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
//        }
//
//        @Test
//        void 가게_등록_이름_null오류_테스트(){
//
//            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
//            BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
//            );
//
//            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
//        }
//
//        @Test
//        void 가게_등록_주소_null오류_테스트(){
//
//            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
//            BadRequestException exception = assertThrows(BadRequestException.class, () ->
//                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
//            );
//
//            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
//        }
//
//        @Test
//        void 가게_등록_갯수제한_테스트(){
//            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
//            User user1 = User.builder()
//                    .email(user.getEmail())
//                    .userRole(user.getRole())
//                    .build();
//            List<Store> stores = new ArrayList<>();
//            stores.add(new Store());
//            stores.add(new Store());
//            stores.add(new Store());
//
//            given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user1));
//            given(storeRepository.findAllByUserAndStoreType(any(User.class), any(StoreType.class))).willReturn(stores);
//
//            StoreForbiddenException exception = assertThrows(StoreForbiddenException.class, () ->
//                    adminStoreServiceImpl.createStore(user,createStoreRequestDto)
//            );
//
//            assertEquals("가게는 3개까지 운영 가능합니다.", exception.getMessage());
//        }
//
//
//        @Test
//        void 가게_등록_테스트_정상(){
//            AuthUser user = new AuthUser("email@mail.com", UserRole.OWNER);
//            User user1 = User.builder()
//                    .email(user.getEmail())
//                    .userRole(user.getRole())
//                    .build();
//            List<Store> stores = new ArrayList<>();
//            given(userRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(user1));
//            given(storeRepository.findAllByUserAndStoreType(any(User.class), any(StoreType.class))).willReturn(stores);
//
//
//            Store store = Store.builder()
//                    .storeName("name")
//                    .storeAddress("address")
//                    .storeNumber("number")
//                    .user(user1)
//                    .build();
//            given(storeRepository.save(any(Store.class))).willReturn(store);
//            CreateStoreResponseDto createStoreResponseDto = adminStoreServiceImpl.createStore(user,createStoreRequestDto);
//
//            assertNotNull(createStoreResponseDto);
//
//        }
//    }
//
//}
