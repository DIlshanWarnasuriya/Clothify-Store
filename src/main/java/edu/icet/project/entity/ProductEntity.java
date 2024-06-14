package edu.icet.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String size;
    private Double price;
    private Integer qty;
    private String imageUrl;
    private String category;
    private Integer supplierId;
    private Integer userId;
    private Date date;
    private String status;
}
