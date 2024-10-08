package com.tsjeong.brokerage.service.util;

public class StringValidator {

    public static boolean isEmptyOrNull(String str) {
        return str == null || str.trim().isEmpty();
    }
}
