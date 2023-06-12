package vn.dasvision.template.service;

import org.springframework.stereotype.Service;

import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRoleRequest;
import vn.dasvision.template.api.requestbody.UserRoleRequest;
import vn.dasvision.template.persistence.dto.UserRole;

import java.util.Map;

/**
 * User Role Service
 * @author anhlh
 */
@Service
public interface UserRoleService {
    /**
     * Inquire the consultation contents data.
     * @param userRole
     * @return
     * @throws Exception
     */
    Map<String, Object> addUserRole(UserRoleRequest userRoleRequest) throws Exception;

    /**
     * Inquire the consultation contents data.
     * @return
     * @throws Exception
     */
    Map<String, Object> getListUserRole(int pageSize, int currentPage, String keyword) throws Exception;

    Map<String, Object> getListAllUserRole() throws Exception;

    Map<String, Object> getUserRoleById(int id) throws Exception;
//    List<String> updateUserRole(UserRole userRole) throws Exception;
    Map<String, Object> deleteUserRole(int userRoleId) throws Exception;

//    Map<String, Object> deleteUserRole(int userRoleId) throws Exception;


//    boolean exitUserRoleName(String roleName) throws Exception;
//
    Map<String, Object> updateUserRole(UpdateUserRoleRequest userRequest, String id) throws Exception;

    Map<String, Object> checkAddUserRoleRequestBody(UserRoleRequest userRole) throws Exception;

    Map<String, Object> checkUpdateUserRoleRequestBody(UpdateUserRoleRequest userRole, String id) throws Exception;



}
