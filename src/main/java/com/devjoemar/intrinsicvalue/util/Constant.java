package com.devjoemar.intrinsicvalue.util;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String RESULT_OK = "OK";
    public static final String RESULT_NOT_OK = "NOT_OK";

    public static final String GENERIC_RESPONSE_CODE = "GENERIC-ERROR";

    public static final String RESPONSE_CODE_PREFIX = "PRODUCT-";

    public static Map<String, String> getResponseHashMap() {
       var responseMap = new HashMap<String, String>();
        responseMap.put(RESPONSE_CODE_PREFIX + "1", "not found");
        responseMap.put(RESPONSE_CODE_PREFIX + "2", "already exists");
        responseMap.put(RESPONSE_CODE_PREFIX + "3", "not created");
        responseMap.put(RESPONSE_CODE_PREFIX + "4", "not updated");
        responseMap.put(RESPONSE_CODE_PREFIX + "5", "not deleted");
        responseMap.put(RESPONSE_CODE_PREFIX + "6", "found");
        responseMap.put(RESPONSE_CODE_PREFIX + "7", "created");
        responseMap.put(RESPONSE_CODE_PREFIX + "8", "updated");
        responseMap.put(RESPONSE_CODE_PREFIX + "9", "deleted");
        responseMap.put(RESPONSE_CODE_PREFIX + "10", "list found");
        responseMap.put(RESPONSE_CODE_PREFIX + "11", "validation error");
        return responseMap;
    }
}