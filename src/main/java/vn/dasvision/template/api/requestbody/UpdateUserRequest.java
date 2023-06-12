package vn.dasvision.template.api.requestbody;

import lombok.*;
import vn.dasvision.template.config.untils.ConstantMessages;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRequest {
    private String userRoleId = ConstantMessages.NOT_UPDATE_ID;
    private String userEmail = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userAvatar = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userFirstName = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userLastName = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userDateOfBirth = ConstantMessages.NOT_UPDATE_DATE_STRING;
    private String userAddress = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userPhoneNumber = ConstantMessages.NOT_UPDATE_CHARACTER;

}
