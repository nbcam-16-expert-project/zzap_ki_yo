package com.nbacm.zzap_ki_yo.domain.favorite.repository;

import com.nbacm.zzap_ki_yo.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
}
