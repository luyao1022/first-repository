package jsola.com.txy.Mapper;


import jsola.com.txy.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface StorageOpreationMapper {

    @Insert("insert into storage_system3.warehouse(name, organization_id, has_location) VALUES (#{name},#{organizationId},#{hasLocation})")
    Warehouse addWarehouse(Integer organizationId, Boolean hasLocation, String name);



    @Select("select * from warehouse where id = #{warehouseId} ")
    Warehouse getWarehouse(Integer warehouseId);


    void addArea(ArrayList<String> areaNames, Integer warehouseId);


    @Select("select * from storage_area where warehouse_id = #{warehouseId}")
    List<StorageArea> getAllAreaByWarehouseId(Integer warehouseId);


    @Select("select * from storage_ara where areaId = #{areaId}")
    StorageArea getAreaById(Integer areaId);


    @Update("update storage_system3.warehouse set ready_area = true where id = #{wareId}")
    void setReadyArea(Integer wareId);

    void AddLocation(Integer areaId, ArrayList<String> locationName);


    @Update("update storage_system3.storage_area set ready_location = true where id = #{areaId}")
    void setReadyLocation(Integer areaId);



    @Select("select * from storage_location where area_id = #{areaId}")
    List<StorageLocation> getAllLocationByAreaId(Integer areaId);


    @Select("SELECT wh.* FROM storage_area sa LEFT JOIN warehouse wh ON sa.warehouse_id = wh.id ")
    Warehouse getBelongToWarehouse(Integer areaId);


    @Select("select * from warehouse where id = #{wareHouseId} ")
    Warehouse getWarehouseByWarehouseId(Integer wareHouseId);

    List<Integer> getAllLocationByAreaIdList(List<Integer> collect);

    void deleteByLocationIdList(List<Integer> allLocationId);

    void deleteByAreaIdList(List<Integer> collect);


    @Delete("delete from storage_system3.warehouse where  id = #{wareHouseId}")
    void deleteByWarehouseId(Integer wareHouseId);

    @Select("SELECT * FROM warehouse")
    List<WarehouseVO> selectAllWarehouses();


    /*查询getWarehouse*/

    @Select("select * from warehouse where id = #{id}")
    WarehouseVO getWarehouseById(Integer id);


    @Select("select material_id from storage where id = #{locationId}")
    Integer getLocationMaterialId(Integer locationId);


    @Select("select id from storage_location where id = #{locationId}")
    Integer getLoaction(Integer locationId);


    @Select("select * from organization where id = #{organizationId}")
    Organization getOrganizationById(Integer organizationId);
}
