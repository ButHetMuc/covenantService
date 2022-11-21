package but.learspring.rent.template;

import but.learspring.rent.model.Covenant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTemplate {
    private Covenant covenant;
    private Department department;
    private Users user;
    private Payment payment;
}
