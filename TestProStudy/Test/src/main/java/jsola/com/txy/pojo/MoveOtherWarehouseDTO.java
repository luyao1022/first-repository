package jsola.com.txy.pojo;


import lombok.Data;



@Data
public class MoveOtherWarehouseDTO {
    private Integer fromOrganizedId;
    private Integer toOrganizedId;
    private Integer fromWarehouseId;
    private Integer toWarehouseId;
    private Boolean flag;//用于判断同种物料在库位余量不足时是否需要进入同一个库位,不同物料默认不放入同一库位
}
