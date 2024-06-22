package edu.icet.project.dto;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class OrdersDetails {
    private Integer id;
    @NonNull private Integer orderId;
    @NonNull private Integer productId;
    @NonNull private String productName;
    @NonNull private Integer qty;
    @NonNull private Double total;
    @NonNull private String imageUrl;
    @NonNull private String status;
}
