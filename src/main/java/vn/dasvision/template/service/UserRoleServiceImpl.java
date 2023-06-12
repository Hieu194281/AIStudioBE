package vn.dasvision.template.service;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRoleRequest;
import vn.dasvision.template.api.requestbody.UserRoleRequest;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.persistence.dto.User;
import vn.dasvision.template.persistence.dto.UserRole;
import vn.dasvision.template.persistence.mapper.UserMapper;
import vn.dasvision.template.persistence.mapper.UserRoleMapper;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNumeric;


/**
 * User Role service implement
 * @author anhlh
 */
@Slf4j
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private final UserRoleMapper userRoleMapper;

    @Autowired
    private final UserMapper userMapper;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper, UserMapper userMapper) {
        this.userRoleMapper = userRoleMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Map<String, Object> getUserRoleById(int id) throws Exception{
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
//        log.info("check id ");
//        log.info(Integer.toString(id));
        reqMap.put("id", id);
        UserRole userRole = userRoleMapper.getUserRoleById(reqMap);
        if (userRole == null){
            res.put("message", "inputInvalid");
            errors.put("id", "id not exit!");
            res.put("errors", errors);
            return res;

        }
        res.put("message", "Successful");
        res.put("data", userRole);
        return res;


    }

    @Override
    public Map<String, Object> getListUserRole(int pageSize, int currentPage, String keyword) throws Exception {
        log.info("UserRoleServiceImpl getUserRoleList Start !!");
        log.info("===================================");

        List<UserRole> retList;
        Map<String, Object> reqInfo = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("keyword", keyword);
        int total = userRoleMapper.total(reqMap);
        int limit;
        int offset;
        try {
            if (pageSize == 0 && currentPage == 0) {
                limit = total;
                offset = 0;
            } else {
                limit = pageSize;
                offset = pageSize * (currentPage - 1);
                if (offset < 0) {
                    return null;
                }
            }
            reqMap.put("limit", limit);
            reqMap.put("offset", offset);
            if (keyword == null) {
                retList = userRoleMapper.list(reqMap);
            } else {
                reqMap.put("keyword", keyword);
                retList = userRoleMapper.listByKeyword(reqMap);
            }
            reqInfo.put("current_page", currentPage);
            reqInfo.put("page_size", pageSize);
            reqInfo.put("data", retList);
            reqInfo.put("total", total);
        } catch (Exception ex) {
            throw ex;
        } finally {
            log.info("===================================");
        }
        return reqInfo;
    }

    @Override
    public Map<String, Object> getListAllUserRole() throws Exception {
        List<UserRole> retList;
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> reqInfo = new HashMap<>();

        int total = userRoleMapper.total(reqMap);


        reqMap.put("limit", total);
        reqMap.put("offset", 0);
        reqMap.put("keyword", null);


        retList = userRoleMapper.list(reqMap);

        for(UserRole userRole : retList){
            userRole.setIcon(null);
            userRole.setCreateDate(null);
            userRole.setModifyDate(null);
        }
        reqInfo.put("total", total);
        reqInfo.put("data", retList);
        return reqInfo;
    }

//    @Override
//    public UserRole getOneUserRole(int id) throws Exception {
//        List<UserRole> retList;
//        return null;
//    }

//    @Override
//    public List<String> updateUserRole(UserRole userRole) throws Exception {
//        return null;
//    }

    @Override
    public Map<String, Object> deleteUserRole(int userRoleId) throws Exception {
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();

        reqMap.put("id", userRoleId);
        UserRole userRole= userRoleMapper.getUserRoleById(reqMap);
//        reqMap.put("userRoleId", userRole.)
        User user = userMapper.getUserByUserRoleId(reqMap);
        // no record userRoleId
        if(userRole == null){
            res.put("message", "inputInvalid");
            errors.put("Id", "idNotExit");
            res.put("errors", errors);
            return  res;
        }else if(user != null){
            res.put("message", "inputInvalid");
            errors.put("Id", "have authorized users");
            res.put("errors", errors);
            return  res;
        }else {
            userRoleMapper.deleteUserRole(reqMap);
            res.put("message", "deleteSucess");
            return res;

        }
    }


    @Override
    public Map<String, Object> addUserRole(UserRoleRequest userRoleRequest) throws Exception {

        UserRole userRole = new UserRole();

        Map<String, Object> checkAddUserRoleRequest = checkAddUserRoleRequestBody(userRoleRequest);
        if (checkAddUserRoleRequest.containsKey("errors") == true) {
            return checkAddUserRoleRequest; // 422
        }

        userRole.setRoleName(userRoleRequest.getRoleName().trim());
        userRole.setRoleDesc(userRoleRequest.getRoleDesc());
        userRole.setIcon(userRoleRequest.getIcon());


        Map<String, Object> res = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();

        reqMap.put("roleName", userRole.getRoleName());
        reqMap.put("roleDesc", userRole.getRoleDesc());
        reqMap.put("icon", userRole.getIcon());

        ;
        // check userRole exit in db
        UserRole checkUserRoleByName = userRoleMapper.getUserRoleByRoleName(reqMap);
        // not record
        if (checkUserRoleByName == null) {
//
            int x = userRoleMapper.insert(reqMap);
            res.put("message", "addUserRoleSuccess");
            return res;

        } else {
            res.put("message", "inputInvalid");
            errors.put("roleName", "RoleNameExit");
            res.put("errors", errors);
            return res;
        }
    }


    @Override
    public Map<String, Object> updateUserRole(UpdateUserRoleRequest userRoleRequest, String id) throws Exception{

// check id
        Map<String, Object> checkAddUserRoleRequest = checkUpdateUserRoleRequestBody(userRoleRequest,id);
        if (checkAddUserRoleRequest.containsKey("errors") == true) {
            return checkAddUserRoleRequest;  // 422
        }

        UserRole userRole = new UserRole();
        userRole.setId(Integer.parseInt(id));
        userRole.setRoleName(userRoleRequest.getRoleName().trim());
        userRole.setRoleDesc(userRoleRequest.getRoleDesc());
        userRole.setIcon(userRoleRequest.getIcon());


        Map<String, Object> res = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();


        reqMap.put("id",userRole.getId());
        reqMap.put("roleName", userRole.getRoleName());
        reqMap.put("roleDesc", userRole.getRoleDesc());
        reqMap.put("roleIcon", userRole.getIcon());

        log.info(Integer.toString(userRole.getId()));

        UserRole userRoleDB  = userRoleMapper.getUserRoleById(reqMap);

        if(userRoleDB == null){
            res.put("message", "inputInvalid");
            errors.put("id", "roleIdNotExit");
            res.put("errors", errors);
            return res;
        }

        if(Objects.equals(userRole.getRoleName(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("roleName", userRoleDB.getRoleName());
        } else if ( Objects.equals(userRoleDB.getRoleName(),userRole.getRoleName())== false){
            // check Duplicate name role name
            if (userRoleMapper.getUserRoleByRoleName(reqMap) == null ){
            } else {
            res.put("message", "inputInvalid");
            errors.put("role name", "duplicate role name");
            res.put("errors", errors);
            return res;
            }
        }
        if(Objects.equals(userRole.getRoleDesc(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("roleDesc", userRoleDB.getRoleName());
        }

        if(Objects.equals(userRole.getIcon(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("roleIcon", userRoleDB.getRoleName());
        }

        userRoleMapper.updateUserRole(reqMap);
        res.put("message", "update user role successful");
        return res;
    }

    @Override
    public Map<String,Object> checkAddUserRoleRequestBody(UserRoleRequest userRoleRequest) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        boolean exitErr = false;
        if(userRoleRequest.getRoleName() == null || userRoleRequest.getRoleName().equals("") ){
            errors.put("roleName", "roleNameNotNull");
            res.put("errors", errors);
            exitErr = true; // 422
        }

        if(userRoleRequest.getIcon() != null && userRoleRequest.getIcon().equals("") == true ){
            errors.put("icon", "icon incorrect format");
            exitErr= true;
        }

        if(userRoleRequest.getIcon() != null ){
	    int dot = userRoleRequest.getIcon().indexOf(",");
	    String icon = userRoleRequest.getIcon().substring(dot+1);
            String iconType = null;
            try {

                byte[] bytes = Base64.getDecoder().decode(icon);
                ByteArrayInputStream  bis = new ByteArrayInputStream(bytes);
                iconType = URLConnection.guessContentTypeFromStream(bis);
            } catch (IOException | IllegalArgumentException e){
                log.info(e.toString());
                iconType = null;
            }
            log.info("icon type");
            log.info(iconType);
            if (iconType == null){
                errors.put("icon", "icon incorrect format");
                exitErr = true; // 422
            }
            else if (Objects.equals(iconType,"image/png") || iconType.equals("image/jpg") || iconType.equals("image/jpeg")){

            }else {
                errors.put("icon", "icon incorrect format");
                exitErr = true; // 422
            }
        }
        if (exitErr){
            res.put("message", "inputInvalid");
            res.put("errors", errors);
            return res;
        }
        else return  new HashMap<>();


    };

    @Override
    public Map<String,Object> checkUpdateUserRoleRequestBody(UpdateUserRoleRequest userRoleRequest, String id) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        boolean exitErr = false;

        if(id == null){
            errors.put("id", "id must be numeric");
            res.put("errors", errors);
            exitErr = true; // 422
        }
        else if(!isNumeric(id)){
            errors.put("id", "id must be numeric");
            res.put("errors", errors);
            exitErr = true; // 422
        }
        if(userRoleRequest.getRoleName() == null || userRoleRequest.getRoleName().equals("") ){
            errors.put("roleName", "roleNameNotNull");
            res.put("errors", errors);
            exitErr = true; // 422
        }

        if(userRoleRequest.getIcon() != null && userRoleRequest.getIcon().equals("") == true ){
            errors.put("icon", "icon incorrect format");
            exitErr= true;
        }

        if(userRoleRequest.getIcon() != null && userRoleRequest.getIcon().equals(ConstantMessages.NOT_UPDATE_CHARACTER) == false ){

		int dot = userRoleRequest.getIcon().indexOf(",");
	    String icon = userRoleRequest.getIcon().substring(dot+1);
            String iconType = null;
            try {

                byte[] bytes = Base64.getDecoder().decode(icon);

                ByteArrayInputStream  bis = new ByteArrayInputStream(bytes);
                 iconType = URLConnection.guessContentTypeFromStream(bis);
            } catch (IOException | IllegalArgumentException e){
                iconType = null;
            }
            log.info("icon type");
            log.info(iconType);
            if (iconType == null){
                errors.put("icon", "icon incorrect format");
                exitErr = true; // 422
            }
            else if (iconType.equals("image/png") || iconType.equals("image/jpg") || iconType.equals("image/jpeg")){

            }else {
                errors.put("icon", "icon incorrect format");
                errors.put("icon", iconType);
                exitErr = true; // 422
            }
        }
        if (exitErr){
            res.put("message", "inputInvalid");
            res.put("errors", errors);
            return res;
        }

        else return  new HashMap<>();

    }


}
