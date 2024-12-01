package com.luzhu.truck.controller;

import com.alibaba.fastjson.JSONObject;
import com.luzhu.truck.response.PageResult;
import com.luzhu.truck.response.ResponseEnum;
import com.luzhu.truck.response.ResponseModel;
import com.luzhu.truck.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice(basePackages = "com.luzhu.truck.controller")
@Slf4j
public class GlobalController implements ResponseBodyAdvice<Object> {

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseModel handleValidationExceptions(MethodArgumentNotValidException ex) {
        JSONObject json = new JSONObject();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                json.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseModel<>().validFail(json.toString(), ResponseEnum.VALID_ERROR);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof ResponseModel responseModel) {
            Object data = responseModel.getData();
            if (data instanceof PageResult<?> pageResult) {
                List<?> pageList = pageResult.getPageList();
                for (Object item : pageList) {
                    processFields(item);
                }
            }
//          todo 還有其他的instance
        }
        return body;
    }

    private void processFields(Object obj) {
        if (obj == null) return;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (isDateField(field)) {
                processDateField(obj, field);
            }
        }
    }

    private boolean isDateField(Field field) {
        return field.getType().equals(String.class) &&
                (field.getName().contains("date") || field.getName().contains("Date")) &&
                !field.getName().contains("update");
    }

    private void processDateField(Object obj, Field field) {
        field.setAccessible(true);
        try {
            String value = (String) field.get(obj);
            if (value != null) {
                LocalDate westDate = LocalDate.parse(value, DateTimeFormatter.ISO_DATE);
                String formattedDate = DateTimeUtil.parseToMinguoDate(westDate);
                field.set(obj, formattedDate);
            }
        } catch (IllegalAccessException | IllegalArgumentException e) {
            log.error("Error processing field: {}", field.getName(), e);
        }
    }
}
