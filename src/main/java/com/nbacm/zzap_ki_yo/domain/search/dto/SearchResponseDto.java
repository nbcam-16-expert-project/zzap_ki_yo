package com.nbacm.zzap_ki_yo.domain.search.dto;

import com.nbacm.zzap_ki_yo.domain.store.dto.response.MenuNamePrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponseDto {

   List<StoreNameDto> stores;
   List<MenuNamePrice> menuNamePrices;

   @Builder
   private SearchResponseDto(List<StoreNameDto> stores, List<MenuNamePrice> menuNamePrices) {
       this.stores = stores;
       this.menuNamePrices = menuNamePrices;
   }

   public static SearchResponseDto build(List<StoreNameDto> stores, List<MenuNamePrice> menuNamePrice) {
       return SearchResponseDto.builder()
               .stores(stores)
               .menuNamePrices(menuNamePrice)
               .build();
   }
}
