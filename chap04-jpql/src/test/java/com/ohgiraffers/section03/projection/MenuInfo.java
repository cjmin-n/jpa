package com.ohgiraffers.section03.projection;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class MenuInfo {

    // 복합키 = 기본키가 여러개인것
    // Embedded 일때만 !!  Embeddable 타입을 적용시킬 때는 꼭 @Id(기본키) 가 아니어도 된다.
    // 2개가 세트로 중복값이 있으면 안된다.
    // 실질적으로 사용하려면 db 에 unique 제약조건을 걸어줘야 한다.
    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    public MenuInfo() {
    }

    public MenuInfo(String menuName, int menuPrice) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    @Override
    public String toString() {
        return "MenuInfo{" +
                "menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                '}';
    }
}
