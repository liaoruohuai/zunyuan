package com.learning.login.repository;

import com.learning.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:GR
 * Date:2016/11/11
 * Discription:用户repository
 */
public interface UserRepository extends JpaRepository<User,Integer>{

    User findByLoginNameAndPasswordAndIsDelete(String loginName,String password,String isDelete);
}
