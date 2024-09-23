package com.nbacm.zzap_ki_yo.domain.store.service;


import com.nbacm.zzap_ki_yo.domain.dashboard.dto.StoreStatisticsResponseDto;
import com.nbacm.zzap_ki_yo.domain.exception.BadRequestException;
import com.nbacm.zzap_ki_yo.domain.exception.UnauthorizedException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.order.service.OrderServiceImpl;
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
import com.nbacm.zzap_ki_yo.domain.user.exception.InvalidRoleException;
import com.nbacm.zzap_ki_yo.domain.user.exception.UserNotFoundException;
import com.nbacm.zzap_ki_yo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminStoreServiceImpl implements AdminStoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final OrderServiceImpl orderService;

    @Transactional
    @Override
    public CreateStoreResponseDto createStore(AuthUser authUser, StoreRequestDto storeRequestDto) {
        roleAdminCheck(authUser);

        if(storeRequestDto.getStoreAddress() == null
                || storeRequestDto.getStoreNumber() == null
                || storeRequestDto.getStoreName() == null){
            throw new BadRequestException("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.");
        }

        User user = userRepository.findByEmailOrElseThrow(authUser.getEmail());

        Store store = Store.createStore(storeRequestDto, user);

        List<Store> stores = storeRepository.findAllByUserAndStoreType(store.getUser(), StoreType.OPENING);

        if(stores.size() >= 3){
            throw new StoreForbiddenException("가게는 3개까지 운영 가능합니다.");
        }

        store = storeRepository.save(store);

        return CreateStoreResponseDto.createStore(store);
    }

    @Override
    @Transactional
    public UpdateStoreResponseDto updateStore(AuthUser authUser, Long storeId, StoreRequestDto request) {

        roleAdminCheck(authUser);
        User user = userRepository.findByEmailOrElseThrow(authUser.getEmail());

        Store store = findByStoreIdAndUser(storeId, user);

        if(store.getStoreType().equals(StoreType.CLOSING)){
            throw new StoreForbiddenException("페업한 가게는 수정을 할 수 없습니다.");
        }

        store.updateStore(request);

        return UpdateStoreResponseDto.updateStoreName(store);
    }


    @Transactional
    @Override
    public void deleteStore(AuthUser authUser, Long storeId) {
        roleAdminCheck(authUser);
        User user = userRepository.findByEmailOrElseThrow(authUser.getEmail());

        Store store = findByStoreIdAndUser(storeId, user);

        storeRepository.delete(store);
    }


    @Override
    public SelectStoreResponseDto selectStore(AuthUser authUser, Long storeId) {
        roleAdminCheck(authUser);

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if(store.getStoreType().equals(StoreType.CLOSING)){
            throw new StoreForbiddenException("폐업한 가게는 조회할 수 없습니다.");
        }

        List<Menu> menus = store.getMenus();

        List<MenuNamePrice> menuNamePrices = new ArrayList<>();
        for (Menu menu : menus) {
            MenuNamePrice menuNamePrice = MenuNamePrice.builder()
                    .menuName(menu.getMenuName())
                    .price(menu.getPrice())
                    .build();

            menuNamePrices.add(menuNamePrice);
        }

        return SelectStoreResponseDto.selectStore(store, menuNamePrices);
    }

    @Override
    public List<SelectAllStoreResponseDto> selectAllStore(AuthUser authUser) {
        roleAdminCheck(authUser);
        List<Store> storeList = storeRepository.findAllByStoreType(StoreType.OPENING).stream().toList();
        if(storeList.isEmpty()){
            throw new StoreNotFoundException("가게를 찾지 못했습니다.");
        }
        List<SelectAllStoreResponseDto> selectAllStoreResponseDtos = new ArrayList<>();
        for (Store store : storeList) {
            SelectAllStoreResponseDto responseDto = SelectAllStoreResponseDto.selectAllStore(store);

            selectAllStoreResponseDtos.add(responseDto);
        }

        return selectAllStoreResponseDtos;
    }

    @Transactional
    @Override
    public ClosingStoreResponseDto closingStore(AuthUser authUser, Long storeId, ClosingStoreRequestDto requestDto) {
        roleAdminCheck(authUser);
        User user = userRepository.findByEmailOrElseThrow(authUser.getEmail());

        Store store = findByStoreIdAndUser(storeId,user);
        if(requestDto.getMessage().equals("폐업합니다")) {
            store.closingStore(StoreType.CLOSING);
        }
        return ClosingStoreResponseDto.builder()
                .storeName(store.getStoreName())
                .storeType(store.getStoreType())
                .build();
    }


    private void roleAdminCheck(AuthUser authUser){
        UserRole role = authUser.getRole();
        if(role.equals(UserRole.USER)){
           throw new UnauthorizedException("어드민 사용자만 이용할 수 있습니다.");
        }
    }

    private Store findByStoreIdAndUser(Long storeId, User user){
        Store store = storeRepository.findByStoreIdAndUser(storeId, user);
        if(store == null){
            throw new StoreNotFoundException("가게를 찾지 못했습니다.");
        }
        return store;
    }

    @Override
    @Transactional(readOnly = false) // 수정 작업이 필요하다면 readOnly를 false로 설정
    public StoreStatisticsResponseDto getDailyStatistics(Long storeId, String email) {
        try {
            // 사장님 또는 관리자 권한 확인
            validateOwnerOrAdmin(email, storeId);
            return orderService.getDailyStatistics(storeId, LocalDate.now());
        } catch (Exception e) {
            // Log the error and return a more specific response
            log.error("Error fetching daily statistics for storeId: " + storeId, e);
            throw e;
        }

    }

    @Override
    @Transactional(readOnly = false) // 수정 작업이 필요하다면 readOnly를 false로 설정
    public StoreStatisticsResponseDto getMonthlyStatistics(Long storeId, String email) {
        try {
            // 사장님 또는 관리자 권한 확인
            validateOwnerOrAdmin(email, storeId);

            // 월간 통계 조회: 현재 월의 시작일과 끝일을 설정
            LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
            LocalDate endOfMonth = LocalDate.now();

            // 월간 통계 조회
            return orderService.getMonthlyStatistics(storeId, startOfMonth, endOfMonth);
        }catch (Exception e){
            log.error("Error fetching monthly statistics for storeId: " + storeId, e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public StoreStatisticsResponseDto getDailyAllStatistics(String email){
        try {
            validatieAdmin(email);
            return orderService.getDailyStatisticsForAllStores( LocalDate.now());
        }catch (Exception e){
            log.error("Error fetching daily statistics for storeId: " + e);
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false)
    public StoreStatisticsResponseDto getMonthlyAllStatistics(String email) {
        try {
            // 사장님 또는 관리자 권한 확인
            validatieAdmin(email);
            // 월간 통계 조회: 현재 월의 시작일과 끝일을 설정
            YearMonth currentMonth = YearMonth.now(); // 현재 월

            // 전체 배달 어플리케이션의 월간 통계 조회
            return orderService.getMonthlyStatisticsForAllStores(currentMonth);
        } catch (Exception e) {
            log.error("Error fetching monthly statistics for storeId: " +  e);
            throw e; // 예외를 그대로 던짐
        }
    }

    // 사장님 또는 관리자 권한 확인 메서드
    private void validateOwnerOrAdmin(String email, Long storeId) {
        User user = userRepository.findByEmailOrElseThrow(email);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException("해당 가게를 찾을 수 없습니다."));

        // 권한이 관리자나 사장님이 아닌 경우 예외 처리
        if (!user.getUserRole().equals(UserRole.ADMIN) && !user.getUserRole().equals(UserRole.OWNER) && !store.getUser().getEmail().equals(email)) {
            throw new UnauthorizedException("해당 통계를 조회할 권한이 없습니다.");
        }
    }
    private void validatieAdmin(String email){
        User user = userRepository.findByEmailOrElseThrow(email);
        if(!user.getUserRole().equals(UserRole.ADMIN)){
            throw new InvalidRoleException("권한이 존재 하지 않습니다.");
        }
    }
}
