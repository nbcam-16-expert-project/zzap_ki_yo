package com.nbacm.zzap_ki_yo.domain.menu.repository;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m JOIN FETCH m.store WHERE m.menuId = :menuId")
    Optional<Menu> findMenuWithStore(@Param("menuId") Long menuId);

    Page<Menu> findByMenuNameContaining(String menuName, Pageable pageable);

}
