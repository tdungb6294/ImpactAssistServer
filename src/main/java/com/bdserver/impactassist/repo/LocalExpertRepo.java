package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.ResponseLocalExpertDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LocalExpertRepo {
    @Select("SELECT id, full_name as fullName, email, phone, longitude, latitude, description FROM local_experts le JOIN users u ON le.user_id=u.id")
    List<ResponseLocalExpertDAO> getLocalExpertList();
}
