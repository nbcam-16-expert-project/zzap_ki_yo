package com.nbacm.zzap_ki_yo.domain.store.repository;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreNameContainingAndStoreType(String storeName, StoreType storeType);

    List<Store> findAllByUserAndStoreType(User user, StoreType storeType);

    Store findByStoreIdAndUser(Long storeId, User user);

    Optional<Store> findById(Long storeId);

    @Query("SELECT s FROM Store s JOIN FETCH s.user WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithUser(@Param("storeId") Long storeId);

    List<Store> findAllByStoreType(StoreType storeType);
}

