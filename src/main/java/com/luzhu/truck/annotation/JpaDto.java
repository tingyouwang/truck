package com.luzhu.truck.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Component
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JpaDto {
}
