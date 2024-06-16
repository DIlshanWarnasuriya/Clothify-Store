package edu.icet.project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "ordersDetails")
public class OrdersDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private Integer qty;
    private Double total;
    private String imageUrl;
    private String status;
}
