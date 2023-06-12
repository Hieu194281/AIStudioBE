package vn.dasvision.template.api.requestbody;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Lob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRoleRequest {
    private String roleName;
    private String roleDesc;
    private String icon;
}
