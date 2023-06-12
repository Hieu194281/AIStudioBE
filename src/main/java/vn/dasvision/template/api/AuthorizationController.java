package vn.dasvision.template.api;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.dasvision.template.service.UserService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping(value = "/v1/authorization")
public class AuthorizationController {

    private UserService userService;
    @Autowired
    public AuthorizationController(UserService userService ) {
        this.userService = userService;
//        this.blacklist = blacklist;
    }

    @PostMapping("/login")
    public ResponseEntity login(
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "password", required = true) String password

    ) throws Exception{
        boolean exitError = false;
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> resErr = new HashMap<>();  // if incorrect input
        Map<String, Object> errors = new HashMap<>();  // save error

        if (email== null || userService.checkEmail(email) == false){
            errors.put("email", "Email incorrect format");
            resErr.put("errors", errors);
            exitError = true;
        }

        if (password == null || userService.checkPassword(password) == false){
            errors.put("Password", "Password incorrect format ");
            resErr.put("errors", errors);
            exitError = true;
        }

        if (exitError == true) {
            res.put("message", "The given data was invalid, pasword must more 8 character, have 4 type character ([0->9],[a->z],[A->Z],[!@#$%^&*)(])");
            res.put("errors", resErr);
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        res = userService.login(email, password);

        if(res.containsKey("errors")){
            return new ResponseEntity<>(res, HttpStatus.UNPROCESSABLE_ENTITY);

        }
        return new ResponseEntity<>(res, HttpStatus.OK);

    }

    @PostMapping("/logout")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity logout(
            HttpServletRequest request
    ) throws Exception{

        String jwt = request.getHeader("Authorization");
        return new ResponseEntity(userService.logout(jwt),HttpStatus.OK);

    }
}
