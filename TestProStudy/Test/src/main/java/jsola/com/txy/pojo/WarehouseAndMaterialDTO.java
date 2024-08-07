package jsola.com.txy.pojo;


import lombok.Data;

@Data
public class WarehouseAndMaterialDTO {
    private Integer warehouseId;
    private String name;
    private String description;
    private Integer organizationId;
    private Integer areaId;
    private Integer locationId;
    private Boolean falg;  //用于判断同种物料在库位余量不足时是否需要进入同一个库位,不同物料默认不放入同一库位
    private Integer materialId;
}
