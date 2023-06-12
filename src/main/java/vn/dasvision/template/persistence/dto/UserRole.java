package vn.dasvision.template.persistence.dto;

import lombok.*;

import javax.persistence.Lob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRole {
    private int id;
    private String roleName;
    private String roleDesc;
    private String createDate;
    private String modifyDate;
    private String icon;
    private int roleState;
}
