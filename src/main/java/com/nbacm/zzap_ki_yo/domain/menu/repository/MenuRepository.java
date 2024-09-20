package com.nbacm.zzap_ki_yo.domain.menu.repository;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
