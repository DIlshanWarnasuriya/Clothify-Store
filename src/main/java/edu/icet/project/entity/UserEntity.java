package edu.icet.project.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String address;
    @Column(unique = true, nullable = false)
    private String contactNo;
    private String gender;
    @Column(nullable = false)
    private String userType;
    private String imageUrl;
    private String status;
}
