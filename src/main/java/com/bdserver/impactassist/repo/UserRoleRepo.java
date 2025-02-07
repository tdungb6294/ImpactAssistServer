package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.RoleDAO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserRoleRepo {
    @Insert("INSERT INTO user_roles(user_id, role_id) VALUES (#{userId}, #{roleId})")
    void createUser(Integer userId, Integer roleId);

    @Select("SELECT role_id AS id, name FROM user_roles ur JOIN roles r ON ur.role_id = r.id WHERE user_id = #{userId}")
    List<RoleDAO> getUserRolesByUserId(int userId);
}
