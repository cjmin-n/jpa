package com.ohgiraffers.section03;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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

        entityManager.detach(foundMenu1); // 준영속화

        foundMenu.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        entityManager.merge(foundMenu1); // 다시 업데이트되면서 영속화된다.
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }


    @Test
    void close_테스트(){

        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        entityManager.close(); // 영속성 컨텍스트가 닫혔기 때문에 사용을 못하게 된다. // 위치에 맞게 닫아줘야 한다.

        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);

        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    void 삭제_remove_테스트(){

        /*
        * remove : 엔티티를 영속성 컨텍스트 에서 삭제한다.
        * 트랜잭션을 커밋하는 순간 데이터베이스에 반영된다.
        * */

        Menu foundMenu = entityManager.find(Menu.class, 2);
        entityManager.remove(foundMenu);

        Menu refoundMenu = entityManager.find(Menu.class, 2); // 이 매니저 안에서는 어차피 삭제된 거라서 다시 가져오지 않는다.
        Assertions.assertEquals(2, foundMenu.getMenuCode());
        Assertions.assertEquals(null, refoundMenu);
    }

    @Test
    void 병합_merge_수정_테스트 () {

        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuName("수박죽");

        Menu refoundMenu = entityManager.find(Menu.class, 3);

        System.out.println(menuToDetach.hashCode()); // 1157856790 - 준영속화 해줬으므로 다른 객체이다.
        System.out.println(refoundMenu.hashCode()); // 1620368915

        entityManager.merge(menuToDetach); // 준영속화하고 pk 바꾸면 머지하지 못한다.

        Menu mergedMenu = entityManager.find(Menu.class, 3);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName()); // true
    }


/*    @Test
    void 병합_merge_수정_테스트2 () {

        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        Menu refoundMenu = entityManager.find(Menu.class, 3);


        entityManager.merge(menuToDetach); // 준영속화하고 pk 바꾸면 머지하지 못한다.

        Menu mergedMenu = entityManager.find(Menu.class, 999);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName()); // true
    }*/

    @Test
    void 병합_merge_삽입_테스트(){

        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach);

        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("수박죽");

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.merge(menuToDetach); // 머지할 대상이 없으면 새로 만들어줌.
        entityTransaction.commit(); // 여기서 insert 날라감.

        Menu mergedMenu = entityManager.find(Menu.class, 999);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName());
    }
}
