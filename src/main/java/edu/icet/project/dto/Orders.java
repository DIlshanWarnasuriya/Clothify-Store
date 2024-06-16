package edu.icet.project.dto;

import lombok.*;
import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Orders {
    private Integer id;
    @NonNull private Integer customerId;
    @NonNull private Date date;
    @NonNull private String paymentMethod;
}
