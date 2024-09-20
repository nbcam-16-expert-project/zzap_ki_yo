package com.nbacm.zzap_ki_yo.domain.menu.entity;

import lombok.Getter;

@Getter
public enum MenuStatus {
    AVAILABLE, // 판매 중
    SOLD_OUT,  // 품절
    DELETE
}
