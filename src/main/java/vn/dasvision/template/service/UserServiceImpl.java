package vn.dasvision.template.service;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import vn.dasvision.template.api.requestbody.EditUserRequest;
import vn.dasvision.template.api.requestbody.UpdateUserRequest;
import vn.dasvision.template.api.requestbody.UserRequest;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.config.untils.JwtTokenUtil;
import vn.dasvision.template.persistence.dto.Config;
import vn.dasvision.template.persistence.dto.UserLogin;
import vn.dasvision.template.persistence.dto.User;
import vn.dasvision.template.persistence.dto.UserRole;
import vn.dasvision.template.persistence.mapper.ConfigMapper;
import vn.dasvision.template.persistence.mapper.UserMapper;
import vn.dasvision.template.persistence.mapper.UserRoleMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.StringUtils.isNumeric;


@Slf4j
@Service
public class UserServiceImpl  implements  UserService{
    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private ConfigMapper configMapper;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RedisTemplate redisTemplate;


    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private  final UserRoleMapper userRoleMapper;

    public UserServiceImpl(UserMapper userMapper, UserRoleMapper userRoleMapper, ConfigMapper configMapper ) throws Exception{
        this.userMapper = userMapper ;
        this.userRoleMapper = userRoleMapper;
        this.configMapper = configMapper;

    };
    public Map<String, Object> getListUser(int pageSize, int currentPage, String keyword) throws Exception {
        log.info("UserRoleServiceImpl getUserRoleList Start !!");
        log.info("===================================");

        List<User> retList;
        Map<String, Object> reqInfo = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("keyword", keyword);
        int total = userMapper.total(reqMap);
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
                retList = userMapper.list(reqMap);
            } else {

                reqMap.put("keyword", keyword);
                retList = userMapper.listByKeyword(reqMap);
            }
            reqInfo.put("current_page", currentPage);
            reqInfo.put("page_size", pageSize);
            reqInfo.put("dataList", retList);
            reqInfo.put("total", total);
            reqInfo.put("isError", false);
            reqInfo.put("message","this is the message");

        } catch (Exception ex) {
            throw ex;
        } finally {
            log.info("===================================");
        }
        return reqInfo;



    }

    public Map<String, Object> getUserById(int id) throws Exception{
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> repMap = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        repMap.put("id", id);
        User user = userMapper.getUserById(repMap);
        // no record userRole
        if(user == null){
            res.put("message", "Not found");
            errors.put("id", "Id does not exist");
            res.put("errors", errors);
            return res;
        }else {
//            res.put("message", "Success");
            res.put("data" , user);
            return res;
        }

    }

//    public Map<String,Object> getListInitUser(int currentPage, int pageSize, String keyword) throws Exception{
//
//        List<User> retList;
//        Map<String, Object> reqInfo = new HashMap<>();
//        Map<String, Object> reqMap = new HashMap<>();
//        reqMap.put("keyword", keyword);
//        int total = userMapper.totalInitUser(reqMap);
//        int limit;
//        int offset;
//        try {
//            if (pageSize == 0 && currentPage == 0) {
//                limit = total;
//                offset = 0;
//            } else {
//                limit = pageSize;
//                offset = pageSize * (currentPage - 1);
//                if (offset < 0) {
//                    return null;
//                }
//            }
//            reqMap.put("limit", limit);
//            reqMap.put("offset", offset);
//
//            if (keyword == null) {
//                retList = userMapper.getListInitUser(reqMap);
//            } else {
//
//                reqMap.put("keyword", keyword);
//                retList = userMapper.getListInitUserByKeyword(reqMap);
//            }
//            reqInfo.put("current_page", currentPage);
//            reqInfo.put("page_size", pageSize);
//            reqInfo.put("dataList", retList);
//            reqInfo.put("total", total);
//
//            reqInfo.put("isError", false);
//            reqInfo.put("message","this is the message");
//
//
//        } catch (Exception ex) {
//            throw ex;
//        } finally {
//            log.info("===================================");
//        }
//        return reqInfo;
//
//
//
//    }

    public boolean checkPassword(String password) throws Exception{
        if (password == null || password.length() < 8){
            return false;
        };

        log.info(password);

        if(password.matches(".*[0-9]+.*") && password.matches(".*[a-z]+.*") && password.matches(".*[A-Z]+.*") && password.matches(".*[!@#$%^&*)(]+.*")){
            return true;
        };
        return false;

    }

    public String newInitPassword(int length) throws  Exception{
        String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
        String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String DIGITS = "0123456789";
        String SPECIAL_CHARS = "!@#$%^&*()";
        Random RANDOM = new SecureRandom();

        String ALL_CHARS = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGITS + SPECIAL_CHARS;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length ; i++) {
            int index = RANDOM.nextInt(ALL_CHARS.length());
            sb.append(ALL_CHARS.charAt(index));
        }


        return sb.toString();
    }

//
    public boolean checkPhoneNumber(String phoneNumber) throws Exception{
        if(phoneNumber == null){
            return true;
        }
        if (phoneNumber.length() != 10 || !isNumeric(phoneNumber)){
            return false;
        } else return true;
    }

    public boolean checkAvatar(String avatar) throws Exception{
        // check format of imageavatar
        String avatarType = null;

        if (avatar != null) {
            try {
                byte[] bytes = Base64.getDecoder().decode(avatar);
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                avatarType = URLConnection.guessContentTypeFromStream(bis);
            } catch (IOException |IllegalArgumentException e){
                log.info(e.toString());
                return false;
            }


            if (Objects.equals(avatarType, "image/png")||Objects.equals(avatarType, "image/jpg")||Objects.equals(avatarType, "image/jpeg")) {
                return true;
            } else  return  false;
        }

        return true;

    }

    public Map<String, Object> addUser(UserRequest userRequest) throws Exception{
        Map<String, Object> checkUserRequestBody = checkUserRequestBody(userRequest, "0");

        if (checkUserRequestBody.containsKey("errors") == true) {
            return checkUserRequestBody;  // 422
        }


        User user = new User();
        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
        user.setUserEmail(userRequest.getUserEmail());
        user.setUserAddress(userRequest.getUserAddress());
        user.setUserAvatar(userRequest.getUserAvatar());
        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
        if (Objects.equals(userRequest.getUserDateOfBirth(), null) ) {
            user.setUserDateOfBirth(null);
        }else {
            user.setUserDateOfBirth(convertStringToDate(userRequest.getUserDateOfBirth()));
        }
        user.setUserFirstName(userRequest.getUserFirstName());
        user.setUserLastName(userRequest.getUserLastName());
        user.setUserPhoneNumber(userRequest.getUserPhoneNumber());

        Map<String, Object> reqUserRoleMap = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();

        boolean add = true;

        reqMap.put("userEmail",user.getUserEmail());
        reqUserRoleMap.put("id", user.getUserRoleId());

        if(userRoleMapper.getUserRoleById(reqUserRoleMap) == null){
//            res.put("message", "Not found");
            errors.put("id", "User Role id does not exist");
//            res.put("errors", errors);
            add = false;
        };

        if(userMapper.getAllUserByEmail(reqMap) != null) {
//            res.put("message", "Not found email");
            errors.put("userEmail", "email exist");
//            res.put("errors", errors);
            add = false;
        }

        if(add == true ){
            String userInitPassword = newInitPassword(8);
            while ( checkPassword(userInitPassword) == false){
                userInitPassword = newInitPassword(8);
            }
            reqMap.put("userInitPassword", userInitPassword);
            reqMap.put("userRoleId", user.getUserRoleId());
            reqMap.put("userEmail",user.getUserEmail());
            reqMap.put("userAddress",user.getUserAddress());
            reqMap.put("userAvatar",user.getUserAvatar());
            reqMap.put("userDateOfBirth",user.getUserDateOfBirth());
            reqMap.put("userFirstName",user.getUserFirstName());
            reqMap.put("userLastName",user.getUserLastName());
            log.info(user.getUserEmail());
            log.info("user roleid", user.getUserRoleId());
            user.setUserInitPassword(userInitPassword);
            userMapper.addUser(reqMap);
            res.put("message", "add user success");
//            res.put("data",user);
            return res;
        } else {
            res.put("message", "not found!");
            res.put("errors", errors);
            return res;
        }



    }

    public Map<String, Object> updateUser(UpdateUserRequest userRequest, String id) throws Exception{
        Map<String, Object> checkUserRequestBody = checkUpdateUserRequestBody(userRequest, id);

        if (checkUserRequestBody.containsKey("errors") == true) {
            return checkUserRequestBody;  // 422
        }

        User user = new User();
        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));

        user.setId(Integer.parseInt(id));
        user.setUserEmail(userRequest.getUserEmail());
        user.setUserAddress(userRequest.getUserAddress());
        user.setUserAvatar(userRequest.getUserAvatar());
        user.setUserRoleId(Integer.parseInt(userRequest.getUserRoleId()));
        if (Objects.equals(userRequest.getUserDateOfBirth(), null) ) {
            user.setUserDateOfBirth(null);
        }else {
            user.setUserDateOfBirth(convertStringToDate(userRequest.getUserDateOfBirth()));
        }        user.setUserFirstName(userRequest.getUserFirstName());
        user.setUserLastName(userRequest.getUserLastName());
        user.setUserPhoneNumber(userRequest.getUserPhoneNumber());



        Map<String, Object> reqUserRoleMap = new HashMap<>();
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        boolean update = false;

        reqMap.put("id", user.getId());
        reqMap.put("userEmail",user.getUserEmail());
        reqMap.put("userRoleId", user.getUserRoleId());
        reqMap.put("userAddress",user.getUserAddress());
        reqMap.put("userAvatar",user.getUserAvatar());
        reqMap.put("userDateOfBirth",user.getUserDateOfBirth());
        reqMap.put("userFirstName",user.getUserFirstName());
        reqMap.put("userLastName",user.getUserLastName());
        reqMap.put("userPhoneNumber", user.getUserPhoneNumber());

        log.info(user.getUserEmail());
        log.info("user roleId", user.getUserRoleId());
//        user.setUserInitPassword(userInitPassword);


        reqUserRoleMap.put("id", user.getUserRoleId());
        User userDB = userMapper.getUserById(reqMap);

        if(userDB == null){
            res.put("message", "Not found");
            errors.put("id", "User id does not exist");
            res.put("errors", errors);
            return res;
        }



//        UserRole userRole= userRoleMapper.getUserRoleById(reqUserRoleMap);
        if(Objects.equals(Integer.toString(user.getUserRoleId()), ConstantMessages.NOT_UPDATE_ID)){}
        else if(userRoleMapper.getUserRoleById(reqUserRoleMap) == null){
            res.put("message", "Not found");
            errors.put("userRoleId", "User Role id does not exist");
            res.put("errors", errors);
            return res;
        }
        if(Objects.equals(Integer.toString(user.getUserRoleId()) ,ConstantMessages.NOT_UPDATE_ID)){
            reqMap.put("userRoleId", userDB.getUserRoleId());
        }
//        else if(Objects.equals(Integer.toString(user.getUserRoleId()),user.getUserRoleId()) == false) update =true;

        if(Objects.equals(user.getUserEmail(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userEmail",userDB.getUserEmail());
        }
        else if(Objects.equals(userDB.getUserEmail(),user.getUserEmail()) == false){
            // email and id duplicate => update yourself

            User userByEmailDB = userMapper.getAllUserByEmail(reqMap);
            // don't exit email in db -> update
            if(userByEmailDB== null){
//                update = true;
            }
            // email and id not duplicate => update userEmail to exit email => can't update
            else if(Objects.equals(user.getId(),userByEmailDB.getId()) == false){
                res.put("message", "Can't update");
                errors.put("infor", "Email exit");
                res.put("errors", errors);
                return res;
            }
            // email and id not duplicate => check next

        }
        if(Objects.equals(user.getUserAvatar(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userAvatar",userDB.getUserAvatar());
        }
//        else if(Objects.equals(userDB.getUserAvatar(),user.getUserAvatar()) == false) update =true;

        if(Objects.equals(user.getUserFirstName(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userFirstName",userDB.getUserFirstName());
        }
//        else if(Objects.equals(userDB.getUserFirstName(),user.getUserFirstName()) == false) update =true;

        if(Objects.equals(user.getUserLastName(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userLastName",userDB.getUserLastName());
        }
//        else if(Objects.equals(userDB.getUserLastName(),user.getUserLastName()) == false) update =true;

        if(Objects.equals(user.getUserDateOfBirth(), convertStringToDate(ConstantMessages.NOT_UPDATE_DATE_STRING))){
            reqMap.put("userDateOfBirth",userDB.getUserDateOfBirth());
            log.info("not update user date of birth");
        }

//        else if(Objects.equals(userDB.getUserDateOfBirth(),user.getUserDateOfBirth()) == false) update =true;
//        log.info(user.getUserDateOfBirth().toString());


        if(Objects.equals(user.getUserAddress(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userAddress",userDB.getUserAddress());

        }
//        else if(Objects.equals(userDB.getUserAddress(),user.getUserAddress()) == false) update =true;

        if(Objects.equals(user.getUserPhoneNumber(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userPhoneNumber", userDB.getUserPhoneNumber());
        }
//        else if(Objects.equals(userDB.getUserPhoneNumber(),user.getUserPhoneNumber()) == false) update =true;



        userMapper.updateUser(reqMap);
        res.put("message", "update user success");
//            res.put("data",user);
        return res;
    }


    public Map<String, Object> editUser(EditUserRequest editUserRequest, String id) throws Exception{    ;

        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        boolean exitErrors = false;



        if (id == null ||isNumeric(id) == false){
            errors.put("id", "user role id must be mumeric and not null");
            exitErrors = true;

        }

        if(Objects.equals(editUserRequest.getUserAvatar(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if(checkAvatar(editUserRequest.getUserAvatar()) == false){
            errors.put("avatar", "avatar incorrect format");

            exitErrors = true;
        }

        if(Objects.equals(editUserRequest.getUserDateOfBirth(), ConstantMessages.NOT_UPDATE_DATE_STRING)){}
        else if (checkDateOfBirth(editUserRequest.getUserDateOfBirth()) == false) {
            errors.put("dateOfBirth", "dataOfBirth incorrect");
            exitErrors = true;
        };

        if(Objects.equals(editUserRequest.getUserPhoneNumber(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if (checkPhoneNumber(editUserRequest.getUserPhoneNumber()) == false) {
            errors.put("phoneNumber", "phoneNumber incorrect format");
            exitErrors = true;
        };

        if(exitErrors == true){
            res.put("message", "inputInvalid");
            res.put("errors", errors);
            return res;
        }



        reqMap.put("id", id);

        reqMap.put("userAddress",editUserRequest.getUserAddress());
        reqMap.put("userAvatar",editUserRequest.getUserAvatar());
        reqMap.put("userDateOfBirth",convertStringToDate(editUserRequest.getUserDateOfBirth()));
        reqMap.put("userPhoneNumber", editUserRequest.getUserPhoneNumber());




        User userDB = userMapper.getUserById(reqMap);

        if(userDB == null){
            res.put("message", "Not found");
            errors.put("id", "User id does not exist");
            res.put("errors", errors);
            return res;
        }




//        else if(Objects.equals(Integer.toString(user.getUserRoleId()),user.getUserRoleId()) == false) update =true;


        if(Objects.equals(editUserRequest.getUserAvatar(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userAvatar",userDB.getUserAvatar());
        }

//        else if(Objects.equals(userDB.getUserLastName(),user.getUserLastName()) == false) update =true;

        if(Objects.equals(editUserRequest.getUserDateOfBirth(), convertStringToDate(ConstantMessages.NOT_UPDATE_DATE_STRING))){
            reqMap.put("userDateOfBirth",userDB.getUserDateOfBirth());
            log.info("not update user date of birth");
        }

//        else if(Objects.equals(userDB.getUserDateOfBirth(),user.getUserDateOfBirth()) == false) update =true;
//        log.info(user.getUserDateOfBirth().toString());


        if(Objects.equals(editUserRequest.getUserAddress(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userAddress",userDB.getUserAddress());

        }
//        else if(Objects.equals(userDB.getUserAddress(),user.getUserAddress()) == false) update =true;

        if(Objects.equals(editUserRequest.getUserPhoneNumber(), ConstantMessages.NOT_UPDATE_CHARACTER)){
            reqMap.put("userPhoneNumber", userDB.getUserPhoneNumber());
        }
//        else if(Objects.equals(userDB.getUserPhoneNumber(),user.getUserPhoneNumber()) == false) update =true;



        userMapper.editUser(reqMap);
        res.put("message", "edit user success");
//            res.put("data",user);
        return res;
    }
    public boolean checkDateOfBirth(String dateOfBirth) throws Exception{
        if(dateOfBirth == null){
            return true;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate.parse(dateOfBirth, formatter);
            return true;
        }catch (DateTimeParseException e){
            return false;
        }
    }

    public java.sql.Date convertStringToDate(String date) throws  Exception{
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(date == ""){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date javaDate = sdf.parse(date);
        return new java.sql.Date(javaDate.getTime());
    }

    @Override
    public boolean checkEmail(String email) throws Exception{
        if(email != null && email.matches("^([\\w]+([.]{1}[\\w]+)*)+@+([\\w]+([.]{1}[\\w]+)+)$")){
            return true;
        } else return false;
    }

    @Override
    public Map<String, Object> login(String email, String password) throws Exception{
        int maxLoginAttempts = 5;
        int maxExpriedPassword = 10; // day
        Config config = configMapper.getConfig();
        if(config == null){

        }else {
            if (config.getMaxExpiredPassword() > 0){
                maxExpriedPassword = config.getMaxExpiredPassword();
                log.info("maxExpriedPassword");
                log.info(Integer.toString(maxExpriedPassword));
            }

            if(config.getMaxNumbersLoginAttempts() > 0){
                maxLoginAttempts =config.getMaxNumbersLoginAttempts();
                log.info("maxLoginAttempts");
                log.info(Integer.toString(maxLoginAttempts));
            }
        }

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        Map<String,Object> claims = new HashMap<>();
        Map<String,Object> res = new HashMap<>();
        Map<String,Object> reqMap = new HashMap<>();
        Map<String,Object> error = new HashMap<>();
        Map<String,Object> jwtUser = new HashMap<>();
        String token = null;
        boolean exitError = false;



        reqMap.put("userEmail", email);
        UserLogin user = userMapper.login(reqMap);
        log.info("lgoin mapper return ");
        log.info("number attempts login ");
//        log.info((Objects.equals(user.getUserNumberLoginAttempts(), null) ? "null": (user.getUserNumberLoginAttempts()));
//        log.info("last time change password login ",(Objects.equals(user.getUserLastTimeChangedPassword(),null)? "null": user.getUserLastTimeChangedPassword().getTime()  ) );
        if(user == null ){
            error.put("errors", "user not eixit");
            exitError = true;
        } else if (user.getUserState() == 3 ){
            error.put("err","wrong login up to "+maxLoginAttempts+" times , please contact to admin to unlock" );
            res.put("message", "login failed");
            res.put("errors", error);
            return res;
        }
        else if(Objects.equals(password, user.getUserInitPassword()) || bCryptPasswordEncoder.matches(password, user.getUserPassword())){

        } else {
            exitError = true;
            error.put("err", "incorrect password");
        }
//        int x = null;

        if(exitError == true){
            if(user != null){

                reqMap.put("userNumberLoginAttempts", user.getUserNumberLoginAttempts() + 1);
                userMapper.loginFailedUpdate(reqMap);

                if(user.getUserNumberLoginAttempts() >= maxLoginAttempts){
                    error.put("err", "wrong login up to "+ maxLoginAttempts +" times");
                    userMapper.lockUser(reqMap);
                }

            }

            res.put("message", "login failed");
            res.put("errors", error);

            return  res;
        } else {
            Long expireJWT = ConstantMessages.TIME_EXPIRE;
            userMapper.loginSuccess(reqMap);
            jwtUser.put("id",user.getId());
            jwtUser.put("email", email);
            jwtUser.put("role", user.getUserRoleName());

            boolean changedPassword = (user.getUserPassword() != null) ? true : false;
            Date now = new Date();
            if(user.getUserLastTimeChangedPassword() == null){

            }else if ( now.getTime() - user.getUserLastTimeChangedPassword().getTime()  > maxExpriedPassword* 24*60*60*1000L){
                changedPassword = false;
            }else if (now.getTime() - user.getUserLastTimeChangedPassword().getTime() < ConstantMessages.TIME_EXPIRE){

                if (now.getTime() - user.getUserLastTimeChangedPassword().getTime() < 1*60*60*1000L ){
                    expireJWT = 1*60*60*1000L;
                }else {
                    expireJWT = user.getUserLastTimeChangedPassword().getTime() - maxExpriedPassword* 24*60*60*1000L;

                }

            }
            jwtUser.put("changedPassword", changedPassword);
            token = jwtTokenUtil.generateToken(jwtUser, expireJWT);

            res.put("message", "success");
            res.put("jwt", token);
		res.put("id", user.getId());
		res.put("changedPassword", changedPassword);

            return res ;
        }

    }

    @Override
    public Map<String, Object> logout(String jwt) throws Exception{
        Map<String, Object> res = new HashMap<>();
        java.util.Date now = new java.util.Date();


        log.info("controler");
        log.info(jwt);
        log.info("jwt expire");
        Claims claims = jwtTokenUtil.getClaimsFromToken(jwt);

        log.info(claims.get("exp").toString());
//        log.info("time now");


        Long timeExpire =  Long.parseLong(claims.get("exp").toString())*1000- now.getTime();
        if (timeExpire < 1000L){
            timeExpire = 1000L;
        }
        log.info(timeExpire.toString());
        redisTemplate.opsForValue().set(jwt,"blacklist", timeExpire, TimeUnit.MILLISECONDS);
        res.put("message", "logout success");
        return res;
    }


    public Map<String, Object> changePassword(String email, String oldPassword, String newPassword) throws Exception{
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        Map<String, Object> data = new HashMap<>();


        reqMap.put("userEmail", email);
        reqMap.put("oldPassword", oldPassword);
        reqMap.put("userPassword",bCryptPasswordEncoder.encode(newPassword) );

        User user = userMapper.getUserByEmail(reqMap);
        if(user == null){
            res.put("message", "The given data was invalid.");
            errors.put("email", "Email does not exist or not active");
            res.put("errors", errors);
        } else if (user.getUserInitPassword() == null ) {
            // check password
            if (bCryptPasswordEncoder.matches(oldPassword, user.getUserPassword())){
                userMapper.changePassword(reqMap);
                res.put("message" , " change password success");
                data.put("userEmail", email);
//                data.put("password", newPassword);
                res.put("data", data);
            } else {
                res.put("message", "The given data was invalid.");
                errors.put("initPassword", "validate password failed");
                res.put("errors", errors);
            }

        } else if (user.getUserPassword() == null) {
            // check init password
            if (Objects.equals(oldPassword, user.getUserInitPassword())){
                userMapper.changePassword(reqMap);
                res.put("message" , " change password success");
                data.put("userEmail", email);
//                data.put("password", newPassword);
                res.put("data", data);
            }  else {
                res.put("message", "The given data was invalid.");
                errors.put("initPassword", "validate password failed");
                res.put("errors", errors);
            }

        }

        if(res.containsKey("errors")){
            return res;
        }
        res =  login(email, newPassword);
        res.put("message" , "Change password success");
        return res;

    }

    public Map<String, Object> deleteUser(int id ) throws Exception{
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        reqMap.put("id",id);

        User user = userMapper.getUserAndInitUserById(reqMap);
        if(user == null){
            res.put("message", "The given data was invalid.");
            errors.put("id", "id not exit!");
            res.put("errors", errors);
            return  res;
        }else {
            userMapper.deleteUser(reqMap);
            res.put("message", "delete user sucess !");
            return  res;
        }

    }
    public Map<String, Object> resetPassword(int id ) throws Exception{
        Map<String, Object> reqMap = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> errors = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        reqMap.put("id",id);

        User user = userMapper.getUserById(reqMap);
        if(user == null){
            res.put("message", "The given data was invalid.");
            errors.put("id", "id not exit in list user!");
            res.put("errors", errors);
            return  res;
        }else {

            String userInitPassword = new String();
            while ( checkPassword(userInitPassword) == false){
                userInitPassword = newInitPassword(8);
            }
            data.put("id", id);
            data.put("initPassword", userInitPassword);
            reqMap.put("initPassword" , userInitPassword);
            userMapper.resetPassword(reqMap);
            res.put("message", "reset password user success !");
            data.put("id", id);
//            res.put("data", data);
            return  res;
        }

    }

    @Override
    public Map<String, Object> checkUserRequestBody(UserRequest userRequest, String id) throws Exception {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();  // if incorrect input
        Map<String, Object> errors = new HashMap<>();  // save error


        Boolean exitError = false;
//        if(id == null){
//            errors.put("id", "id must be numeric");
//            resErr.put("errors", errors);
//            exitError = true; // 422
//        }
//        else if(!isNumeric(id)){
//            errors.put("id", "id must be numeric");
//            resErr.put("errors", errors);
//            exitError = true; // 422
//        }

        if (userRequest.getUserRoleId() == null ||isNumeric(userRequest.getUserRoleId()) == false){
            errors.put("user role id", "user role id must be mumeric and not null");
            resErr.put("errors", errors);
            exitError = true;

        }
        if(userRequest.getUserFirstName() == null || userRequest.getUserFirstName().equals("")){
            errors.put("firstName", "firstName not null");
            resErr.put("errors", errors);
            exitError = true;
        }
        if (userRequest.getUserLastName() == null || userRequest.getUserLastName().equals("")){
            errors.put("lastName", "lastName not null");
            resErr.put("errors", errors);
            exitError = true;
        }
        if (checkEmail(userRequest.getUserEmail()) == false) {
            errors.put("userEmail", "Email incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }
        ;
        if (checkPhoneNumber(userRequest.getUserPhoneNumber()) == false) {
            errors.put("phoneNumber", "phoneNumber incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }
        ;
        if (checkDateOfBirth(userRequest.getUserDateOfBirth()) == false) {
            errors.put("dateOfBirth", "dataOfBirth incorrect");
            resErr.put("errors", errors);
            exitError = true;
        };
        if(checkAvatar(userRequest.getUserAvatar()) == false){
            errors.put("avatar", "avatar incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }


        if (exitError == true) {
            res.put("message", "The given data was invalid.");
            res.put("errors", resErr);
            return res;
        };


        return new HashMap<>();
    }


    @Override
    public Map<String, Object> checkUpdateUserRequestBody(UpdateUserRequest userRequest, String id) throws Exception {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();  // if incorrect input
        Map<String, Object> errors = new HashMap<>();  // save error


        Boolean exitError = false;
        if(id == null){
            errors.put("id", "id must be numeric");
            resErr.put("errors", errors);
            exitError = true; // 422
        }
        else if(!isNumeric(id)){
            errors.put("id", "id must be numeric");
            resErr.put("errors", errors);
            exitError = true; // 422
        }
        if(Objects.equals(userRequest.getUserRoleId(), ConstantMessages.NOT_UPDATE_ID)){}
        else if (userRequest.getUserRoleId() == null ||isNumeric(userRequest.getUserRoleId()) == false){
            errors.put("user role id", "user role id must be mumeric and not null");
            resErr.put("errors", errors);
            exitError = true;

        }
        if(Objects.equals(userRequest.getUserFirstName(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if(userRequest.getUserFirstName() == null || userRequest.getUserFirstName().equals("")){
            errors.put("firstName", "firstName not null");
            resErr.put("errors", errors);
            exitError = true;
        }

        if(Objects.equals(userRequest.getUserLastName(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if (userRequest.getUserLastName() == null || userRequest.getUserLastName().equals("")){
            errors.put("lastName", "lastName not null");
            resErr.put("errors", errors);
            exitError = true;
        }

        if(Objects.equals(userRequest.getUserEmail(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if (checkEmail(userRequest.getUserEmail()) == false) {
            errors.put("userEmail", "Email incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }

        if(Objects.equals(userRequest.getUserPhoneNumber(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if (checkPhoneNumber(userRequest.getUserPhoneNumber()) == false) {
            errors.put("phoneNumber", "phoneNumber incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }

        if(Objects.equals(userRequest.getUserDateOfBirth(), ConstantMessages.NOT_UPDATE_DATE_STRING)){
            log.info("????");
        }
        else if (checkDateOfBirth(userRequest.getUserDateOfBirth()) == false) {
            errors.put("dateOfBirth", "dataOfBirth incorrect");
            resErr.put("errors", errors);
            exitError = true;
        };

        /*if(Objects.equals(userRequest.getUserAvatar(), ConstantMessages.NOT_UPDATE_CHARACTER)){}
        else if(checkAvatar(userRequest.getUserAvatar()) == false){
            errors.put("avatar", "avatar incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        } */


        if (exitError == true) {
            res.put("message", "The given data was invalid.");
            res.put("errors", resErr);
            return res;
        };


        return new HashMap<>();
    }



}
