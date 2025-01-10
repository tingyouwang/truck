package com.luzhu.truck.validator;

import com.luzhu.truck.exception.AbstractException;

public class Validator {
    public static void isFalseThrow(boolean bool, AbstractException abstractException) {
        if (!bool) {
            throw abstractException;
        }
    }

    public static void isNullThrow(Object object, AbstractException abstractException) {
        if (null == object) {
            throw abstractException;
        }
    }
}
