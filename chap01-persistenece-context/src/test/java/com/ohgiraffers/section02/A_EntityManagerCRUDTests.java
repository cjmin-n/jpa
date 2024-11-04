package com.ohgiraffers.section02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityManagerCRUDTests {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    @Test
    public void 메뉴코드로_메뉴조회_테스트() {

        int menuCode = 1;

        Menu foundMenu = entityManager.find(Menu.class, menuCode); // 기본적으로 id 만 넣을 수 있다.

        System.out.println("foundMenu = " + foundMenu);

        Assertions.assertNotNull(foundMenu);
    }
}
