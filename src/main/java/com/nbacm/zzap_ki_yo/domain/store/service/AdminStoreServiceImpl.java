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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStoreServiceImpl implements AdminStoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CreateStoreResponseDto createStore(AuthUser authUser, StoreRequestDto storeRequestDto) {

       roleAdminCheck(authUser);

        if(storeRequestDto.getStoreAddress() == null
                || storeRequestDto.getStoreNumber() == null
                || storeRequestDto.getStoreName() == null){
            throw new BadRequestException("등록할 가게 이름, 주소, 번호가 없으면 안 됩니다.");
        }

       User user = findByEmail(authUser);

        Store store = Store.builder()
                .storeAddress(storeRequestDto.getStoreAddress())
                .storeNumber(storeRequestDto.getStoreNumber())
                .storeName(storeRequestDto.getStoreName())
                .favoriteCount(0)
                .orderMinPrice(storeRequestDto.getOrderMinPrice())
                .user(user)
                .storeType(StoreType.OPENING)
                .openingTime(storeRequestDto.getOpeningTime())
                .closingTime(storeRequestDto.getClosingTime())
                .build();

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
        User user = findByEmail(authUser);

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
        User user = findByEmail(authUser);

        Store store = findByStoreIdAndUser(storeId, user);

        storeRepository.delete(store);
    }


    @Override
    public SelectStoreResponseDto selectStore(AuthUser authUser, Long storeId) {
        roleAdminCheck(authUser);

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new StoreNotFoundException("가게를 찾을 수 없습니다."));

        if(store.getStoreType().equals(StoreType.CLOSING)){
            throw new UnauthorizedException("폐업한 가게는 조회할 수 없습니다.");
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
        List<Store> storeList = storeRepository.findAll().stream().toList();
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
        User user = findByEmail(authUser);

        Store store = findByStoreIdAndUser(storeId,user);
        if(requestDto.getMessage().equals("폐업합니다")) {
            store.closingStore(StoreType.CLOSING);
        }
        return ClosingStoreResponseDto.builder()
                .storeName(store.getStoreName())
                .storeType(store.getStoreType())
                .build();
    }

    private User findByEmail(AuthUser authUser){
        String email = authUser.getEmail();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
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
}
