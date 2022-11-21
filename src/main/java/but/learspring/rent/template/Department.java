package but.learspring.rent.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private Long departmentId;
    private String ownerId;
    private String name;
    private String address;
    private int price;
    private String description;
    private int area;
    private String image;

}
