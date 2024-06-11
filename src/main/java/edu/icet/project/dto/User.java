package edu.icet.project.dto;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Integer id;
    @NonNull private String name;
    @NonNull private String email;
    @NonNull private String password;
    @NonNull private String address;
    @NonNull private String contactNo;
    @NonNull private String gender;
    @NonNull private String userType;
    @NonNull private String imageUrl;
    @NonNull private String status;
}
