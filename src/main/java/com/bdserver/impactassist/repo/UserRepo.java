package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.RegisterLocalExpertDAO;
import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.ResponseUserDataDAO;
import com.bdserver.impactassist.model.UserDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepo {
    @Select("INSERT INTO users(full_name, email, password, phone) VALUES(#{fullName}, #{email}, #{password}, #{phone}) RETURNING id")
    Integer createUser(RegisterUserDAO user);

    @Select("SELECT id, full_name as fullName, email, password, phone, created_at as createdAt, updated_at as updatedAt FROM users WHERE email = #{username}")
    UserDAO findByUsername(String username);

    @Select("SELECT id, full_name as fullName, email, phone FROM users")
    List<UserDAO> findAll();

    @Select("SELECT id, full_name as fullName, email, password, phone, created_at as createdAt, updated_at as updatedAt FROM users WHERE id = ${id}")
    UserDAO findUserById(int id);

    @Insert("INSERT INTO local_experts (user_id, longitude, latitude, description) VALUES (#{userId}, #{longitude}, #{latitude}, #{description})")
    void createLocalExpert(RegisterLocalExpertDAO user);

    @Select("SELECT full_name as fullName, email, phone FROM users WHERE id = ${id}")
    ResponseUserDataDAO findUserDataById(int id);
}
