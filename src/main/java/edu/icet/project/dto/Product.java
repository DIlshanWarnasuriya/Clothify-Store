package edu.icet.project.dto;

import lombok.*;
import java.util.Date;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Product {
    private Integer id;
    @NonNull private String name;
    @NonNull private String size;
    @NonNull private Double price;
    @NonNull private Integer qty;
    @NonNull private String imageUrl;
    @NonNull private String category;
    @NonNull private Integer supplierId;
    @NonNull private Integer userId;
    @NonNull private Date date;
    @NonNull private String status;
}
