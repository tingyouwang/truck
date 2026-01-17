package com.luzhu.truck.config;

import com.luzhu.truck.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//         http.csrf().disable()
//                .formLogin().disable()
//                .logout().disable()
////                .exceptionHandling(e -> e.authenticationEntryPoint(inv))
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests(
//                        auth -> auth.requestMatchers(HttpMethod.POST, "/user/register", "/user/login", "/authentication/login", "/key/getKey").permitAll()
//                                //todo 未來加入腳色管理
////                                .requestMatchers(HttpMethod.POST, "/abc/edf").hasRole("admin")
//                        .anyRequest().authenticated());
//
////        // 不設置默認的 loginPage，避免重定向問題
//        http.exceptionHandling()
//                .authenticationEntryPoint((request, response, authException) -> {
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorizedaaaaaaa");
//                });
//
////         前端login頁面 url
////         http.formLogin().loginPage("/authentication/login");
////                 .loginProcessingUrl("/login");
//
////         http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
//         http.addFilterAfter(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
//
//         return http.build();
//    }

    @Autowired
    @Qualifier("delegatedAuthenticationEntryPoint")
    AuthenticationEntryPoint authEntryPoint;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers(HttpMethod.POST, "/user/register", "/user/login", "/authentication/login", "/key/getKey").permitAll()
                                //todo 未來加入腳色管理
                                .anyRequest().authenticated());

//        // 不設置默認的 loginPage，避免重定向問題
        http.exceptionHandling()
                .authenticationEntryPoint(authEntryPoint);


        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}



