package com.nbacm.zzap_ki_yo.store;


import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.store.admin.service.AdminStoreServiceImpl;
import com.nbacm.zzap_ki_yo.domain.store.dto.request.CreateStoreRequestDto;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import com.nbacm.zzap_ki_yo.domain.user.User;
import com.nbacm.zzap_ki_yo.domain.user.UserRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AdminStoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private AdminStoreServiceImpl adminStoreServiceImpl;


    @Nested
    class CreateStoreTest {
        @Test
        void 가게_등록_번호_null오류_테스트(){
            LocalTime openingTime = LocalTime.of(9, 0);
            LocalTime closingTime = LocalTime.of(18, 0);


            CreateStoreRequestDto requestDto = new CreateStoreRequestDto(
                    "storeName",
                    "storeAddress",
                    null,
                    openingTime,
                    closingTime
            );

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(requestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }

        @Test
        void 가게_등록_이름_null오류_테스트(){
            LocalTime openingTime = LocalTime.of(9, 0);
            LocalTime closingTime = LocalTime.of(18, 0);


            CreateStoreRequestDto requestDto = new CreateStoreRequestDto(
                    null,
                    "storeAddress",
                    "storeNumber",
                    openingTime,
                    closingTime
            );

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(requestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }

        @Test
        void 가게_등록_주소_null오류_테스트(){
            LocalTime openingTime = LocalTime.of(9, 0);
            LocalTime closingTime = LocalTime.of(18, 0);


            CreateStoreRequestDto requestDto = new CreateStoreRequestDto(
                    "storeName",
                    null,
                    "storeNumber",
                    openingTime,
                    closingTime
            );

            BadRequestException exception = assertThrows(BadRequestException.class, () ->
                    adminStoreServiceImpl.createStore(requestDto)
            );

            assertEquals("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.", exception.getMessage());
        }
    }

}
