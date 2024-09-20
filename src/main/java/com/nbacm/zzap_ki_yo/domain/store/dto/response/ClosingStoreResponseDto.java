package com.nbacm.zzap_ki_yo.domain.store.dto.response;

import com.nbacm.zzap_ki_yo.domain.store.entity.StoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class ClosingStoreResponseDto {
   private String storeName;
   private StoreType storeType;

   @Builder
    public ClosingStoreResponseDto(String storeName, StoreType storeType) {
        this.storeName = storeName;
        this.storeType = storeType;
    }

}

