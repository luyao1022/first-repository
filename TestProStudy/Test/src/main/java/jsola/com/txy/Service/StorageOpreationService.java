package jsola.com.txy.Service;

import jsola.com.txy.pojo.*;


import java.util.List;

public interface StorageOpreationService {

    Warehouse saveWarehouse(WarehouseDTO warehouse);

    List<StorageArea> saveAreaForWarehouse(Integer warehouseId, Integer countArea);

    List<StorageLocation> saveLocationForArea(Integer areaId, Integer countLocation);

    void deleteWarehouseByWarehouseId(Integer wareHouseId);

    List<WarehouseVO> getAllWarehouse();

    WarehouseVO getWarehouseById(Integer id);
}
