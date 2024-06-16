package edu.icet.project.dto.table;

import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class OrderDetailTable {
    private Integer id;
    private ImageView imageUrl;
    private Integer productId;
    private Integer qty;
    private Double total;
    private String status;
}
