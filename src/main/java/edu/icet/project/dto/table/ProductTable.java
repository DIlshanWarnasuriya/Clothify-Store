package edu.icet.project.dto.table;

import javafx.scene.image.ImageView;
import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class ProductTable {
    private ImageView image;
    private Integer id;
    private String name;
    private Integer qty;
    private String size;
    private Double price;
    private String category;
    private String date;
    private Integer supplierId;
}
