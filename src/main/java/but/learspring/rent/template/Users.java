package but.learspring.rent.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Long id;
    private String lastLogin;
    private String username;
    private String first_name;
    private String last_name;
    private boolean is_active;
    private String date_joined;
    private String email;
    private String phone;
    private String date_of_birth;
    private String gender;
}
