package vn.dasvision.template.config.untils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@RestController
public class JwtAuthenticationFilter extends  OncePerRequestFilter {

//    private RedisTemplate redisTemplate =  new RedisConfig().redisTemplate( new RedisConfig().redisConnectionFactory());

//    @Autowired


    @Autowired
    public RedisTemplate redisTemplate ;



    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private boolean checkRoleUserWithID(String pathURL, String id) {
        String[] pathSplit = pathURL.split("/");

        if(pathSplit.length == 5 ){
            if(Objects.equals(id,pathSplit[4]))return true;
        }
        return false;
    }

    private boolean exitInBlackList(String token)  throws Exception{
        if(redisTemplate.hasKey(token) ) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException  {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = authorizationHeader;
        Claims claims = null;
        String pathInfor = request.getRequestURI();
        String roleClient = ConstantMessages.GUEST;
        ObjectMapper objectMapper = new ObjectMapper();


        if(jwt == null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            log.info(" ============================");
            log.info( SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());
        } else {
            try {
                if( exitInBlackList(jwt)) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    Map<String,Object> ret = new HashMap<>();
                    ret.put("message", "Token invalidate");
                    ret.put("error", "token in was logout");
                    response.getWriter().write(objectMapper.writeValueAsString(ret));
                    return;
                }
            } catch (Exception e) {
                log.info("some err when check black list");
            }
//            ;
            log.info("1234");

            try {

                if(jwtTokenUtil.validateToken(jwt)){
                    claims = jwtTokenUtil.getClaimsFromToken(jwt);
                    if(Objects.equals(claims.get("changedPassword"), false) && !((Objects.equals(pathInfor, "/v1/user/change_password")  || Objects.equals(pathInfor,"/v1/authorization/logout"))) ){
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            Map<String,Object> ret = new HashMap<>();
                            ret.put("message", "you must change password first");
                            response.getWriter().write(objectMapper.writeValueAsString(ret));
                            return ;


//                        filterChain.doFilter(request, response);
                    }

                    log.info("claims.getExpiration().toString()");
                    log.info(claims.getExpiration().toString());
                    roleClient= claims.get("role").toString();
//                    if(Objects.equals(pathInfor, "/v1/user/change_password")){
//                        if(Objects.equals(claims.get("email"), request.getParameter("email"))== false) {
//                            response.setStatus(HttpStatus.FORBIDDEN.value());
//                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                            Map<String,Object> ret = new HashMap<>();
//                            ret.put("error", "You this account is another user");
//                            response.getWriter().write(objectMapper.writeValueAsString(ret));
//                            return ;
//
//                        }
//                    }
                    log.info(jwt);
                    log.info(pathInfor);
                    log.info(claims.get("id").toString());

//                    checkRoleUserWithID(pathInfor, claims.get("id").toString());
                    if(Objects.equals(roleClient, ConstantMessages.USER) && checkRoleUserWithID(pathInfor, claims.get("id").toString())) {
                        roleClient = ConstantMessages.USER_VALIDATE_ID;
                    }

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+roleClient));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info(" ============================");
                    log.info( SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString());



                }else {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    Map<String,Object> ret = new HashMap<>();
                    ret.put("message", "Token validate failed");
                    response.getWriter().write(objectMapper.writeValueAsString(ret));
                    return ;


                }
            } catch (Exception e) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                Map<String,Object> ret = new HashMap<>();
                ret.put("message", "Token invalidate");
//                ret.put("error", "token incorrect format");
//                ret.put("error", e);
                response.getWriter().write(objectMapper.writeValueAsString(ret));


                return ;

            }


        }


        filterChain.doFilter(request, response);


    }
}