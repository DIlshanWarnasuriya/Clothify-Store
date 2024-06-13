package edu.icet.project.dto.table;

import javafx.scene.shape.Circle;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class CustomerTable {
    private Circle imageUrl;
    private Integer id;
    private String name;
    private String contactNo;
    private String email;
    private String address;
}
