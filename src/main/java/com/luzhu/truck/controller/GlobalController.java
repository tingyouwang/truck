package com.luzhu.truck.controller;

import com.alibaba.fastjson.JSONObject;
import com.luzhu.truck.response.ResponseEnum;
import com.luzhu.truck.response.ResponseModel;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.luzhu.truck.controller")
public class GlobalController {

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseModel handleValidationExceptions(MethodArgumentNotValidException ex) {
        JSONObject json = new JSONObject();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                json.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseModel<>().validFail(json.toString(), ResponseEnum.VALID_ERROR);
    }

}
