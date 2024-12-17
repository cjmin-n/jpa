package com.ohgiraffers.chap05springdata.menu.service;

import com.ohgiraffers.chap05springdata.menu.dto.MenuDTO;
import com.ohgiraffers.chap05springdata.menu.entity.Menu;
import com.ohgiraffers.chap05springdata.menu.infra.CategoryFind;
import com.ohgiraffers.chap05springdata.menu.respository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired // 느슨한 결합 구조를 위해 서비스를 직접 넣지 않는다.
    private CategoryFind categoryFind;

    public List<Menu> selectAll() {

        List<Menu> menuList = menuRepository.findAll();

        if(menuList != null) {
            return menuList;
        }
        return null;
    }

    public Object insertMenu(MenuDTO menuDTO) {

        // 메뉴 이름 중복 체크
        Menu menu = menuRepository.findByMenuName(menuDTO.getMenuName());

        if(!Objects.isNull(menu)){
            return new String(menuDTO.getMenuName() + " 메뉴가 존재함.");
        }

        // 가격 유효성 검사
        if(menuDTO.getMenuPrice() <= 0){
            return new String(menuDTO.getMenuName() + " 가격이 잘못 입력됨");
        }

        // 카테고리 코드 검사
        Integer categoryCode = categoryFind.getCategory(menuDTO.getCategoryCode());

        if(Objects.isNull(categoryCode)){
            return menuDTO.getCategoryCode() + "는 존재하지 않습니다.";
        }

        Menu newMenu = new Menu();
        newMenu.setMenuName(menuDTO.getMenuName());
        newMenu.setMenuPrice(menuDTO.getMenuPrice());
        newMenu.setCategoryCode(menuDTO.getCategoryCode());
        newMenu.setOrderableStatus(menuDTO.getOrderableStatus());

        Menu result = menuRepository.save(newMenu);

        return result;
    }


    public Object updateMenu(Map<String, Object> menus) {

        if(Objects.isNull(menus)){
            return new String("업데이트할 메뉴가 없습니다.");
        }

        Integer menuCode = (Integer) menus.get("menuCode");
        String menuName = (String) menus.get("menuName");
        int menuPrice = (Integer) menus.get("menuPrice");
        int categoryCode = (Integer) menus.get("categoryCode");
        String orderableStatus = (String) menus.get("orderableStatus");

        Menu menu = menuRepository.findByMenuCode(menuCode);

        menu.setMenuName(menuName);
        menu.setMenuPrice(menuPrice);
        menu.setCategoryCode(categoryCode);
        menu.setOrderableStatus(orderableStatus);

        Menu result = menuRepository.save(menu);

        return result;
    }


    public Object removeMenu(Map<String, Object> menus) {

        Integer menuCode = (Integer) menus.get("menuCode");

        if(menuCode == null){
            return new String("삭제할 메뉴가 없습니다.");
        }

        Menu deleteMenu = menuRepository.findByMenuCode(menuCode);

        if(Objects.isNull(deleteMenu)){
            return new String("삭제할 메뉴가 없습니다.");
        }

        menuRepository.delete(deleteMenu);
        return "delete 성공";
    }
}
