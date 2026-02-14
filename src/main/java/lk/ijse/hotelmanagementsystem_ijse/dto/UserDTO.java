package lk.ijse.hotelmanagementsystem_ijse.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    private String user_id;
    private String user_name;
    private String password;
    private String email;
    private String job_role;
    private String status;
}
