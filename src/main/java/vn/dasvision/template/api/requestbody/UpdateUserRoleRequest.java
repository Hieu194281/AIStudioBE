package vn.dasvision.template.api.requestbody;

import lombok.*;
import vn.dasvision.template.config.untils.ConstantMessages;

import javax.persistence.Lob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRoleRequest {
    private String roleName  = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String roleDesc = ConstantMessages.NOT_UPDATE_CHARACTER;
    private String icon = ConstantMessages.NOT_UPDATE_CHARACTER;
}
