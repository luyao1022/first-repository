package jsola.com.txy.Mapper;


import jsola.com.txy.pojo.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommonMapper {

    @Select("select * from organization where id = #{organizationId} ")
    Organization getOrganzation(Integer organizationId);

}
