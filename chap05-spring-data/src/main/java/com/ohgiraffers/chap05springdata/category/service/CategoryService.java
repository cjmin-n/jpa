package com.ohgiraffers.chap05springdata.category.service;

import com.ohgiraffers.chap05springdata.category.entity.Category;
import com.ohgiraffers.chap05springdata.category.respository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> selectAllCategory() {

        List<Category> categoryList = categoryRepository.findAll();

        if(categoryList.isEmpty()){
            return null;
        }

        return categoryList;
    }


    public Category selectCategoryByCode(int categoryCode) {
        Category category = categoryRepository.findByCategoryCode(categoryCode);

        if(Objects.isNull(category)){
            return null;
        }

        return category;
    }

    public Category insertCategory(String categoryName) {

        // 이름 중복 체크
        Category foundCategory = categoryRepository.findByCategoryName(categoryName);

        if(!Objects.isNull(foundCategory)){
            return null; // db 에 존재하면
        }

        Category insertCategory = new Category();
        insertCategory.setCategoryName(categoryName);

        Category result = categoryRepository.save(insertCategory);
        /*
        * save() 는 jpa 에서 EntityManager 를 통해 데이터를 저장하는 메소드
        * 기본적으로, jpa 는 트랜잭션 내에서 자동으로 커밋을 처리한다.
        * 새로운 엔티티의 경우 : db 에 저장하고 저장한 객체 반환
        * 기존에 존재하는 엔티티의 경우 : 해당 엔티티의 정보를 업데이트하고 업데이트한 객체 반환
        * */
        return result;
    }

    public Category updateCategory(Category categoryDTO) {

        Category foundCategory = categoryRepository.findByCategoryCode(categoryDTO.getCategoryCode());
        if(Objects.isNull(foundCategory)){
           return null;
        }

        foundCategory.setCategoryName(categoryDTO.getCategoryName());
        Category result = categoryRepository.save(foundCategory);
        return result;
    }

    public boolean deleteCategory(Integer categoryCode) {

        Category category = categoryRepository.findById(categoryCode).orElse(null);
        // findById 는 jpa 기본 제공 메소드
        // 아이디가 없는 값일 수 있기 때문에 대체까지 해야한다.
        // 없으면 null 값을 설정.

        if(category == null){
            return false;
        }
        categoryRepository.delete(category); // delete 는 반환 타입이 void 여서 존재하지 않으면 바로 에러난다.
        return true;
    }

    public Integer findByCategory(int code) {

        Category category = categoryRepository.findByCategoryCode(code);

        if(Objects.isNull(category)){
            return null;
        }

        return category.getCategoryCode();
    }
}
