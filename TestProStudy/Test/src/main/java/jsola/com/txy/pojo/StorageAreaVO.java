package jsola.com.txy.pojo;

import lombok.Data;

import java.util.List;

@Data
public class StorageAreaVO {
    private Integer id;
    private String name;
    private Integer warehouseId;
    private Boolean readyLocation;
    private List<StorageLocation> storageLocations;
}
