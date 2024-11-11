package com.ohgiraffers.chap06securityjwt.user.repository;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
