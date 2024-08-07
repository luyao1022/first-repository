package jsola.com.txy.Service;

import jsola.com.txy.pojo.*;

public interface MaterialOpreationService {
    void saveMaterial(WarehouseAndMaterialDTO warehouseAndMaterialDTO);

    Material SaveMaterial(MaterialDTO materialDTO);

    void MoveMaterialFromStorage(Integer organizationId, Integer materialId);

    void MoveToOtherStorage(MoveMaterialToOtherDTO moveMaterialToOtherDTO);

    void MoveOtherWarehouse(MoveOtherWarehouseDTO moveOtherWarehouseDTO);
}
