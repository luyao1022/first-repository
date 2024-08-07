package jsola.com.txy.pojo;


import lombok.Data;

import java.util.List;

@Data
public class WarehouseVO {
    private Integer id;
    private String name;
    private Integer organizationId;
    private Boolean hasLocation;
    private Boolean readyArea;
    private List<StorageAreaVO> storageAreaVOS;
}
