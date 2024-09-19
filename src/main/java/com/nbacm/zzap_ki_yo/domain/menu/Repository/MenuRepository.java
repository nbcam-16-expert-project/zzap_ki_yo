package com.nbacm.zzap_ki_yo.domain.menu.Repository;

import com.nbacm.zzap_ki_yo.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu,Long> {
}
