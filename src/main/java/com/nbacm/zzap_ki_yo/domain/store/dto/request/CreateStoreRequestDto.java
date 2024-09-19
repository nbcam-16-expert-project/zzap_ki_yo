package com.nbacm.zzap_ki_yo.domain.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@Data
public class CreateStoreRequestDto {

    private String storeName;
    private String storeAddress;
    private String storeNumber;
    private LocalTime openingTime;
    private LocalTime closingTime;

}

