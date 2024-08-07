package jsola.com.txy.Service.ServiceImpl;

import jsola.com.txy.Mapper.MaterialOpreationMapper;
import jsola.com.txy.Mapper.StorageOpreationMapper;
import jsola.com.txy.Service.MaterialOpreationService;
import jsola.com.txy.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class MaterialOpreationServiceImpl implements MaterialOpreationService {
    @Autowired
    StorageOpreationMapper storageOpreationMapper;
    @Autowired
    MaterialOpreationMapper materialOpreationMapper;


    /**
     * 物料入库
     *
     * @param warehouseAndMaterialDTO
     */
    @Override
    @Transactional
    public void saveMaterial(WarehouseAndMaterialDTO warehouseAndMaterialDTO) {
        //查询该仓库是否存在
        Integer warehouseId = warehouseAndMaterialDTO.getWarehouseId();
        Warehouse warehouse = storageOpreationMapper.getWarehouse(warehouseId);
        if (Objects.isNull(warehouse)) {
            log.error("该仓库不存在");
            throw new RuntimeException("仓库不存在");
        }
        //判断该仓库是分区存放还是分库存放

        //1分库存放
        if (warehouse.getHasLocation()) {
            Integer locationId = warehouseAndMaterialDTO.getLocationId();
            //判断当前库位是否存在
            Integer locationalId = storageOpreationMapper.getLoaction(locationId);
            //如果库位存在，查询investory该库位是否存放了物料
            if (locationalId == null) {
                log.error("当前库位不存在");
                throw new RuntimeException("当前库位id不存在");
            }

            //通过locationalId 在inventory查询出inventory之后判断描述信息是否相符
            List<Inventory> inventories = materialOpreationMapper.getInventoryByLocationId(locationalId);
            //如果已经存放了物料，在判断是否需要存放相同类型的物料
            String materialDescription = materialOpreationMapper.getMaterialDescription(inventories.get(0).getMaterialId());
            if (materialDescription.equals(null)) {
                //直接放入，当前库位没有物料存入
                addMaterialToInventory(warehouseAndMaterialDTO);
            }
            //同种物料，并且勾选同意之后就可以入库
            if (warehouseAndMaterialDTO.getDescription().equals(materialDescription) && warehouseAndMaterialDTO.getFalg()) {
                addMaterialToInventory(warehouseAndMaterialDTO);
            } else {
                log.error("当前物料描述不匹配，无法入库");
            }
        }
        //2分区存放
        else {
            //不区分物料描述信息，直接入库
            addMaterialToInventory(warehouseAndMaterialDTO);
        }
    }

    /**
     * 物料入库
     *
     * @param materialDTO
     * @return
     */
    @Override
    public Material SaveMaterial(MaterialDTO materialDTO) {
        materialOpreationMapper.saveMaterial(materialDTO);
        Material material = new Material();
        BeanUtils.copyProperties(materialDTO, material);
        return material;
    }

    /**
     * 物料出库
     *
     * @param organizationId
     * @param materialId
     */
    @Override
    @Transactional
    public void MoveMaterialFromStorage(Integer organizationId, Integer materialId) {
        //判断当前组织是否合法
        Organization organization = storageOpreationMapper.getOrganizationById(organizationId);
        if (!Objects.isNull(organization)) {
            log.error("物料无法完成出库，组织不合法");
            throw new RuntimeException("物料无法完成出库，组织不合法");
        }
        //判断物料存在库中
        List<Inventory> inventory = materialOpreationMapper.getInventoryByMaterialId(materialId);
        if (inventory.size() <= 0) {
            log.error("物料不存在，无法完成出库");
            throw new RuntimeException("物料不存在，无法完成出库");
        }

        List<Integer> inventoryIdList = inventory.stream().map(Inventory::getId).collect(Collectors.toList());
        //将inventoryList进行出库
        materialOpreationMapper.moveMaterialFromInventory(inventoryIdList);
    }

    /**
     * 物料移位
     *
     * @param moveMaterialToOtherDTO
     * @return
     */
    @Override
    @Transactional
    public void MoveToOtherStorage(MoveMaterialToOtherDTO moveMaterialToOtherDTO) {
        try {

            //判断是否符合移位规范，是否归属同一仓库（当前仓库id和移去的仓库id是否相等）
            Integer materialId = moveMaterialToOtherDTO.getMaterialId();
            //当前的materialId的所属仓库id
            List<Inventory> inventoryByMaterialId = materialOpreationMapper.getInventoryByMaterialId(materialId);


            //在库位所存放的物料
            List<Integer> locationIds = inventoryByMaterialId.stream().filter(inventory -> inventory.getLocationId() != null).map(Inventory::getLocationId).collect(Collectors.toList());
            //在区位所存放的物料
            List<Integer> areaIds = inventoryByMaterialId.stream().filter(inventory -> inventory.getAreaId() != null).map(Inventory::getAreaId).collect(Collectors.toList());
            //根据库位查询出区位的id
            List<Integer> areaIdsFromLocation = materialOpreationMapper.getAllAreaIds(locationIds);

            List<Integer> newAreaIds = Stream.concat(areaIds.stream(), areaIdsFromLocation.stream()).collect(Collectors.toList());
            //通过areaids查询出所有的warehouse id
            List<Integer> warehouseIds = materialOpreationMapper.getAllWarehouseIds(newAreaIds);

            for (Integer warehouseId : warehouseIds) {
                if (warehouseId != moveMaterialToOtherDTO.getWarehouseId()) {
                    log.error("当前操作不符合移位规范");
                    throw new RuntimeException("当前操作不符合移位规范");
                }
            }

            //判断要移去的是库位还是库区
            Integer moveToLocationId = moveMaterialToOtherDTO.getMoveToLocationId();
            Integer moveToAreaId = moveMaterialToOtherDTO.getMoveToAreaId();

            if (moveToLocationId.equals(null) && moveToAreaId.equals(null)) {
                log.error("移去的库位为空库位，请检查");
                throw new RuntimeException("移去的库位为空库位，请检查");
            }
            //移区
            if (moveToLocationId.equals(null)) {
                //对原来的物料出库
                MoveMaterialFromStorage(moveMaterialToOtherDTO.getOrganizedId(), moveMaterialToOtherDTO.getMaterialId());
                //入库
                WarehouseAndMaterialDTO warehouseAndMaterialDTO = new WarehouseAndMaterialDTO();
                warehouseAndMaterialDTO.setMaterialId(moveMaterialToOtherDTO.getMaterialId());
                warehouseAndMaterialDTO.setAreaId(moveMaterialToOtherDTO.getMoveToAreaId());
                warehouseAndMaterialDTO.setLocationId(moveMaterialToOtherDTO.getMoveToLocationId());
                warehouseAndMaterialDTO.setFalg(moveMaterialToOtherDTO.getFlag());
                warehouseAndMaterialDTO.setWarehouseId(moveMaterialToOtherDTO.getWarehouseId());
                saveMaterial(warehouseAndMaterialDTO);
            }
        } catch (RuntimeException e) {
            log.error("移位出错");
            throw new RuntimeException("移位出错");
        }

    }


    /**
     * 将物料信息存入inventory
     *
     * @param warehouseAndMaterialDTO
     */
    private void addMaterialToInventory(WarehouseAndMaterialDTO warehouseAndMaterialDTO) {
        Integer materialId = warehouseAndMaterialDTO.getMaterialId();
        //分区存放的情况
        if (warehouseAndMaterialDTO != null) {
            Integer areaId = warehouseAndMaterialDTO.getAreaId();
            materialOpreationMapper.saveMaterialToInventoryForArea(areaId, materialId);
        }
        Integer locationId = warehouseAndMaterialDTO.getLocationId();
        materialOpreationMapper.saveMaterialToInventoryForLocation(locationId, materialId);

    }


    /**
     * 调拨
     *
     * @param moveOtherWarehouseDTO
     */
    @Override
    public void MoveOtherWarehouse(MoveOtherWarehouseDTO moveOtherWarehouseDTO) {
        //判断是组织间调拨还是组织内调拨

        Integer fromOrganizedId = moveOtherWarehouseDTO.getFromOrganizedId();
        Integer toOrganizedId = moveOtherWarehouseDTO.getToOrganizedId();
        Integer fromWarehouseId = moveOtherWarehouseDTO.getFromWarehouseId();
        Integer toWarehouseId = moveOtherWarehouseDTO.getToWarehouseId();
        //组织间调拨
        if (!fromOrganizedId.equals(toOrganizedId)) {
            //查看两组之间所归属的仓库是否属实
            if (!judgeRealityWarehouseInfo(fromOrganizedId,fromWarehouseId,toOrganizedId,toWarehouseId)){
                log.error("组织仓库信息不属实");
                throw new RuntimeException("组织仓库信息不属实");
            }
            //开始调拨



            //判断是要进行区位调拨还是库位调拨



            //更新需要调拨的物料



        }
        //组织内调拨
        else {

        }


    }

    private Boolean judgeRealityWarehouseInfo(Integer fromOrganizedId, Integer fromWarehouseId, Integer toOrganizedId, Integer toWarehouseId) {

    }
}
