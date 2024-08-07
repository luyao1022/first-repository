package jsola.com.txy.pojo;


import lombok.Data;

@Data
public class Warehouse {
private Integer id;
private String name;
private Integer organizationId;
private Boolean hasLocation;
private Boolean readyArea;
}
