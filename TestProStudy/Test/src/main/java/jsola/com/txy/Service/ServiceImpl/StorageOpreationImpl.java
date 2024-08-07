package jsola.com.txy.Service.ServiceImpl;

import jsola.com.txy.Mapper.CommonMapper;
import jsola.com.txy.Mapper.StorageOpreationMapper;
import jsola.com.txy.Service.StorageOpreationService;
import jsola.com.txy.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StorageOpreationImpl implements StorageOpreationService {
    @Autowired
    CommonMapper commonMapper;
    @Autowired
    StorageOpreationMapper storageOpreationMapper;
    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 添加仓库
     * @param warehouse
     * @return
     */
    @Override
    @Transactional
    public Warehouse saveWarehouse(WarehouseDTO warehouse) {
        if (Objects.isNull(warehouse)) {
            log.error("warehouse为null");
            throw new RuntimeException("warehouse信息非法");
        }
        //判断组织是否合法
        Organization organization = commonMapper.getOrganzation(warehouse.getOrganizationId());
        if (Objects.isNull(organization)) {
            log.error("organization为null");
            throw new RuntimeException("organization非法");
        }

        Integer organizationId = warehouse.getOrganizationId();
        Boolean hasLocation = warehouse.getHasLocation();
        String name = warehouse.getName();
        //添加仓库
        return storageOpreationMapper.addWarehouse(organizationId, hasLocation, name);
    }

    /**
     * 为仓库分区位
     *
     * @param warehouseId
     * @param countArea   需要分区的个数
     * @return
     */
    @Override
    @Transactional
    public List<StorageArea> saveAreaForWarehouse(Integer warehouseId, Integer countArea) {
        //查询warehouse
        Warehouse warehouse = storageOpreationMapper.getWarehouse(warehouseId);
        //判断warehouse是否合法
        if (Objects.isNull(warehouse)) {
            log.error("此仓库不存在");
            throw new RuntimeException("仓库非法");
        }
        //判断此仓库是否已经分区
        if (warehouse.getReadyArea()) {
            log.error("仓库已分区");
            throw new RuntimeException("仓库已经分区");
        }
        //根据countArea开始分区
        ArrayList<String> areaNames = new ArrayList<>();
        for (Integer i = 0; i < countArea; i++) {
            areaNames.add("库区" + i);
        }
        storageOpreationMapper.addArea(areaNames, warehouseId);
        //分区结束，为仓库做标记
        storageOpreationMapper.setReadyArea(warehouseId);
        //查询所有分区
        return  storageOpreationMapper.getAllAreaByWarehouseId(warehouseId);
    }

    /**
     * 为区分位
     * @param areaId
     * @param countLocation
     * @return
     */
    @Override
    @Transactional
    public List<StorageLocation> saveLocationForArea(Integer areaId, Integer countLocation) {
        //判断所属仓库是否可以分库
      Warehouse warehouse =   storageOpreationMapper.getBelongToWarehouse(areaId);
      //此仓库不允许分库
      if (!warehouse.getHasLocation()){
          log.error("此仓库为不分库仓库");
          throw new RuntimeException("此仓库为不分库仓库");
      }
        //判断此区存不存在
       StorageArea storageArea =  storageOpreationMapper.getAreaById(areaId);
       //如果此区存在就进行分库
        if (Objects.isNull(storageArea)){
            log.error("此区不存在");
            throw new RuntimeException("areaId异常");
        }
        //判断是否已经分库
       if (storageArea.getReadyLocation()){
           log.error("此区已分库");
           throw new RuntimeException("此区已分库");
       }
        ArrayList<String> locationName  = new ArrayList<>();
        //进行分库
        for (Integer i = 0; i < countLocation; i++) {
            locationName.add("库"+i);
        }
        storageOpreationMapper.AddLocation(areaId,locationName);
        //分区结束，为区位做标记
        storageOpreationMapper.setReadyLocation(areaId);
        //查询所有分库
        List<StorageLocation> storageLocations = storageOpreationMapper.getAllLocationByAreaId(areaId);
        return storageLocations;
    }

    /**
     * 删除仓库
     * @param wareHouseId
     */
    @Override
    @Transactional
    public void deleteWarehouseByWarehouseId(Integer wareHouseId) {
        //判断该仓库是否存在
        Warehouse warehouse = storageOpreationMapper.getWarehouseByWarehouseId(wareHouseId);
        if (Objects.isNull(warehouse)){
            log.error("该仓库不存在");
            throw new RuntimeException("该仓库不存在");
        }
        try {
            //1找出该仓库的区位和库位

            //2区位
            List<StorageArea> allAreaByWarehouseId = storageOpreationMapper.getAllAreaByWarehouseId(warehouse.getId());

            //3收集所有的区位id
            List<Integer> collect = allAreaByWarehouseId.stream().map(StorageArea::getId).collect(Collectors.toList());

            //4根据区位id查找所有的库位id
            List<Integer> AllLocationId =   storageOpreationMapper.getAllLocationByAreaIdList(collect);

            //根据库位id进行删除
            storageOpreationMapper.deleteByLocationIdList(AllLocationId);
            //根据区位id进行删除
            storageOpreationMapper.deleteByAreaIdList(collect);
            //删除仓库
            storageOpreationMapper.deleteByWarehouseId(wareHouseId);
        } catch (Exception e) {
            log.error("仓库删除异常");
            throw new RuntimeException(e);
        }
    }

    /**
     * 对所有的warehouse进行查询
     * @return
     */
    @Override
    public List<WarehouseVO> getAllWarehouse() {
        //TODO 实现对redis的查询
        //redisTemplate.opsForValue();
        //查询数据库
        List<WarehouseVO> warehouseVOS = null;
        try {
            warehouseVOS = storageOpreationMapper.selectAllWarehouses();
        } catch (Exception e) {
            log.error("仓库查询失败");
            throw new RuntimeException(e);
        }
        return warehouseVOS;
    }

    /**
     * 根据warehouseId查询warehouse
     * @param id
     * @return
     */
    @Override
    public WarehouseVO getWarehouseById(Integer id) {
       WarehouseVO warehouse =  storageOpreationMapper.getWarehouseById(id);
        return warehouse;
    }
}
