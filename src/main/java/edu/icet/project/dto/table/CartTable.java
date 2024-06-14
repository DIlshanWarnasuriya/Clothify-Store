package edu.icet.project.dto.table;

import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CartTable {
    private ImageView imageUrl;
    private Integer id;
    private String name;
    private String size;
    private Integer qty;
    private Double total;

}
