package jsola.com.txy.pojo;


import lombok.Data;

@Data
public class StorageArea {
    private Integer id;
    private String name;
    private Integer warehouseId;
    private Boolean readyLocation;
}
