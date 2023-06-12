package vn.dasvision.template.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRoleRequest;
import vn.dasvision.template.api.requestbody.UserRoleRequest;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.persistence.dto.UserRole;
import vn.dasvision.template.service.UserRoleService;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Tag(name = "User Role Controller", description = "Controller for functions related to  user role, such as searching for role")
@RestController
@Slf4j
@RequestMapping(value="/v1/user_role")
public class UserRoleController {
    private UserRoleService userRoleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @DeleteMapping(value = "/delete/{id}")
    @SecurityRequirement(name = "Authorization")
    public  ResponseEntity<Object> delete(@PathVariable String id) throws  Exception{
        log.info("UserRoleController.delete start !!!");
        log.info("==============================");
        if(id == null){
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> errors = new HashMap<>();
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);

            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);

        }
        if (isNumeric(id) == false){
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> errors = new HashMap<>();
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);
            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);

        }

        Map<String, Object> res = new HashMap<>();
        res = userRoleService.deleteUserRole(Integer.parseInt(id));
        // exit errors
        if(res.containsKey("errors")){
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);
        }else {
            return  new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }

    }
    @GetMapping(value = "/list/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> getUserRole(@PathVariable String id) throws Exception{
        log.info("UserRoleController.getUserRole start !!!");
        log.info("==============================");

        if (id == null ){
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> errors = new HashMap<>();
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);

            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isNumeric(id) == false){
            Map<String, Object> res = new HashMap<>();
            Map<String, Object> errors = new HashMap<>();
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);

            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);

        }
        Map<String, Object> res = new HashMap<>();
        res = userRoleService.getUserRoleById(Integer.parseInt(id));
        // exit errors
        if(res.containsKey("errors")){
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);
        }else {
            Object  userRole = res.get("data");//
            ObjectMapper objectMapper = new ObjectMapper();
            return  new ResponseEntity<>( userRole, HttpStatus.OK);
        }
    }

    @PutMapping(value = "update/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> updateUserRole(
            @RequestBody UpdateUserRoleRequest userRoleRequest,
            @PathVariable String id ) throws Exception{
        log.info("UserRoleController.updateUserRole start !!!");
        log.info("==============================");

//
//// check id
//        Map<String, Object> checkAddUserRoleRequest = userRoleService.checkUpdateUserRoleRequestBody(userRoleRequest,id);
//        if (checkAddUserRoleRequest.containsKey("errors") == true) {
//            return new ResponseEntity<>(new JSONObject(checkAddUserRoleRequest), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
//        }
//
//        UserRole userRole = new UserRole();
//        userRole.setId(Integer.parseInt(id));
//        userRole.setRoleName(userRoleRequest.getRoleName().trim());
//        userRole.setRoleDesc(userRoleRequest.getRoleDesc());
//        userRole.setIcon(userRoleRequest.getIcon());

        Map<String, Object> res = userRoleService.updateUserRole(userRoleRequest, id);
        if (res.containsKey("errors") == true) {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }
    }


    @Operation(summary = "Get List User Role API.", description = "Get list of user roles.")
    @GetMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> getListUserRole(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "currentPage", required = false) String currentPage,
            @RequestParam(name = "keyword", required = false) String keyword
    ) throws Exception {
        log.info("UserRoleController.getListUserRole start !!!");
        log.info("==============================");
        Map<String, Object> resError = new HashMap<>();
        JSONObject res;
        log.info("### Request : ");
        if (pageSize == null && currentPage == null) {
            Map<String, Object> users = userRoleService.getListUserRole(0, 0, keyword);
            res = new JSONObject(users);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        if (!isNumeric(pageSize)) {
            resError.put("message", ConstantMessages.VALUE_NOT_BE_OBTAINED);
            resError.put("errors", ConstantMessages.PAGE_SIZE_NOT_BE_CHARACTER);
            res = new JSONObject(resError);
        } else if (!isNumeric(currentPage)) {
            resError.put("message", ConstantMessages.VALUE_NOT_BE_OBTAINED);
            resError.put("errors", ConstantMessages.CURRENT_PAGE_NOT_BE_CHARACTER);
            res = new JSONObject(resError);
        } else {
            int pageSizeInt = Integer.parseInt(pageSize);
            int currentPageInt = Integer.parseInt(currentPage);
            Map<String, Object> users = userRoleService.getListUserRole(pageSizeInt, currentPageInt, keyword);
            res = new JSONObject(users);
        }
        log.info("==============================");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(path = "/list_all", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> getAllListUserRole (
    ) throws Exception{

        Map<String, Object> users = userRoleService.getListAllUserRole();
        JSONObject res;
        res = new JSONObject(users);
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    // add userRole
    @PostMapping(path = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> addUserRole(
            @RequestBody UserRoleRequest userRoleRequest

    ) throws Exception {
//        UserRole userRole = new UserRole();
//
//        Map<String, Object> checkAddUserRoleRequest = userRoleService.checkAddUserRoleRequestBody(userRoleRequest);
//        if (checkAddUserRoleRequest.containsKey("errors") == true) {
//            return new ResponseEntity<>(new JSONObject(checkAddUserRoleRequest), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
//        }
//
//        userRole.setRoleName(userRoleRequest.getRoleName().trim());
//        userRole.setRoleDesc(userRoleRequest.getRoleDesc());
//        userRole.setIcon(userRoleRequest.getIcon());

        Map<String, Object> res = userRoleService.addUserRole(userRoleRequest);
        if (res.containsKey("errors") == true) {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }
    }



}


