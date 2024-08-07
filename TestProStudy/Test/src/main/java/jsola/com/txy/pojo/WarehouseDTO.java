package jsola.com.txy.pojo;


import lombok.Data;

@Data
public class WarehouseDTO {
    private String name;
    private Integer organizationId;
    private Boolean hasLocation;
}
