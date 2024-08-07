package jsola.com.txy.pojo;


import lombok.Data;

@Data
//移位需要物料id 移去的库区id或者移去的库位id
public class MoveMaterialToOtherDTO {
    private Integer materialId;
    private Integer MoveToLocationId;
    private Integer MoveToAreaId;
    private Integer organizedId;
    private Boolean flag; //用于判断同种物料在库位余量不足时是否需要进入同一个库位,不同物料默认不放入同一库位
    private Integer warehouseId;
}
