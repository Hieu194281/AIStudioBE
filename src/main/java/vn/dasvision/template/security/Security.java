package vn.dasvision.template.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import vn.dasvision.template.config.untils.ConstantMessages;
import vn.dasvision.template.config.untils.JwtAuthenticationFilter;
import vn.dasvision.template.config.untils.JwtTokenUtil;

import static com.google.common.base.Predicates.and;
//import vn.dasvision.template.config.untils.JwtTokenUtil;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {




    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;



    @Override
    public  void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // guest
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v1/authorization/login", "/v1/user/change_password").permitAll()
                // admin
                .antMatchers("/v1/user_role/delete/**").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user_role/add").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user_role/update/**").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user_role/list").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user_role/list_all").hasRole(ConstantMessages.ADMIN)

                .antMatchers("/v1/user/delete/**").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user/add").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user/reset_password/**").hasRole(ConstantMessages.ADMIN)
                .antMatchers("/v1/user/list").hasRole(ConstantMessages.ADMIN)

                // user update user
                .antMatchers("/v1/user/update/**", "/v1/user_role/list/**", "/v1/user/edit/**").hasAnyRole(ConstantMessages.USER_VALIDATE_ID, ConstantMessages.ADMIN)
//                .antMatchers().hasAnyRole(ConstantMessages.ADMIN, ConstantMessages.USER_VALIDATE_ID)


                // user
//                .antMatchers("/v1/user_role/list_all",
//                                "/v1/user_role/list",
//                        "/v1/user_role/list/**",
//                        "/v1/user/list",
//                        "/v1/user/list/**",
//                        "/v1/authorization/lout"
//                ).hasAnyRole(ConstantMessages.USER, ConstantMessages.ADMIN,ConstantMessages.USER_VALIDATE_ID)

                .antMatchers("/v1/**").hasAnyRole(ConstantMessages.USER, ConstantMessages.ADMIN,ConstantMessages.USER_VALIDATE_ID)

                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
//
//
//    @Override
//    public  void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                // guest
//                .anyRequest().permitAll();
////                .and()
////                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//    }
//


}