package but.learspring.rent.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long paymentId;
    private String paymentStatus;
    private String paymentMode;
    private Long amount;
    private Instant paymentDate;
    private Long covenantId;

}