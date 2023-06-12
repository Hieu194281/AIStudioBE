package vn.dasvision.template.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import vn.dasvision.template.persistence.dto.UserLogin;
import vn.dasvision.template.persistence.dto.User;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper {
    List<User> listByKeyword(Map<String, Object> reqMap) throws Exception;

    List<User> list(Map<String, Object> reqMap) throws Exception;

    int total(Map<String, Object> reqMap) throws Exception;

    List<User> getListInitUser(Map<String,Object> reqMap) throws Exception;

    int totalInitUser(Map<String,Object> reqMap) throws Exception;

    List<User> getListInitUserByKeyword(Map<String,Object> reqMap) throws Exception;

    void addUser(Map<String, Object> user) throws Exception;

    void updateUser(Map<String, Object> reqMap) throws Exception;
    void editUser(Map<String, Object> reqMap) throws Exception;

    User getUserById(Map<String, Object> reqMap) throws Exception;
    void changePassword(Map<String, Object> reqMap) throws Exception;

    void resetPassword(Map<String, Object> reqMap) throws Exception;

    User getInitUserByEmail(Map<String, Object> reqMap) throws Exception;
    User getUserByEmail(Map<String, Object> reqMap) throws Exception;

    User getUserByUserRoleId(Map<String, Object> reqMap) throws Exception;

    User getAllUserByEmail(Map<String, Object> reqMap) throws Exception;

    User getUserAndInitUserById(Map<String, Object> reqMap) throws Exception;


    User getInitUserByEmailAndInitPassword(Map<String, Object> reqMap) throws Exception;



    void deleteUser(Map<String, Object> reqMap) throws Exception;

    User checkLoginUserEmailUserPassword(Map<String, Object> reqMap) throws Exception;

    User checkLoginUserNameInitUserPassword(Map<String, Object> reqMap) throws Exception;


    UserLogin login(Map<String, Object> reqMap) throws Exception;
    void loginSuccess(Map<String, Object> reqMap) throws Exception;
    void loginFailedUpdate(Map<String, Object> reqMap) throws Exception;

    void lockUser(Map<String, Object> reqMap) throws Exception;



}
