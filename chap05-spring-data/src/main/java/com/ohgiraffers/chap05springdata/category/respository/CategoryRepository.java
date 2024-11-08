package com.ohgiraffers.chap05springdata.category.respository;

import com.ohgiraffers.chap05springdata.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {


    Category findByCategoryCode(int categoryCode); // entity 의 필드명 기준으로 where 문을 작성해준다.

    Category findByCategoryName(String categoryName);
}
