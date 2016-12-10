package com.nabto.api;

public class NabtoCApiWrapperStubController {
    static {
        System.loadLibrary("nabto_client_api_jni");
    }

    static native void setReturnValues(String values);
    static native String getParameterValues();
}
