package com.nbacm.zzap_ki_yo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
public class ZzapKiYoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZzapKiYoApplication.class, args);
    }

}
