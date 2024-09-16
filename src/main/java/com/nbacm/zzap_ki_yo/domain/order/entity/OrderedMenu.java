package com.nbacm.zzap_ki_yo.domain.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class OrderedMenu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
