package vn.dasvision.template.api.requestbody;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRequest {
    private String userRoleId;
    private String userEmail;
    private String userAvatar;
    private String userFirstName;
    private String userLastName;
    private String userDateOfBirth;
    private String userAddress;
    private String userPhoneNumber;

}
