package vn.dasvision.template.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.dasvision.template.api.requestbody.EditUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UserRequest;
import vn.dasvision.template.persistence.dto.User;

import java.security.Signature;
import java.util.Map;
@Service
public interface UserService {
    Map<String, Object> getListUser(int pageSize , int currentPage, String keyword) throws Exception;

    Map<String, Object> getUserById(int id) throws Exception;

//    Map<String, Object> getListInitUser(int currentPage, int pageSize, String keyword) throws Exception;

    boolean checkPassword(String password) throws Exception;

    boolean checkEmail(String email) throws Exception;

    boolean checkPhoneNumber(String phoneNumber) throws  Exception;

    boolean checkDateOfBirth(String dateOfBirth) throws Exception;

    boolean checkAvatar(String avatar) throws Exception;

    Map<String,Object> addUser(UserRequest user) throws Exception;

    java.sql.Date convertStringToDate(String date) throws  Exception;

//    String sha256(String input) throws  Exception;

    Map<String,Object> changePassword(String email, String oldPassword, String newPassword) throws Exception;
    Map<String,Object> login(String email, String password) throws Exception;
    Map<String, Object> logout(String jwt) throws Exception;

    Map<String,Object> deleteUser(int id) throws Exception;

    Map<String,Object> resetPassword(int id) throws Exception;

    Map<String,Object> updateUser(UpdateUserRequest userRequest, String id) throws Exception;

    Map<String,Object> editUser(EditUserRequest editUserRequest, String id) throws Exception;


    Map<String,Object> checkUserRequestBody(UserRequest userRequest, String id) throws Exception;

    Map<String,Object> checkUpdateUserRequestBody(UpdateUserRequest userRequest, String id) throws Exception;

}
