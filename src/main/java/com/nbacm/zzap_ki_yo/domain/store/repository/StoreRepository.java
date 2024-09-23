package com.nbacm.zzap_ki_yo.domain.store.repository;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import com.nbacm.zzap_ki_yo.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findAllByStoreType(StoreType storeType);

    List<Store> findAllByUserAndStoreType(User user, StoreType storeType);

    Store findByStoreIdAndUser(Long storeId, User user);

    Optional<Store> findById(Long storeId);

    @Query("SELECT s FROM Store s JOIN FETCH s.user WHERE s.storeId = :storeId")
    Optional<Store> findByIdWithUser(@Param("storeId") Long storeId);

    Page<Store> findByStoreNameContainingAndStoreType(String name, StoreType storeType, Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.storeType = :storeType ORDER BY " +
            "CASE WHEN s.adType = 'AD' THEN 0 ELSE 1 END, s.storeId")
    List<Store> findAllByStoreTypeOrderByAdTypeAndId(@Param("storeType") StoreType storeType);
}

