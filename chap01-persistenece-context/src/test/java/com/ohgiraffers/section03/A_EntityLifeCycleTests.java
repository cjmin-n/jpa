package com.ohgiraffers.section03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityLifeCycleTests {

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


    /*
    * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 지정하는 공간으로,
    * 엔티티 객체를 보관하고 정리한다.
    * 엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어진다.
    *
    * 엔티티의 생명주기
    * 비영속, 영속, 준영속
    * */

    @Test
    void 비영속_테스트(){

        Menu foundMenu = entityManager.find(Menu.class, 11);

        // 객체만 생성하면, 영속성 컨텍스트나 db 와 관련없는 비영속 상태
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Assertions.assertFalse(foundMenu == newMenu);
    }

    @Test
    void 영속성_연속_조회_테스트() {
        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 저장(persist) 하면
        * 영속성 컨텍스트가 엔티티 객체를 관리하게 되고 이를 영속 상태라고 한다.
        * find(), jpql 을 사용한 조회도 영속 상태가 된다.
        * */

        Menu foundMenu1 = entityManager.find(Menu.class, 11); // 영속화
        Menu foundMenu2 = entityManager.find(Menu.class, 11); // 영속화 된 걸 받아옴 // 쿼리문 1개

        Assertions.assertTrue(foundMenu1 == foundMenu2);
    }

    @Test
    void 영속성_객체_추가_테스트() {

        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500); // Menu 의 @GeneratedValue(strategy = GenerationType.IDENTITY) 주석처리
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");

        entityManager.persist(menuToRegist); // 영속성 추가하면 db 에 반영되지 않아도 가져올 수 있옴.
        Menu foundMenu = entityManager.find(Menu.class, 500);

        Assertions.assertTrue(foundMenu == menuToRegist);
    }


    @Test
    void 준영속_detach_테스트() {

        Menu foundMenu = entityManager.find(Menu.class, 11);
        Menu foundMenu1 = entityManager.find(Menu.class, 12);

        /*
        * 영속성 컨텍스트가 관리하던 엔티티 객체가 더 이상 관리되지 않는 상태
        * 로 전환되면(detach), 해당 객체는 준영속 상태로 바뀐다.
        * 이는 JPA 객체의 변경 사항이 데이터베이스에 자동 반영되지 않는 상태
        *
        * Detach 메소드를 사용하면 특정 엔티티를 준영속 상태로 만들 수 있다.
        * 즉, 원하는 객체만 선택적으로 영속성 컨텍스트에서 분리할 수 있다.
        * */


    }
}
