package but.learspring.rent.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private Long id;
    private Long covenantId;
    private Instant paymentDate;
    private String paymentStatus;
    private long amount;
}