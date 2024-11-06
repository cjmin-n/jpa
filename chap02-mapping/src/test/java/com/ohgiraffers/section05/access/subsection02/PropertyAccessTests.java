package com.ohgiraffers.section05.access.subsection02;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class PropertyAccessTests {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManger(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }



    @Test
    void 프로퍼티_접근_테스트() {
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickName("홍길동");

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();

        String jpql = "SELECT memberId FROM member_section05_subsection02 WHERE memberNo = 1";
        String registeId = entityManager.createQuery(jpql, String.class).getSingleResult();

        System.out.println(registeId);
        Assertions.assertEquals("user01", registeId);
    }
}
