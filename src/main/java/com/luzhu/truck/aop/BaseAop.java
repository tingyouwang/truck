package com.luzhu.truck.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class BaseAop {
    public BaseAop() {
    }

    protected HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    protected Map<String, String> paramsMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        Enumeration<String> parameterNames = request.getParameterNames();

        while(parameterNames.hasMoreElements()) {
            String name = (String)parameterNames.nextElement();
            String parameterValues = request.getParameter(name);
            map.put(name, parameterValues);
        }

        return map;
    }
}
