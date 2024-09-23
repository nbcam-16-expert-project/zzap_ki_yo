package com.nbacm.zzap_ki_yo.domain.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class PopularWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String word;
    private Long popularity;


    @Builder
    private PopularWord(String word, Long popularity) {
        this.word = word;
        this.popularity = popularity;
    }

    public static PopularWord create(String word, Long popularity) {
        return PopularWord.builder().word(word).popularity(popularity).build();
    }

    public void countUp(Long popularity) {
      this.popularity += popularity;
    }
}
