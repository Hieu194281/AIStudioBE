package vn.dasvision.template.api.requestbody;

import lombok.*;
import vn.dasvision.template.config.untils.ConstantMessages;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditUserRequest {
    private String userAvatar = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userDateOfBirth = ConstantMessages.NOT_UPDATE_DATE_STRING;
    private String userAddress = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String userPhoneNumber = ConstantMessages.NOT_UPDATE_CHARACTER;
}
