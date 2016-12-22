package com.nabto.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NabtoCApiWrapperStubController {
    static {
        System.loadLibrary("nabto_client_api_jni");
    }

    static native void setReturnValues(String values);
    static native String getParameterValues();

    static void setReturnValueMap(Map<String, String> values) {
        setReturnValues(mapToString(values));
    }

    static Map<String, String> getParameterValueMap() {
        return stringToMap(getParameterValues());
    }

    static String mapToString(Map<String, String> values) {
        StringBuilder valuesString = new StringBuilder();
        String delim = "";
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            valuesString.append(delim).append(key).append("=").append(value);
            delim = ",";
        }
        return valuesString.toString();
    }

    static Map<String, String> stringToMap(String valuesString) {
        Map values = new HashMap<String, String>();

        Pattern pattern = Pattern.compile("[^,]+\\=[^,]+");
        Matcher matcher = pattern.matcher(valuesString);

        while (matcher.find()) {
            String pair = matcher.group();
            String key = pair.split("=")[0];
            String value = pair.split("=")[1];
            values.put(key, value);
        }
        return values;
    }
}
