package vn.dasvision.template.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import vn.dasvision.template.persistence.dto.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Inquiring the User MyBatis Mapper
 * @author anhlh
 */

@Mapper
public interface UserRoleMapper {
    /**
     * Inquiring the User Role
     * @param
     * @return
     * @throws Exception
     */
    List<UserRole> list (Map<String, Object> reqMap) throws Exception;

    /**
     * Inquiring the User Role
     * @param
     * @return
     * @throws Exception
     */
    int insert ( Map <String, Object> reqMap) throws Exception;

    /**
     * Inquiring the User Role
     * @return
     * @throws Exception
     */
    int total (Map<String, Object> reqMap) throws Exception;

    /**
     * Inquiring the User Role
     * @return
     * @throws Exception
     */
    int totalById (Map<String, Object> reqMap) throws Exception;
    /**
     *
     * @param reqMap
     * @return
     * @throws Exception
     */
    List<UserRole> listByKeyword ( Map<String, Object> reqMap) throws Exception;

    UserRole getUserRoleByRoleName(Map<String, Object> reqMap) throws Exception;

    UserRole getUserRoleById(Map<String, Object> reqMap) throws Exception;

    void deleteUserRole(Map<String, Object> reqMap) throws Exception;

    void updateUserRole(Map<String, Object> reqMap) throws Exception;




}
