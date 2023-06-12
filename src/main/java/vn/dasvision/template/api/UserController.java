package vn.dasvision.template.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.dasvision.template.api.requestbody.EditUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UserRequest;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.config.untils.JwtTokenUtil;
import vn.dasvision.template.persistence.dto.User;
import vn.dasvision.template.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isNumeric;

@Tag(name = "User Controller", description = "Controller for functions related to  user , such as searching for user")
@RestController
@Slf4j
@RequestMapping(value = "/v1/user")
public class UserController {
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> getListUser(
            @RequestParam(name = "pageSize", required = false) String pageSize,
            @RequestParam(name = "currentPage", required = false) String currentPage,
            @RequestParam(name = "keyword", required = false) String keyword
    ) throws Exception {
        log.info("UserController.getListUser start !!!");
        log.info("==============================");
        Map<String, Object> resError = new HashMap<>();
        JSONObject res;
        log.info("### Request : ");
        if (pageSize == null && currentPage == null) {
            Map<String, Object> users = userService.getListUser(0, 0, keyword);
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
            Map<String, Object> users = userService.getListUser(pageSizeInt, currentPageInt, keyword);
            res = new JSONObject(users);
        }
        log.info("==============================");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> getUser(@PathVariable String id) throws Exception {
        log.info("UserController.getUser start !!!");
        log.info("==============================");
        Map<String, Object> res = new HashMap<>();


        if (!isNumeric(id)) {
            res.put("message", ConstantMessages.VALUE_NOT_BE_OBTAINED);
            res.put("isError", true);
            res.put("errors", ConstantMessages.ID_NOT_BE_CHARACTER);
        }

        res = userService.getUserById(Integer.parseInt(id));

        if (res.containsKey("errors") == true) {
            res.put("isError", true);

            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            res.put("isError", false);
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }

    }

//    @GetMapping("/list_inviting_user")
//    @SecurityRequirement(name = "Authorization")
//    public ResponseEntity<Object> getListInvitingUser(
//            @RequestParam(name = "pageSize", required = false) String pageSize,
//            @RequestParam(name = "currentPage", required = false) String currentPage,
//            @RequestParam(name = "keyword", required = false) String keyword
//    ) throws Exception {
//        log.info("UserController.getListInvitingUser start !!!");
//        log.info("==============================");
//        Map<String, Object> resError = new HashMap<>();
//        JSONObject res;
//        log.info("### Request : ");
//        if (pageSize == null && currentPage == null) {
//            Map<String, Object> users = userService.getListInitUser(0, 0, keyword);
//            res = new JSONObject(users);
//            return new ResponseEntity<>(res, HttpStatus.OK);
//        }
//        if (!isNumeric(pageSize)) {
//            resError.put("message", ConstantMessages.VALUE_NOT_BE_OBTAINED);
//            resError.put("errors", ConstantMessages.PAGE_SIZE_NOT_BE_CHARACTER);
//            res = new JSONObject(resError);
//        } else if (!isNumeric(currentPage)) {
//            resError.put("message", ConstantMessages.VALUE_NOT_BE_OBTAINED);
//            resError.put("errors", ConstantMessages.CURRENT_PAGE_NOT_BE_CHARACTER);
//            res = new JSONObject(resError);
//        } else {
//            int pageSizeInt = Integer.parseInt(pageSize);
//            int currentPageInt = Integer.parseInt(currentPage);
//            Map<String, Object> users = userService.getListInitUser(pageSizeInt, currentPageInt, keyword);
//            res = new JSONObject(users);
//        }
//        log.info("==============================");
//        return new ResponseEntity<>(res, HttpStatus.OK);
//
//
//
//    }

    @PostMapping("/add")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> addUser(
            @RequestBody UserRequest userRequest
    ) throws Exception {
        Map<String, Object> res = new HashMap<>();
//        Map<String, Object> resErr = new HashMap<>();  // if incorrect input
//        Map<String, Object> errors = new HashMap<>();  // save error


//        Map<String, Object> checkUserRequestBody = userService.checkUserRequestBody(userRequest, "0");
//
//        if (checkUserRequestBody.containsKey("errors") == true) {
//            return new ResponseEntity<>(new JSONObject(checkUserRequestBody), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
//        }
//
//
//        User user = new User();
//        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
//        user.setUserEmail(userRequest.getUserEmail());
//        user.setUserAddress(userRequest.getUserAddress());
//        user.setUserAvatar(userRequest.getUserAvatar());
//        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
//        if (Objects.equals(userRequest.getUserDateOfBirth(), null) ) {
//            user.setUserDateOfBirth(null);
//        }else {
//            user.setUserDateOfBirth(userService.convertStringToDate(userRequest.getUserDateOfBirth()));
//        }
//        user.setUserFirstName(userRequest.getUserFirstName());
//        user.setUserLastName(userRequest.getUserLastName());
//        user.setUserPhoneNumber(userRequest.getUserPhoneNumber());



        res = userService.addUser(userRequest);

        if (res.containsKey("errors") == true) {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }


    }
    @PutMapping("/update/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> updataUser(
            @RequestBody UpdateUserRequest userRequest,
            @PathVariable(name = "id", required = true) String id


    ) throws Exception {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();  // save error

//        Map<String, Object> checkUserRequestBody = userService.checkUpdateUserRequestBody(userRequest, id);
//
//        if (checkUserRequestBody.containsKey("errors") == true) {
//            return new ResponseEntity<>(new JSONObject(checkUserRequestBody), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
//        }
//
//        User user = new User();
//        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
//
//        user.setId(Integer.parseInt(id));
//        user.setUserEmail(userRequest.getUserEmail());
//        user.setUserAddress(userRequest.getUserAddress());
//        user.setUserAvatar(userRequest.getUserAvatar());
//        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
//        if (Objects.equals(userRequest.getUserDateOfBirth(), null) ) {
//            user.setUserDateOfBirth(null);
//        }else {
//            user.setUserDateOfBirth(userService.convertStringToDate(userRequest.getUserDateOfBirth()));
//        }        user.setUserFirstName(userRequest.getUserFirstName());
//        user.setUserLastName(userRequest.getUserLastName());
//        user.setUserPhoneNumber(userRequest.getUserPhoneNumber());
        res = userService.updateUser(userRequest , id);

        if (res.containsKey("errors") == true) {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }


    }

    @PutMapping("/edit/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> editUser(
            @RequestBody EditUserRequest editUserRequest,
            @PathVariable(name = "id", required = true) String id


    ) throws Exception {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();  // save error

        res = userService.editUser(editUserRequest , id);

        if (res.containsKey("errors") == true) {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.UNPROCESSABLE_ENTITY);  // 422
        } else {
            return new ResponseEntity<>(new JSONObject(res), HttpStatus.OK);
        }


    }

    @PutMapping("/change_password")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> changePassword(
            HttpServletRequest request,
            @RequestParam(name = "oldPassword" ,required = true) String oldPassword,
            @RequestParam(name = "newPassword", required = true) String newPassword,
            @RequestParam(name = "confirmPassword", required = true) String confirmPassword

    ) throws Exception{
        String jwt = request.getHeader("Authorization");
        String email = jwtTokenUtil.getClaimsFromToken(jwt).get("email").toString();

        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();  // if incorrect input
        Map<String, Object> errors = new HashMap<>();  // save error
        boolean exitError = false;
        if(email == null || userService.checkEmail(email)== false){
            errors.put("email", "Email incorrect");
            resErr.put("errors", errors);
            exitError = true;
        }
        if (oldPassword == null || userService.checkPassword(oldPassword) == false){
            errors.put("oldPassword", "oldPassword incorrect");
            resErr.put("errors", errors);
            exitError = true;
        }
        if (newPassword == null || userService.checkPassword(newPassword) == false) {
            errors.put("newPassword", "newPassword incorrect ");
            resErr.put("errors", errors);
            exitError = true;
        }
        if (Objects.equals(newPassword, oldPassword)){
            errors.put("password", "newPassword must different oldPassword ");
            resErr.put("errors", errors);
            exitError = true;
        }
        if(Objects.equals(newPassword,confirmPassword)== false){
            errors.put("password", "confirmPassword  different newPassword ");
            resErr.put("errors", errors);
            exitError = true;
        }


        if (exitError == true) {
            res.put("message", "The given data was invalid, pasword must more 8 character, have 4 type character ([0->9],[a->z],[A->Z],[!@#$%^&*)(])");
            res.put("errors", resErr);
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        res = userService.changePassword(email, oldPassword, newPassword);
        if(res.containsKey("errors")){
//            userService.logout(jwt);
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);

        }
        userService.logout(jwt);
        return new ResponseEntity<>(res, HttpStatus.OK);




    }

    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> deleteUser(
            @PathVariable(name = "id", required = false) String id
    ) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        if(id == null){
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);
            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isNumeric(id) == false){
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);

            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);

        }

        res = userService.deleteUser(Integer.parseInt(id));
        if(res.containsKey("errors")){
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);

    }
//    @GetMapping("/checkpass/{password}")
//    public ResponseEntity<Object> check(
//            @PathVariable(name = "password", required = false) String password
//    )throws  Exception{
//        log.info(password);
//
////        return new ResponseEntity<>(userService.sha256(password),HttpStatus.OK);
//        if(userService.checkPassword(password)== true){
//            return new ResponseEntity<>("ok",HttpStatus.OK);
//        } else return  new ResponseEntity<>("fail",HttpStatus.OK);
//
//    }

    @PutMapping("/reset_password/{id}")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Object> resertPassword(
            @PathVariable(name = "id") String id
    ) throws Exception{


        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        boolean exitError =false;
        if(id == null){
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);
            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (isNumeric(id) == false){
            res.put("message", "inputInvalid");
            errors.put("id", "id must be numberic");
            res.put("errors", errors);

            return new ResponseEntity<>(res,HttpStatus.UNPROCESSABLE_ENTITY);

        }

        res = userService.resetPassword(Integer.parseInt(id));
        if(res.containsKey("errors")){
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);

    }


}
