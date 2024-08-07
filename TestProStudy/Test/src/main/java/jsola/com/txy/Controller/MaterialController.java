package jsola.com.txy.Controller;


import jsola.com.txy.Service.ServiceImpl.MaterialOpreationServiceImpl;
import jsola.com.txy.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 物料管理
 */
@RestController
@Slf4j
public class MaterialController {

    @Autowired
    MaterialOpreationServiceImpl materialOpreationService;

    /**
     * 物料入库
     * @param warehouseAndMaterialDTO
     * @return
     */
    @PostMapping("api/materialOperation/SaveMaterialReception")
    public Result SaveMaterialReseption(@RequestBody WarehouseAndMaterialDTO warehouseAndMaterialDTO){
        materialOpreationService.saveMaterial(warehouseAndMaterialDTO);
        return Result.success("物料入库成功");
    }

    /**
     * 物料信息录入
     */
    @PostMapping("api/materialOperation/SaveMaterial")
    public Result SaveMaterial(@RequestBody MaterialDTO materialDTO){
        Material material = materialOpreationService.SaveMaterial(materialDTO);
        return Result.success(material);
    }

    /**
     * 物料出库
     */
    @PostMapping("api/materialOperation/MoveMaterialFromStorage")
    public Result MoveMaterialFromStorage(@RequestParam Integer organizationId,@RequestParam Integer MaterialId){
        materialOpreationService.MoveMaterialFromStorage(organizationId,MaterialId);
        return Result.success("物料出库成功");
    }

    /**
     * 物料移位
     * @param moveMaterialToOtherDTO
     * @return
     */
    @PostMapping("api/materialOperation/MoveOtherStorage")
    public Result MoveOtherStorage(@RequestBody MoveMaterialToOtherDTO moveMaterialToOtherDTO){
         materialOpreationService.MoveToOtherStorage(moveMaterialToOtherDTO);
        return Result.success();
    }

    /**
     * 物料调拨
     * @return
     */
    @PostMapping("api/materialOperation/MoveOtherWarehouse")
    public Result MoveOtherWarehouse(@RequestBody MoveOtherWarehouseDTO moveOtherWarehouseDTO){
        materialOpreationService.MoveOtherWarehouse(moveOtherWarehouseDTO);
        return Result.success();
    }
}
