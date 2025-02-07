package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.UserDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepo {
    @Select("INSERT INTO users(full_name, email, password) VALUES(#{fullName}, #{email}, #{password}) RETURNING id")
    Integer createUser(RegisterUserDAO user);

    @Select("SELECT * FROM users WHERE email = #{username}")
    UserDAO findByUsername(String username);

    @Select("SELECT id, full_name as fullName, email, phone FROM users")
    List<UserDAO> findAll();

    @Select("SELECT * FROM users WHERE id = ${id}")
    UserDAO findUserById(int id);
}
