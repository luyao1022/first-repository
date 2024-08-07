package jsola.com.txy.Controller;


import jsola.com.txy.Service.StorageOpreationService;
import jsola.com.txy.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 添加仓库，库区，库位
 */
@RestController
public class StorageController {
    @Autowired
    StorageOpreationService storageOpreationService;

    /**
     * 添加仓库
     * @param warehouse
     * @return
     */
    @PostMapping("/api/storageOpreation/saveOpreation")
    public Result saveStorage(@RequestBody WarehouseDTO warehouse){
       Warehouse saveWarehouse =  storageOpreationService.saveWarehouse(warehouse);
       return Result.success(saveWarehouse);
    }


    /**
     * 为仓库分区
     * @param warehouseId
     * @param countArea
     * @return
     */
    @PostMapping("/api/storageOpreation/saveArea")
    public Result saveArea(@RequestParam Integer warehouseId,@RequestParam Integer countArea){
      List<StorageArea> storageAreas =  storageOpreationService.saveAreaForWarehouse(warehouseId,countArea);
        return Result.success(storageAreas);
    }

    /**
     * 为区分库位
     * @param areaId
     * @param countLocation
     * @return
     */
    @PostMapping("api/storageOpreation/saveLocation")
    public Result saveLocation(@RequestParam Integer areaId,@RequestParam Integer countLocation){
        List<StorageLocation> storageLocations = storageOpreationService.saveLocationForArea(areaId,countLocation);
        return Result.success(storageLocations);
    }







    //删除仓库
    @PostMapping("api/storageOpreation/deleteStorage")
    public Result deleteStorage(@RequestParam Integer wareHouseId){
        storageOpreationService.deleteWarehouseByWarehouseId(wareHouseId);
        return Result.success("仓库删除成功");
    }


    //查找仓库
    //需要定义返回对象
    //查询时先进行redis查询
    //如果redis不存在在进行数据库的查询
    @PostMapping("api/storageOpreation/getAllWarehouse")
    public Result getAllWarehouse(){
       List<WarehouseVO> warehouseVOS =  storageOpreationService.getAllWarehouse();
        return Result.success(warehouseVOS);
    }
    //根据id查找仓库
    @PostMapping("api/storageOpreation/getWarehouseById")
    public Result getWarehouseByWarehouseId(@RequestParam Integer id){
        WarehouseVO warehouseVO = storageOpreationService.getWarehouseById(id);
        return Result.success(warehouseVO);
    }


}
