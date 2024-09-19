package com.nbacm.zzap_ki_yo.domain.menu.temporary;

import com.nbacm.zzap_ki_yo.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {
    private final Service service;

    @PostMapping("/asdf")
    public void savemenu(
            @RequestBody SaveMenuRequest saveMenuRequest
    ){
        service.saveMenu(saveMenuRequest);
    }
}
