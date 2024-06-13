package edu.icet.project.dto.table;

import javafx.scene.shape.Circle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class SupplierTable {
    private Circle image;
    private Integer id;
    private String name;
    private String contactNo;
    private String email;
    private String company;
}
