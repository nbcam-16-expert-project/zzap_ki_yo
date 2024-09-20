package com.nbacm.zzap_ki_yo.domain.store.repository;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreNameContainingAndStoreType(String storeName, StoreType storeType);

    @Query("select s from Store s join fetch s.menus where s.storeId = :storeId")
    Store findByStoreId(Long storeId);

    List<Store> findAllByUserAndStoreType(User user, StoreType storeType);

    Store findByStoreIdAndUser(Long storeId, User user);
}

