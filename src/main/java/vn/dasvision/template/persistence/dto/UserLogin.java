package vn.dasvision.template.persistence.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserLogin {
    private int id;
    private String UserEmail;
    private String userRoleName;
    private String userInitPassword;
    private String userPassword;
    private int userState;
    private Date userLastTimeChangedPassword;
    private int userNumberLoginAttempts;

}
