package com.luzhu.truck.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component("delegatedAuthenticationEntryPoint")
@Slf4j
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //測試
        log.info("exception Request from: " + request.getRemoteAddr());

        //todo 回家測試手機?是否也會被掃, 或者把對外port關閉
        String requestURI = request.getRequestURI();
        String servletPath = request.getServletPath();
        String contextPath = request.getContextPath();

        log.info("Request URI: " + requestURI);
        log.info("Servlet Path: " + servletPath);
        log.info("Context Path: " + contextPath);

        log.info(authException.getMessage(), authException);
        resolver.resolveException(request, response, null, authException);

    }
}
