package com.nbacm.zzap_ki_yo.domain.menu.temporary;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class Service {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void saveMenu(SaveMenuRequest saveMenuRequest) {
        Store store = storeRepository.findById(saveMenuRequest.getStoreId())
                .orElseThrow(()-> new NotFoundException("asdf"));
        Menu menu = new Menu(
                saveMenuRequest.getMenuName(),
                saveMenuRequest.getPrice(),
                store
        );
        menuRepository.save(menu);
    }
}
