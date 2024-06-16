package edu.icet.project.dto.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class OrderTable {
    private Integer id;
    private Integer customerId;
    private Integer userId;
    private String date;
    private String paymentMethod;
    private String status;
}
