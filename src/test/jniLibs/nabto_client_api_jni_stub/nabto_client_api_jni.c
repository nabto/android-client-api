#include <sys/types.h>
#include "nabto_client_api.h"

/**
 * The following header file can be regenerated with javah
 * javah -classpath ../android/android-common/src/main/java/ com.nabto.api.NabtoCApiWrapper
 */

#include "com_nabto_api_NabtoCApiWrapper.h"

#include <string.h>
#include <stdio.h>
#include <jni.h>

#define DEBUG 0  // Enable to print JNI debug logging

#if defined(ANDROID) || defined(__ANDROID__)

#include <android/log.h>
#define DEBUG_TAG "NDK_DEBUG"
#define APPNAME DEBUG_TAG

void nabtoLogCallback(const char* str, size_t s) {
    __android_log_print(ANDROID_LOG_VERBOSE, APPNAME, "%s", str);
}

#if DEBUG
#define JNILOG(fmt, ...)  __android_log_print(ANDROID_LOG_DEBUG, DEBUG_TAG, fmt, ##__VA_ARGS__);
#else
#define JNILOG(fmt, ...) while(0);
#endif

#else

void nabtoLogCallback(const char* str, size_t s) {
    printf("%s\n", str);
}

#if DEBUG
#define JNILOG(fmt, ...) \
    do { \
        printf("%s/%s:%d " fmt " \n", __FILE__, __func__, __LINE__, ##__VA_ARGS__); \
    } while(0);
#else
#define JNILOG(fmt, ...) while(0) {};
#endif

#endif /* ANDROID */

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoStartup(JNIEnv* env,
                                                      jclass thiz,
                                                      jstring nabtoHomeDir) {
    nabto_status_t nabtoStatus;

    if(nabtoHomeDir == NULL) {
        nabtoStatus = nabtoStartup(NULL);
    } else {
        // Convert Java string into native string
        const char* nabtoHomeDirNative = (*env)->GetStringUTFChars(env, nabtoHomeDir, NULL);
        if (nabtoHomeDirNative == NULL)  // Was there enough memory?
            return -1;  // If not return. This will throw OutOfMemoryError on Java
                        // side since exception was not cleared.

        nabtoStatus = nabtoStartup(nabtoHomeDirNative);

        // Release string again
        (*env)->ReleaseStringUTFChars(env, nabtoHomeDir, nabtoHomeDirNative);
    }

#if defined(ANDROID) || defined(__ANDROID__)
    // Register nabto client library internal logging method
    nabtoRegisterLogCallback(&nabtoLogCallback);
#endif

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoSetStaticResourceDir(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring nabtoResDir) {
    // Convert Java string into native string
    const char* nabtoResDirNative = (*env)->GetStringUTFChars(env, nabtoResDir, NULL);
    if (nabtoResDirNative == NULL)  // Was there enough memory?
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.

    nabto_status_t nabtoStatus = nabtoSetStaticResourceDir(nabtoResDirNative);

    // Release string again
    (*env)->ReleaseStringUTFChars(env, nabtoResDir, nabtoResDirNative);

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoShutdown(JNIEnv* env,
                                                       jclass thiz) {
    return nabtoShutdown();
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoOpenSession(JNIEnv* env,
                                                             jclass thiz,
                                                             jstring email,
                                                             jstring password) {
    JNILOG("Before start")

    // Convert Java string into native string
    const char* emailNative = (*env)->GetStringUTFChars(env, email, 0);
    if (emailNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    const char* passwordNative = (*env)->GetStringUTFChars(env, password, 0);
    if (passwordNative == NULL)  // Was there enough memory?
    {
        (*env)->ReleaseStringUTFChars(env, email, emailNative);  // Free the allocated email
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    // session must not be NULL if open session fails (NewDirectByteBuffer JNI
    // check)
    nabto_handle_t session = (nabto_handle_t)1;
    nabto_status_t nabtoStatus = nabtoOpenSession(&session, emailNative, passwordNative);
    if (nabtoStatus != NABTO_OK || session == (nabto_handle_t)1) {
        JNILOG("Invalid open of session with nabto status: %i", nabtoStatus)
    }

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, email, emailNative);
    (*env)->ReleaseStringUTFChars(env, password, passwordNative);

    // Now create a Session object and return it
    // Find the class com.nabto.api.Session
    jclass sessionClass = (*env)->FindClass(env, "com/nabto/api/Session");
    if (sessionClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("Session obj is created")
    // Get the method ID for constructor
    jmethodID cid = (*env)->GetMethodID(env, sessionClass, "<init>",
                                        "(Ljava/lang/Object;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("Got the method ID for constructor")

    jobject sessionDirectBytebuffer = (*env)->NewDirectByteBuffer(
        env, (void*)session, sizeof(nabto_handle_t));

    // Call constructor and return the resulting Session object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, sessionClass, cid, sessionDirectBytebuffer,
                             nabtoStatus);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoOpenSessionBare(JNIEnv* env,
                                                                 jclass thiz) {
    JNILOG("Before start")

    // session must not be NULL if open session fails (NewDirectByteBuffer JNI
    // check)
    nabto_handle_t session = (nabto_handle_t)1;
    nabto_status_t nabtoStatus = nabtoOpenSessionBare(&session);
    if (nabtoStatus != NABTO_OK || session == (nabto_handle_t)1) {
        JNILOG("Invalid open of session with nabto status: %i", nabtoStatus)
    }

    // Now create a Session object and return it
    // Find the class com.nabto.api.Session
    jclass sessionClass = (*env)->FindClass(env, "com/nabto/api/Session");
    if (sessionClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("Session obj is created")
    // Get the method ID for constructor
    jmethodID cid = (*env)->GetMethodID(env, sessionClass, "<init>",
                                        "(Ljava/lang/Object;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("Got the method ID for constructor")

    jobject sessionDirectBytebuffer = (*env)->NewDirectByteBuffer(
        env, (void*)session, sizeof(nabto_handle_t));

    // Call constructor and return the resulting Session object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, sessionClass, cid, sessionDirectBytebuffer,
                             nabtoStatus);
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoCloseSession(JNIEnv* env,
                                                           jclass thiz,
                                                           jobject sessionObj) {
    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);
    return nabtoCloseSession(session);
}

jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcSetDefaultInterface(JNIEnv* env,
                                                                                jclass thiz,
                                                                                jstring interfaceDefinition,
                                                                                jobject sessionObj) {
    // Convert Java string into native string
    const char* interfaceDefinitionNative = (*env)->GetStringUTFChars(env, interfaceDefinition, 0);
    if (interfaceDefinitionNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    char* errorMessageBufferNative;

    JNILOG("%s XML: %s", "Before RpcSetDefaultInterface", interfaceDefinitionNative)

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    // set default interface
    nabto_status_t nabtoStatus = nabtoRpcSetDefaultInterface(
        session, interfaceDefinitionNative, &errorMessageBufferNative);

    JNILOG("%s error: %s NabtoStatus: %i", "After RpcSetDefaultInterface",
           errorMessageBufferNative, nabtoStatus)

    // Release string again
    (*env)->ReleaseStringUTFChars(env, interfaceDefinition, interfaceDefinitionNative);

    jstring errorMessageBuffer = NULL;
    if (nabtoStatus == NABTO_FAILED_WITH_JSON_MESSAGE) {
        errorMessageBuffer = (*env)->NewStringUTF(env, errorMessageBufferNative);
        if (errorMessageBuffer == NULL) {  // Was there enough memory?
            nabtoFree(errorMessageBuffer);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }
        nabtoFree(errorMessageBufferNative);  // Free the result as it is now in
                                              // a java equivalent
    }

    // Now create a RpcResult object and return it
    // Find the class com.nabto.api.RpcResult
    jclass rpcResultClass = (*env)->FindClass(env, "com/nabto/api/RpcResult");
    if (rpcResultClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for the RpcResult(String, int) constructor
    jmethodID cid = (*env)->GetMethodID(env, rpcResultClass, "<init>",
                                        "(Ljava/lang/String;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Call constructor and return the resulting RpcResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, rpcResultClass, cid, errorMessageBuffer,
                             nabtoStatus);
}

jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcSetInterface(JNIEnv* env,
                                                                         jclass thiz,
                                                                         jstring host,
                                                                         jstring interfaceDefinition,
                                                                         jobject sessionObj) {
    // Convert Java string into native string
    const char* hostNative = (*env)->GetStringUTFChars(env, host, 0);
    if (hostNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    const char* interfaceDefinitionNative = (*env)->GetStringUTFChars(env, interfaceDefinition, 0);
    if (interfaceDefinitionNative == NULL) {  // Was there enough memory?
        (*env)->ReleaseStringUTFChars(env, host, hostNative);  // Free the allocated host
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    char* errorMessageBufferNative;

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    // set interface
    nabto_status_t nabtoStatus = nabtoRpcSetInterface(session, hostNative,
        interfaceDefinitionNative, &errorMessageBufferNative);

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, host, hostNative);
    (*env)->ReleaseStringUTFChars(env, interfaceDefinition, interfaceDefinitionNative);

    jstring errorMessageBuffer = NULL;
    if (nabtoStatus == NABTO_FAILED_WITH_JSON_MESSAGE) {
        errorMessageBuffer = (*env)->NewStringUTF(env, errorMessageBufferNative);
        if (errorMessageBuffer == NULL) {  // Was there enough memory
            nabtoFree(errorMessageBuffer);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }
        nabtoFree(errorMessageBufferNative);  // Free the result as it is now in
                                              // a java equivalent
    }

    // Now create a RpcResult object and return it
    // Find the class com.nabto.api.RpcResult
    jclass rpcResultClass = (*env)->FindClass(env, "com/nabto/api/RpcResult");
    if (rpcResultClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for the RpcResult(String, int) constructor
    jmethodID cid = (*env)->GetMethodID(env, rpcResultClass, "<init>",
                                        "(Ljava/lang/String;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Call constructor and return the resulting RpcResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, rpcResultClass, cid, errorMessageBuffer,
                             nabtoStatus);
}

jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcInvoke(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring nabtoUrl,
                                                                   jobject sessionObj) {
    // Convert Java string into native string
    const char* nabtoUrlNative = (*env)->GetStringUTFChars(env, nabtoUrl, 0);
    if (nabtoUrlNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    char* jsonResponseBufferNative;

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    // set default interface
    nabto_status_t nabtoStatus =
        nabtoRpcInvoke(session, nabtoUrlNative, &jsonResponseBufferNative);

    // Release string again
    (*env)->ReleaseStringUTFChars(env, nabtoUrl, nabtoUrlNative);

    jstring jsonResponseBuffer = NULL;
    if (nabtoStatus == NABTO_OK) {
        jsonResponseBuffer = (*env)->NewStringUTF(env, jsonResponseBufferNative);
        if (jsonResponseBuffer == NULL) {  // Was there enough memory?
            nabtoFree(jsonResponseBuffer);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }
        nabtoFree(jsonResponseBufferNative);  // Free the result as it is now in
                                              // a java equivalent
    }

    // Now create a RpcResult object and return it
    // Find the class com.nabto.api.RpcResult
    jclass rpcResultClass = (*env)->FindClass(env, "com/nabto/api/RpcResult");
    if (rpcResultClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for the RpcResult(String, int) constructor
    jmethodID cid = (*env)->GetMethodID(env, rpcResultClass, "<init>",
                                        "(Ljava/lang/String;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Call constructor and return the resulting RpcResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, rpcResultClass, cid, jsonResponseBuffer,
                             nabtoStatus);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoFetchUrl(JNIEnv* env,
                                                          jclass thiz,
                                                          jstring url,
                                                          jobject sessionObj) {
    // Convert Java string into native string
    const char* urlNative = (*env)->GetStringUTFChars(env, url, 0);
    if (urlNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    char* resultBufferNative;
    size_t resultLen;
    char* mimeTypeBufferNative;

    JNILOG("%s URL: %s", "Before fetch", urlNative)

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    // Fetch the url
    nabto_status_t nabtoStatus =
        nabtoFetchUrl(session, urlNative, &resultBufferNative, &resultLen,
                      &mimeTypeBufferNative);

    JNILOG("%s URL: %s Result length: %zu NabtoStatus: %i", "After fetch",
           urlNative, resultLen, nabtoStatus)

    // Release string again
    (*env)->ReleaseStringUTFChars(env, url, urlNative);

    jstring mimeTypeBuffer = NULL;
    jbyteArray resultBuffer = NULL;
    if (nabtoStatus == NABTO_OK) {
        JNILOG("%s %s", "Before mimetype", mimeTypeBufferNative)
        mimeTypeBuffer = (*env)->NewStringUTF(env, mimeTypeBufferNative);
        if (mimeTypeBuffer == NULL) {  // Was there enough memory?
            nabtoFree(mimeTypeBufferNative);  // Free before returning from exception
            nabtoFree(resultBufferNative);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }
        nabtoFree(mimeTypeBufferNative);  // Free the result as it is now in a
                                          // java equivalent
        JNILOG("%s", "After mimetype, before byte array")

        // Create the char array into a java byte array
        resultBuffer = (*env)->NewByteArray(env, resultLen);
        if (resultBuffer == NULL) {  // Was there enough memory?
            nabtoFree(resultBufferNative);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }

        // Put the contents into the array
        (*env)->SetByteArrayRegion(env, resultBuffer, 0, resultLen,
                                   (jbyte*)resultBufferNative);
        nabtoFree(resultBufferNative);  // Free the result as it is now in a
                                        // java equivalent

        JNILOG("%s", "After byte array")
    }

    JNILOG("%s", "Before fetch finding class")

    // Now create a UrlResult object and return it
    // Find the class com.nabto.api.UrlResult
    jclass urlResultClass = (*env)->FindClass(env, "com/nabto/api/UrlResult");
    if (urlResultClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for the String(byte[], String, int) constructor
    jmethodID cid = (*env)->GetMethodID(env, urlResultClass, "<init>",
                                        "([BLjava/lang/String;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    JNILOG("%s", "Before fetch return")

    // Call constructor and return the resulting UrlResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, urlResultClass, cid, resultBuffer,
                             mimeTypeBuffer, nabtoStatus);
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoLookupExistingProfile(JNIEnv* env,
                                                                       jclass thiz) {
    char* emailNative;
    // nabtoStatus = NABTO_NO_PROFILE if no profiles exists, NABTO_OK if one was
    // found
    nabto_status_t nabtoStatus = nabtoLookupExistingProfile(&emailNative);
    if (nabtoStatus == NABTO_OK) {
        jstring email = (*env)->NewStringUTF(env, emailNative);
        nabtoFree(&emailNative);  // Free before returning from exception
        if (email == NULL)  // Was there enough memory?
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        return email;
    } else {
        // No profiles installed. Signal this by returning NULL
        // This will not throw an exception.
        return NULL;
    }
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoCreateProfile(JNIEnv* env,
                                                            jclass thiz,
                                                            jstring email,
                                                            jstring password) {
    // Convert Java string into native string
    const char* emailNative = (*env)->GetStringUTFChars(env, email, 0);
    if (emailNative == NULL)  // Was there enough memory?
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.

    const char* passwordNative = (*env)->GetStringUTFChars(env, password, 0);
    if (passwordNative == NULL) {  // Was there enough memory?
        (*env)->ReleaseStringUTFChars(env, email,
                                      emailNative);  // Free the allocated email
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.
    }

    nabto_status_t nabtoStatus = nabtoCreateProfile(emailNative, passwordNative);

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, email, emailNative);
    (*env)->ReleaseStringUTFChars(env, password, passwordNative);

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoCreateSelfSignedProfile(JNIEnv* env,
                                                            jclass thiz,
                                                            jstring email,
                                                            jstring password) {
    // Convert Java string into native string
    const char* emailNative = (*env)->GetStringUTFChars(env, email, 0);
    if (emailNative == NULL)  // Was there enough memory?
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.

    const char* passwordNative = (*env)->GetStringUTFChars(env, password, 0);
    if (passwordNative == NULL) {  // Was there enough memory?
        (*env)->ReleaseStringUTFChars(env, email,
                                      emailNative);  // Free the allocated email
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.
    }

    nabto_status_t nabtoStatus = nabtoCreateSelfSignedProfile(emailNative, passwordNative);

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, email, emailNative);
    (*env)->ReleaseStringUTFChars(env, password, passwordNative);

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoSignup(JNIEnv* env,
                                                     jclass thiz,
                                                     jstring email,
                                                     jstring password) {
    // Convert Java string into native string
    const char* emailNative = (*env)->GetStringUTFChars(env, email, 0);
    if (emailNative == NULL)  // Was there enough memory?
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.

    const char* passwordNative = (*env)->GetStringUTFChars(env, password, 0);
    if (passwordNative == NULL)  // Was there enough memory?
    {
        (*env)->ReleaseStringUTFChars(env, email,
                                      emailNative);  // Free the allocated email
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.
    }

    nabto_status_t nabtoStatus = nabtoSignup(emailNative, passwordNative);

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, email, emailNative);
    (*env)->ReleaseStringUTFChars(env, password, passwordNative);

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoResetAccountPassword(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring email) {
    // Convert Java string into native string
    const char* emailNative = (*env)->GetStringUTFChars(env, email, 0);
    if (emailNative == NULL)  // Was there enough memory?
        return -1;  // If not return. This will throw OutOfMemoryError on Java
                    // side since exception was not cleared.

    nabto_status_t nabtoStatus = nabtoResetAccountPassword(emailNative);

    // Release string again
    (*env)->ReleaseStringUTFChars(env, email, emailNative);

    return nabtoStatus;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoProbeNetwork(JNIEnv* env,
                                                           jclass thiz,
                                                           jint timeoutMillis,
                                                           jstring hostname) {
    // Check if hostname is specified
    if (hostname == NULL) {
        return nabtoProbeNetwork(timeoutMillis, NULL);
    } else {
        // Convert Java string into native string
        const char* hostnameNative = (*env)->GetStringUTFChars(env, hostname, 0);
        if (hostnameNative == NULL)  // Was there enough memory?
            return -1;  // If not return. This will throw OutOfMemoryError on
                        // Java side since exception was not cleared.

        nabto_status_t nabtoStatus = nabtoProbeNetwork(timeoutMillis, hostnameNative);

        // Release string again
        (*env)->ReleaseStringUTFChars(env, hostname, hostnameNative);

        return nabtoStatus;
    }
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSubmitPostData(JNIEnv* env,
                                                                jclass thiz,
                                                                jstring nabtoUrl,
                                                                jbyteArray postData,
                                                                jstring postMimeType,
                                                                jobject sessionObj) {
    // Convert Java string into native string
    const char* nabtoUrlNative = (*env)->GetStringUTFChars(env, nabtoUrl, 0);
    if (nabtoUrlNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    const char* postMimeTypeNative = (*env)->GetStringUTFChars(env, postMimeType, 0);
    if (postMimeTypeNative == NULL)  // Was there enough memory?
    {
        (*env)->ReleaseStringUTFChars(env, nabtoUrl,
                                      nabtoUrlNative);  // Free the allocated url
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    jbyte* postDataNative = (*env)->GetByteArrayElements(env, postData, NULL);
    if (postDataNative == NULL)  // Was there enough memory?
    {
        (*env)->ReleaseStringUTFChars(env, nabtoUrl,
                                      nabtoUrlNative);  // Free the allocated url
        (*env)->ReleaseStringUTFChars(env, postMimeType,
                                      postMimeTypeNative);  // Free the allocated mimetype
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    char* resultBufferNative;
    size_t resultLen;
    char* mimeTypeBufferNative;

    JNILOG("%s URL: %s", "Before post", nabtoUrlNative)

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    nabto_status_t nabtoStatus = nabtoSubmitPostData(session,
                                                     nabtoUrlNative,
                                                     (char*)postDataNative,
                                                     (*env)->GetArrayLength(env, postData),
                                                     postMimeTypeNative,
                                                     &resultBufferNative,
                                                     &resultLen,
                                                     &mimeTypeBufferNative);

    JNILOG("%s URL: %s Result length: %zu NabtoStatus: %i", "After fetch",
           nabtoUrlNative, resultLen, nabtoStatus)

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, nabtoUrl, nabtoUrlNative);
    (*env)->ReleaseStringUTFChars(env, postMimeType, postMimeTypeNative);

    // Release post data again
    (*env)->ReleaseByteArrayElements(env, postData, postDataNative, 0);

    jstring mimeTypeBuffer = NULL;
    jbyteArray resultBuffer = NULL;
    if (nabtoStatus == NABTO_OK) {
        JNILOG("%s %s", "Before mimetype", mimeTypeBufferNative)
        mimeTypeBuffer = (*env)->NewStringUTF(env, mimeTypeBufferNative);
        if (mimeTypeBuffer == NULL) {  // Was there enough memory?
            nabtoFree(mimeTypeBufferNative);  // Free before returning from exception
            nabtoFree(resultBufferNative);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }
        nabtoFree(mimeTypeBufferNative);  // Free the result as it is now in a
                                          // java equivalent
        JNILOG("%s", "After mimetype, before byte array")

        // Create the char array into a java byte array
        resultBuffer = (*env)->NewByteArray(env, resultLen);
        if (resultBuffer == NULL) {  // Was there enough memory?
            nabtoFree(resultBufferNative);  // Free before returning from exception
            return NULL;  // If not return. This will throw OutOfMemoryError on
                          // Java side since exception was not cleared.
        }

        // Put the contents into the array
        (*env)->SetByteArrayRegion(env, resultBuffer, 0, resultLen,
                                   (jbyte*)resultBufferNative);
        nabtoFree(resultBufferNative);  // Free the result as it is now in a
                                        // java equivalent

        JNILOG("%s", "After byte array")
    }

    JNILOG("%s", "Before fetch finding class")

    // Now create a UrlResult object and return it
    // Find the class com.nabto.api.UrlResult
    jclass urlResultClass = (*env)->FindClass(env, "com/nabto/api/UrlResult");
    if (urlResultClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for the String(byte[], String, int) constructor
    jmethodID cid = (*env)->GetMethodID(env, urlResultClass, "<init>",
                                        "([BLjava/lang/String;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    JNILOG("%s", "Before post return")

    // Call constructor and return the resulting UrlResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, urlResultClass, cid, resultBuffer,
                             mimeTypeBuffer, nabtoStatus);
}

jobjectArray stringArrayToJavaArray(JNIEnv* env,
                                    char** strings,
                                    int stringsLength) {
    jobjectArray ret;
    jclass stringClass = (*env)->FindClass(env, "java/lang/String");

    if (stringClass == NULL) {
        ret = NULL;
    } else {
        ret = (*env)->NewObjectArray(env, stringsLength, stringClass, NULL);
    }

    if (ret != NULL) {
        int i;
        for (i = 0; i < stringsLength; i++) {
            (*env)->SetObjectArrayElement(
                env, ret, i, (*env)->NewStringUTF(env, strings[i]));
        }
    }
    return ret;
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetProtocolPrefixes(JNIEnv* env,
                                                                          jclass thiz) {
    char** prefixes;
    int prefixesLength;

    nabto_status_t status;
    status = nabtoGetProtocolPrefixes(&prefixes, &prefixesLength);
    if (status != NABTO_OK)
        return NULL;
    jobjectArray ret = stringArrayToJavaArray(env, prefixes, prefixesLength);

    int i;
    for (i = 0; i < prefixesLength; i++) {
        nabtoFree(prefixes[i]);
    }
    nabtoFree(prefixes);

    return ret;
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetCertificates(JNIEnv* env,
                                                                      jclass thiz) {
    char** certificates;
    int certificatesLength;

    nabto_status_t status;
    status = nabtoGetCertificates(&certificates, &certificatesLength);
    if (status != NABTO_OK)
        return NULL;
    jobjectArray ret = stringArrayToJavaArray(env, certificates, certificatesLength);

    int i;
    for (i = 0; i < certificatesLength; i++) {
        nabtoFree(certificates[i]);
    }
    nabtoFree(certificates);

    return ret;
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoGetSessionToken(JNIEnv* env,
                                                                 jclass thiz,
                                                                 jobject sessionObj) {
    // 64+1 = 65
    const size_t size = 65;
    char resultBufNative[65];  // Windows vc cannot use size because it's a
                               // variable.
    size_t resultLen;

    JNILOG("%s", "Before get session token")

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    nabto_status_t nabtoStatus = nabtoGetSessionToken(session, resultBufNative,
                                                      size, &resultLen);

    if (nabtoStatus == NABTO_OK && resultLen <= size) {
        // Terminate char array, convert to java string, free buffer and return
        // result
        resultBufNative[resultLen] = 0;
        jstring result = (*env)->NewStringUTF(env, resultBufNative);
        // nabtoFree(resultBufNative);
        return result;
    } else {
        return NULL;
    }
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelOpenTcp(JNIEnv* env,
                                                               jclass thiz,
                                                               jint localPort,
                                                               jstring nabtoHost,
                                                               jstring remoteHost,
                                                               jint remotePort,
                                                               jobject sessionObj) {
    JNILOG("%s", "Opening a tunnel")

    // Convert Java string into native string
    const char* nabtoHostNative = (*env)->GetStringUTFChars(env, nabtoHost, 0);
    if (nabtoHostNative == NULL)  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.

    // Convert Java string into native string
    const char* remoteHostNative = (*env)->GetStringUTFChars(env, remoteHost, 0);
    if (remoteHostNative == NULL) {  // Was there enough memory?
        (*env)->ReleaseStringUTFChars(env, nabtoHost, nabtoHostNative);
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    // Get the session
    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    nabto_tunnel_t tunnel;
    nabto_status_t nabtoStatus;
    nabtoStatus = nabtoTunnelOpenTcp(&tunnel, session, localPort, nabtoHostNative,
                                     remoteHostNative, remotePort);

    JNILOG("Tunnel = %u", (unsigned int)tunnel)
    JNILOG("status = %i", nabtoStatus)

    // Now create a Tunnel object and return it
    // Find the class com.nabto.api.Tunnel
    jclass tunnelClass = (*env)->FindClass(env, "com/nabto/api/Tunnel");
    if (tunnelClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("%s", "Tunnel obj is created")
    // Get the method ID for constructor
    jmethodID cid = (*env)->GetMethodID(env, tunnelClass, "<init>",
                                        "(Ljava/lang/Object;I)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.
    JNILOG("%s", "Got the method ID for constructor")

    // Release strings again
    (*env)->ReleaseStringUTFChars(env, nabtoHost, nabtoHostNative);
    (*env)->ReleaseStringUTFChars(env, remoteHost, remoteHostNative);
    JNILOG("%s", "strings is released")

    jobject tunnelDirectBytebuffer = (*env)->NewDirectByteBuffer(env, (void*)tunnel,
                                                                 sizeof(nabto_tunnel_t));

    // Call constructor and return the resulting Tunnel object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, tunnelClass, cid, tunnelDirectBytebuffer,
                             nabtoStatus);
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelClose(JNIEnv* env,
                                                          jclass thiz,
                                                          jobject tunnelObj) {
    nabto_tunnel_t tunnel;
    tunnel = (nabto_tunnel_t)(*env)->GetDirectBufferAddress(env, tunnelObj);
    JNILOG("Tunnel = %u", (unsigned int)tunnel)

    nabto_status_t nabtoStatus = nabtoTunnelClose(tunnel);
    return nabtoStatus;
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelInfo(JNIEnv* env,
                                                            jclass thiz,
                                                            jobject tunnelObj) {
    nabto_status_t status;
    
    nabto_tunnel_state_t state;
    unsigned short port;
    int lastError;
    unsigned int version;

    nabto_tunnel_t tunnel;
    tunnel = (nabto_tunnel_t)(*env)->GetDirectBufferAddress(env, tunnelObj);

    JNILOG("Tunnel = %u", (unsigned int)tunnel)
    status = nabtoTunnelInfo(tunnel, NTI_STATUS, sizeof(nabto_tunnel_state_t),
                             &state);
    JNILOG("status =%i   state =%i", status, state)

    status = nabtoTunnelInfo(tunnel, NTI_PORT, sizeof(unsigned short), &port);
    status = nabtoTunnelInfo(tunnel, NTI_VERSION, sizeof(unsigned int), &version);
    status = nabtoTunnelInfo(tunnel, NTI_LAST_ERROR, sizeof(int), &lastError);

    // Now create a TunnelInfoResult object and return it
    // Find the class com.nabto.api.TunnelInfoResult
    jclass tunnelInfoClass = (*env)->FindClass(env, "com/nabto/api/TunnelInfoResult");
    if (tunnelInfoClass == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Get the method ID for constructor
    jmethodID cid = (*env)->GetMethodID(env, tunnelInfoClass, "<init>", "(IIIII)V");
    if (cid == NULL)  // Was there an error?
        return NULL;  // If so, return. This will throw the exception on Java
                      // side since exception was not cleared.

    // Call constructor and return the resulting TunnelInfoResult object.
    // If NULL is returned, then exception will be thrown on Java side
    return (*env)->NewObject(env, tunnelInfoClass, cid, version, state, lastError, port, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamOpen(JNIEnv* env,
                                                            jclass thiz,
                                                            jstring nabtoHost,
                                                            jobject sessionObj) {
    JNILOG("%s", "Opening a stream");
    const char* nabtoHostNative = (*env)->GetStringUTFChars(env, nabtoHost, 0);
    if (nabtoHostNative == NULL) {  // Was there enough memory?
        return NULL;  // If not return. This will throw OutOfMemoryError on Java
                      // side since exception was not cleared.
    }

    nabto_handle_t session;
    session = (nabto_handle_t)(*env)->GetDirectBufferAddress(env, sessionObj);

    nabto_stream_t stream;
    nabto_status_t status = nabtoStreamOpen(&stream, session, nabtoHostNative);
    jclass streamClass = (*env)->FindClass(env, "com/nabto/api/Stream");
    if (streamClass == NULL) {
        return NULL;
    }
    jmethodID cid = (*env)->GetMethodID(env, streamClass, "<init>",
                                        "(Ljava/lang/Object;I)V");
    if (cid == NULL) {
        return NULL;
    }
    jobject streamDirectBytebuffer = (*env)->NewDirectByteBuffer(env, (void*)stream,
                                                                 sizeof(nabto_stream_t));

    return (*env)->NewObject(env, streamClass, cid, streamDirectBytebuffer,
                             status);
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamClose(JNIEnv* env,
                                                          jclass thiz,
                                                          jobject streamObj) {
    nabto_stream_t stream;
    stream = (nabto_stream_t)(*env)->GetDirectBufferAddress(env, streamObj);

    nabto_status_t status = nabtoStreamClose(stream);

    return status;
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamRead(JNIEnv* env,
                                                            jclass thiz,
                                                            jobject streamObj) {
    nabto_stream_t stream;
    stream = (nabto_stream_t)(*env)->GetDirectBufferAddress(env, streamObj);

    jclass resultClass =
        (*env)->FindClass(env, "com/nabto/api/StreamReadResult");
    if (resultClass == NULL)
        return NULL;

    jmethodID cid = (*env)->GetMethodID(env, resultClass, "<init>", "([BI)V");
    if (cid == NULL)
        return NULL;

    char* buffer;
    size_t bufferLength = 0;
    nabto_status_t status = nabtoStreamRead(stream, &buffer, &bufferLength);

    jbyteArray array = NULL;
    if(status == NABTO_OK) {
        array = (*env)->NewByteArray(env, bufferLength);
        (*env)->SetByteArrayRegion(env, array, 0, bufferLength, (jbyte*)(buffer));
        nabtoFree(buffer);
    }

    return (*env)->NewObject(env, resultClass, cid, array, status);
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamWrite(JNIEnv* env,
                                                          jclass thiz,
                                                          jbyteArray data,
                                                          jobject streamObj) {
    nabto_stream_t stream;
    stream = (nabto_stream_t)(*env)->GetDirectBufferAddress(env, streamObj);

    int len = (*env)->GetArrayLength(env, data);
    jbyte* buffer = (*env)->GetByteArrayElements(env, data, NULL);

    nabto_status_t status = nabtoStreamWrite(stream, (char*)buffer, len);

    // Release data array again
    (*env)->ReleaseByteArrayElements(env, data, buffer, JNI_ABORT);

    return status;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamConnectionType(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jobject streamObj) {
    nabto_stream_t stream;
    stream = (nabto_stream_t)(*env)->GetDirectBufferAddress(env, streamObj);

    nabto_connection_type_t type;
    nabto_status_t status = nabtoStreamConnectionType(stream, &type);

    if(status != NABTO_OK)
        return -1;

    return type;
}

jint Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamSetOption(JNIEnv* env,
                                                              jclass thiz,
                                                              jint optionName,
                                                              jbyteArray option,
                                                              jobject streamObj) {
    nabto_stream_t stream;
    stream = (nabto_stream_t)(*env)->GetDirectBufferAddress(env, streamObj);

    int len = (*env)->GetArrayLength(env, option);
    jbyte* buffer = (*env)->GetByteArrayElements(env, option, NULL);

    nabto_status_t status = nabtoStreamSetOption(stream, optionName,
                                                 (void*)buffer, len);

    // Release option array again
    (*env)->ReleaseByteArrayElements(env, option, buffer, JNI_ABORT);

    return status;
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoVersion(JNIEnv* env,
                                                         jclass thiz) {
    int major, minor;
    char buf[10] = {0};

    nabtoVersion(&major, &minor);
    sprintf(buf, "%i.%i", major, minor);

    return (*env)->NewStringUTF(env, buf);
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetLocalDevices(JNIEnv* env,
                                                                      jclass thiz) {
    char** devices;
    int numberOfDevices;

    nabto_status_t status = nabtoGetLocalDevices(&devices, &numberOfDevices);
    if (status != NABTO_OK)
        return NULL;
    jobjectArray ret = stringArrayToJavaArray(env, devices, numberOfDevices);

    int i;
    for (i = 0; i < numberOfDevices; i++) {
        nabtoFree(devices[i]);
    }
    nabtoFree(devices);

    return ret;
}
