package com.ohgiraffers.chap05springdata.menu.controller;

import com.ohgiraffers.chap05springdata.menu.dto.MenuDTO;
import com.ohgiraffers.chap05springdata.menu.entity.Menu;
import com.ohgiraffers.chap05springdata.menu.infra.CategoryFindImpl;
import com.ohgiraffers.chap05springdata.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private CategoryFindImpl categoryFindImpl;

    @GetMapping("/select")
    public ResponseEntity<Object> selectAll() {

        List<Menu> menulist = menuService.selectAll();

        try {
            if (menulist != null) {
                return ResponseEntity.ok(menulist);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("메뉴가 존재하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버에서 오류가 발생했습니다.");
        }

    }

    @PostMapping("insert")
    public ResponseEntity<Object> insertMenu(@RequestBody MenuDTO menuDTO) {

        Object result = menuService.insertMenu(menuDTO);

        if (result instanceof Menu) {
            Menu response = (Menu) result;
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(result);
    }


    @PostMapping("update")
    public ResponseEntity<Object> updateMenu(@RequestBody Map<String, Object> menus) {


        Object result = menuService.updateMenu(menus);
        if (result instanceof Menu) {
            Menu response = (Menu) result;
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(404).body(result);
    }

    @PostMapping("remove")
    public ResponseEntity<Object> removeMenu(@RequestBody Map<String, Object> menus) {

        System.out.println("menus = " + menus);

        Object result = menuService.removeMenu(menus);
        System.out.println("result = " + result);

        if (!Objects.isNull(result)) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(404).body(result);
    }
}

