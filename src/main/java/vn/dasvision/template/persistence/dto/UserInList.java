package vn.dasvision.template.persistence.dto;

import lombok.*;

import javax.persistence.Lob;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserInList {
    private int id;
    private String userRoleName;
    private String userEmail;
    private String userAvatar;
    private String userFirstName;
    private String userLastName;
    private Date userDateOfBirth;
    private String userAddress;
    private String userPhoneNumber;
    private String userInitPassword;
//    private String userPassword;
    private int userState;
    private  String createDate;
    private String modifyDate;
}
