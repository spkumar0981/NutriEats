package com.nutri.rest.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUtils {
    public static Map<Integer, String> captchaValues = new HashMap<Integer, String>() {
        {
            put(0,"2+3");
            put(1, "10-2");
            put(2, "5+9");
            put(3, "6-4");
            put(4, "8+10");
            put(5, "12-10");
            put(6, "20-15");
            put(7, "13+6");
            put(8, "16+5");
            put(9, "14+6");
            put(10, "7+6");
            put(11, "19-0");
            put(12, "18-4");
            put(13, "16+2");
            put(14, "10+5");
            put(15, "11-1");
            put(16, "50+10");
            put(17, "40-11");
            put(18, "25+25");
            put(19, "60-30");
            put(20, "10+10");
        }

    };

    public static Map<Integer, String> captchaResults = new HashMap<Integer, String>() {
        {
            put(0,"5");
            put(1, "8");
            put(2, "14");
            put(3, "2");
            put(4, "18");
            put(5, "2");
            put(6, "5");
            put(7, "19");
            put(8, "21");
            put(9, "20");
            put(10, "13");
            put(11, "19");
            put(12, "14");
            put(13, "18");
            put(14, "15");
            put(15, "10");
            put(16, "60");
            put(17, "29");
            put(18, "50");
            put(19, "30");
            put(20, "20");
        }

    };

    public static final String PAGABLE = " \n-- #pageable\n";

    public static Long castObjectToLong(Object field) {
        if (field == null)
            return null;
        else
            return new Long(field.toString());
    }

    public static BigDecimal castObjectToBigDecimal(Object field) {
        if (field == null)
            return null;
        else
            return new BigDecimal(field.toString());
    }

    public static String castObjectToString(Object field) {
        if (field == null)
            return null;
        else
            return field.toString();
    }

    public static String[] castObjectToStringArray(Object field) {
        if (field == null)
            return null;
        else
            return field.toString().split(";");
    }

    public static Boolean castObjectToBoolean(Object field) {
        if (field == null)
            return null;
        else
            return Boolean.valueOf(field.toString());
    }
}
