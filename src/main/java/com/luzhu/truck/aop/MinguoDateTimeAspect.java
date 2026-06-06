package com.luzhu.truck.aop;

import com.luzhu.truck.util.DateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
@Slf4j
public class MinguoDateTimeAspect extends BaseAop{
    @Pointcut("execution(* com.luzhu.truck.controller..*(..))")
    public void allControllerLayer() {
    }

    @Before("allControllerLayer()")
    public void convertDate(JoinPoint joinPoint) throws Exception {
        HttpServletRequest request = getHttpServletRequest();
        if (null == request) {
            return;
        }
//        if ("get".equalsIgnoreCase(request.getMethod())) {
//            return;
//        }
        Object[] args = joinPoint.getArgs();

        for (Object paramObject : args) {
            if (null == paramObject) {
                continue;
            }
            if (paramObject instanceof Integer || paramObject instanceof BigDecimal
                    || paramObject instanceof String
                    || paramObject instanceof HttpServletRequest
                    || paramObject instanceof HttpServletResponse) {
                continue;
            }

            Field[] fieldArr = paramObject.getClass().getDeclaredFields();
            for (Field field : fieldArr) {
                if (field.getName().contains("Date") || field.getName().contains("date") || field.getName().equals("expenseYearMonth")
                        || field.getName().equals("targetBillYearMonth")) {
                    setField(paramObject, field);
                }

            }
        }
    }

    private void setField(Object paramObject, Field field) throws Exception {
        field.setAccessible(true);
        Object value = field.get(paramObject);
        if (null == value || "".equals(value)) {
            return;
        }
        if (value instanceof String) {
            field.set(paramObject, DateTimeUtil.transferWestDateStr(value.toString()));
            field.setAccessible(false);
        }
        if (value instanceof List<?>) {

            List<?> listValue = (List<?>) value;
            List<Object> convertedList = new ArrayList<>();
            for (Object item : listValue) {
                if (item instanceof String) {
                    convertedList.add(DateTimeUtil.transferWestDateStr(item.toString()));
                } else {
                    convertedList.add(item); // 保留非字串的元素
                }
            }
            field.set(paramObject, convertedList);
            field.setAccessible(false);
        }
    }
}
