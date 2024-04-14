package com.luzhu.truck.validator;

import com.luzhu.truck.exception.AbstractException;

public class Validator {
    public static void isFalseThrow(boolean bool, AbstractException abstractException) {
        if (!bool) {
            throw abstractException;
        }
    }
}
