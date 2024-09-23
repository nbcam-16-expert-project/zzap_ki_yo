package com.nbacm.zzap_ki_yo.domain.store.converter;

import com.nbacm.zzap_ki_yo.domain.exception.NotFoundException;
import com.nbacm.zzap_ki_yo.domain.store.entity.Category;
import jakarta.persistence.AttributeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryListConverter implements AttributeConverter<List<Category>,String> {

    // 카테고리를 데이터 베이스로 전송 시 변환
    // 1. stream 변환 2. 문자열 변환 3. 콤마로 붙임
    @Override
    public String convertToDatabaseColumn (List<Category> categoryList){
        if (categoryList == null || categoryList.isEmpty()){
            throw new NotFoundException("카테고리를 찾을 수 없습니다.");
        }
        return categoryList.stream().
                map(Enum::name).
                collect(Collectors.joining(","));
    }

    // 데이터 베이스에서 불러온 값을 Entity 필드에 바인딩 할 때 수행
    // 1. stream 변환 2. 문자열을 Enum 으로 변환 3. Enum 을 리스트로
    @Override
    public List<Category> convertToEntityAttribute (String dbData){
        // 데이터가 있는지 확인
        if (dbData == null || dbData.isEmpty()){
            throw new NotFoundException("저장된 카테고리가 없습니다.");
        }
        return Arrays.stream(dbData.split(",")).
                map(Category::valueOf).
                collect(Collectors.toList());

    }
}
