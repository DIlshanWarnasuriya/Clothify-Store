package edu.icet.project.dto;
import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Orders {
    private Integer id;
    @NonNull private Integer customerId;
    @NonNull private Integer userId;
    @NonNull private String date;
    @NonNull private String paymentMethod;
    @NonNull private String status;
}
