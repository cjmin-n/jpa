package com.ohgiraffers.section03.projection;

import jakarta.persistence.*;

@Entity(name = "bidirection_menu")
@Table(name = "tbl_menu")
public class BiDirectionMenu {

    @Id
    @Column(name = "menu_code")
    private int menuCode;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private int menuPrice;

    @ManyToOne
    @JoinColumn(name = "category_code")
    private BiDirectionCategory category;

    @Column(name = "orderable_status")
    private String orderableStauts;

    public BiDirectionMenu() {
    }

    public BiDirectionMenu(int menuCode, String menuName, int menuPrice, BiDirectionCategory category, String orderableStauts) {
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.category = category;
        this.orderableStauts = orderableStauts;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
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

    public BiDirectionCategory getCategory() {
        return category;
    }

    public void setCategory(BiDirectionCategory category) {
        this.category = category;
    }

    public String getOrderableStauts() {
        return orderableStauts;
    }

    public void setOrderableStauts(String orderableStauts) {
        this.orderableStauts = orderableStauts;
    }

    @Override
    public String toString() {
        return "BiDirectionMenu{" +
                "menuCode=" + menuCode +
                ", menuName='" + menuName + '\'' +
                ", menuPrice=" + menuPrice +
                ", category=" + category +
                ", orderableStauts='" + orderableStauts + '\'' +
                '}';
    }
}
