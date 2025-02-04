package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.RegisterUserDAO;
import com.bdserver.impactassist.model.UserDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRepo {
    @Insert("INSERT INTO users(full_name, email, password, roles) VALUES(#{fullName}, #{email}, #{password}, 'USER')")
    Integer createUser(RegisterUserDAO user);

    @Select("SELECT * FROM users WHERE email = #{username}")
    UserDAO findByUsername(String username);

    @Select("SELECT * FROM users")
    List<UserDAO> findAll();
}
