package com.nbacm.zzap_ki_yo.domain.search.controller;

import com.nbacm.zzap_ki_yo.domain.search.dto.PopularWordResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.dto.SearchResponseDto;
import com.nbacm.zzap_ki_yo.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<SearchResponseDto> search(@RequestParam String keyword){
        SearchResponseDto responseDto = searchService.search(keyword);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/popular-word")
    public ResponseEntity<List<PopularWordResponseDto>> getPopularWord(){
        List<PopularWordResponseDto> responseDto = searchService.getPopularWord();
        return ResponseEntity.ok(responseDto);
    }
}
