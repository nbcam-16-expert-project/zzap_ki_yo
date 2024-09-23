package com.nbacm.zzap_ki_yo.domain.search.repository;

import com.nbacm.zzap_ki_yo.domain.search.entity.PopularWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopularWordRepository extends JpaRepository<PopularWord, Long> {

    PopularWord findByWord(String word);

    List<PopularWord> findTop10ByOrderByPopularityDesc();
}
