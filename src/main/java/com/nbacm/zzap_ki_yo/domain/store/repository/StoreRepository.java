package com.nbacm.zzap_ki_yo.domain.store.repository;

import com.nbacm.zzap_ki_yo.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
