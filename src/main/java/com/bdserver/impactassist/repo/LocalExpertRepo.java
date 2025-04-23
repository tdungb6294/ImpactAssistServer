package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LocalExpertRepo {
    @SelectProvider(value = LocalExpertSqlProvider.class, method = "getLocalExperts")
    List<ResponseLocalExpertDAO> getLocalExpertList(String search);

    @Select("SELECT id, full_name as fullName, email, phone, longitude, latitude, description FROM local_experts le JOIN users u ON le.user_id=u.id WHERE le.user_id = #{id}")
    ResponseLocalExpertDAO getLocalExpertById(int id);
}
