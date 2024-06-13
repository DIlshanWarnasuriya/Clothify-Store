package edu.icet.project.dto;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Supplier {
    private Integer id;
    @NonNull private String name;
    @NonNull private String email;
    @NonNull private String company;
    @NonNull private String contactNo;
    @NonNull private String gender;
    @NonNull private String imageUrl;
    @NonNull private String status;
}
