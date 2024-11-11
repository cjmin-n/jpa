package com.ohgiraffers.chap05springdata.menu.infra;

import com.ohgiraffers.chap05springdata.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryFindImpl implements CategoryFind{

    // 카테고리가 변경/확장 되더라도 구현체만 바꾸면 되기때문에
    // 다른 패키지가 직접 소통을 하지 않게
    // 느슨한 결합체를 만들 수 있음

    @Autowired
    CategoryService categoryService;

    @Override
    public Integer getCategory(int code) {
        return categoryService.findByCategory(code);
    }
}
