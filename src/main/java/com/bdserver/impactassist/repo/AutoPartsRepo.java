package com.bdserver.impactassist.repo;

import com.bdserver.impactassist.model.PartialAutoPartsAndServicesDAO;
import com.bdserver.impactassist.model.ResponseAutoPartsCategoryDAO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AutoPartsRepo {

    @SelectProvider(value = AutoPartsSqlProvider.class, method = "getCategories")
    List<ResponseAutoPartsCategoryDAO> getCategories(String search, int offset, int limit, String lang);

    @SelectProvider(value = AutoPartsSqlProvider.class, method = "getAutoPartsAndServices")
    List<PartialAutoPartsAndServicesDAO> getAutoPartsAndServices(List<Integer> category, String search, int offset, int limit, String lang);

    @SelectProvider(value = AutoPartsSqlProvider.class, method = "getCategoriesCount")
    int getCategoriesCount(String search, String lang);

    @SelectProvider(value = AutoPartsSqlProvider.class, method = "getAutoPartsAndServicesCount")
    int getAutoPartsAndServicesCount(List<Integer> category, String search, String lang);
}
