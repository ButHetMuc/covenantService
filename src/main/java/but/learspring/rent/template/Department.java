package but.learspring.rent.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    private String _id;
    private int creatorId;
    private String name;
    private Address address;
    private long price;
    private String description;
    private int area;
    private ArrayList<String> image;
    private String deleteAt;
    private boolean deleted;
    private boolean isSold;
    private String createAt;
    private String updateAt;

}
