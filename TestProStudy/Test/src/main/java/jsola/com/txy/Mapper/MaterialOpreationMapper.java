package jsola.com.txy.Mapper;


import jsola.com.txy.pojo.Inventory;
import jsola.com.txy.pojo.MaterialDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MaterialOpreationMapper {



    @Select("select description from material where id =  #{materialId}")
    String getMaterialDescription(Integer materialId);



    @Select("select * from inventory where location_id = #{locationalId}")
    List<Inventory> getInventoryByLocationId(Integer locationalId);

    void saveMaterial(MaterialDTO materialDTO);


    @Insert("insert into storage_system3.inventory(location_id, material_id) VALUES (#{locationId},#{materialId})")
    void saveMaterialToInventoryForLocation(Integer locationId, Integer materialId);


    @Insert("insert into storage_system3.inventory(area_id, material_id) VALUES (#{areaId},#{materialId})")
    void saveMaterialToInventoryForArea(Integer areaId, Integer materialId);


    @Select("select * from inventory where material_id = #{materialId}")
    List<Inventory> getInventoryByMaterialId(Integer materialId);



    void moveMaterialFromInventory(List<Integer> inventoryIdList);





    List<Integer> getAllAreaIds(List<Integer> locationIds);



    List<Integer> getAllWarehouseIds(List<Integer> newAreaIds);
}
