#include "nabto_client_api.h"
#include "com_nabto_api_NabtoCApiWrapper.h"
#include "jni_string.hpp"
#include "jni_byte_array.hpp"

#include <jni.h>
#include <string>
#include <iostream>

jobjectArray stringArrayToJavaArray(JNIEnv* env, char** strings, size_t stringsLength)
{
    jclass stringClass = env->FindClass("java/lang/String");
    if(stringClass == NULL) return NULL;

    jobjectArray javaArray = env->NewObjectArray(stringsLength, stringClass, NULL);
    if(javaArray == NULL) return NULL;

    for (size_t i = 0; i < stringsLength; i++) {
        env->SetObjectArrayElement(javaArray, i, env->NewStringUTF(strings[i]));
    }

    return javaArray;
}

jbyteArray charArrayToJavaArray(JNIEnv* env, char* array, size_t arrayLength)
{
    jbyteArray javaArray = env->NewByteArray(arrayLength);
    if(javaArray == NULL) return NULL;

    env->SetByteArrayRegion(javaArray, 0, arrayLength, reinterpret_cast<jbyte*>(array));
    return javaArray;
}

jobject toNabtoStatus(JNIEnv* env, int status) {
    jclass nabtoStatusEnum = env->FindClass("com/nabto/api/NabtoStatus");
    if(nabtoStatusEnum == NULL) {
        std::cout << "ERROR: Could not find class com/nabto/api/NabtoStatus" << std::endl;
        return NULL;
    }

    jmethodID fromInteger = env->GetStaticMethodID(
            nabtoStatusEnum, "fromInteger", "(I)Lcom/nabto/api/NabtoStatus;");
    if(fromInteger == NULL) {
        std::cout << "ERROR: Could not convert status " << nabtoStatusEnum << " from integer" << std::endl;
        return NULL;
    }

    return env->CallStaticObjectMethod(nabtoStatusEnum, fromInteger, status);
}

jobject toNabtoConnectionType(JNIEnv* env, int connectionType) {
    jclass nabtoConnectionTypeEnum = env->FindClass("com/nabto/api/NabtoConnectionType");
    if(nabtoConnectionTypeEnum == NULL) return NULL;

    jmethodID fromInteger = env->GetStaticMethodID(
            nabtoConnectionTypeEnum, "fromInteger", "(I)Lcom/nabto/api/NabtoConnectionType;");
    if(fromInteger == NULL) return NULL;

    return env->CallStaticObjectMethod(nabtoConnectionTypeEnum, fromInteger, connectionType);
}

template<typename T>
T getHandle(JNIEnv* env, jobject object) {
    if(object == NULL) return NULL;

    jclass cls = env->GetObjectClass(object);
    if (cls == NULL) return NULL;

    jmethodID getHandleMethod = env->GetMethodID(cls, "getHandle", "()Ljava/lang/Object;");
    if (getHandleMethod == NULL) return NULL;

    jobject handleObject = env->CallObjectMethod(object, getHandleMethod);
    if(handleObject == NULL) return NULL;

    return static_cast<T>(env->GetDirectBufferAddress(handleObject));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStartup(JNIEnv* env,
                                                      jclass thiz,
                                                      jstring nabtoHomeDir)
{
    jni_string nabtoHomeDirNative(env, nabtoHomeDir);
    return toNabtoStatus(env, nabtoStartup(nabtoHomeDirNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSetStaticResourceDir(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring resourceDir)
{
    jni_string resourceDirNative(env, resourceDir);
    return toNabtoStatus(env, nabtoSetStaticResourceDir(resourceDirNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoInstallDefaultStaticResources(JNIEnv* env,
                                                                                jclass thiz,
                                                                                jstring resourceDir)
{
    jni_string resourceDirNative(env, resourceDir);
    return toNabtoStatus(env, nabtoInstallDefaultStaticResources(resourceDirNative));
}


jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoShutdown(JNIEnv* env, jclass thiz)
{
    return toNabtoStatus(env, nabtoShutdown());
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSetOption(JNIEnv* env,
                                                           jclass thiz,
                                                           jstring name,
                                                           jstring value)
{
    jni_string nameNative(env, name);
    jni_string valueNative(env, value);
    return toNabtoStatus(env, nabtoSetOption(nameNative, valueNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoOpenSession(JNIEnv* env,
                                                             jclass thiz,
                                                             jstring id,
                                                             jstring password)
{
    jni_string idNative(env, id);
    jni_string passwordNative(env, password);

    nabto_handle_t sessionHandle;
    nabto_status_t status = nabtoOpenSession(&sessionHandle, idNative, passwordNative);

    jobject sessionHandleObject = NULL;
    if(status == NABTO_OK) {
        sessionHandleObject = env->NewDirectByteBuffer(
                static_cast<void*>(sessionHandle), sizeof(nabto_handle_t));
    }

    jclass sessionClass = env->FindClass("com/nabto/api/Session");
    if(sessionClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(sessionClass, "<init>", "(Ljava/lang/Object;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(sessionClass, constructor, sessionHandleObject, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoOpenSessionBare(JNIEnv* env, jclass thiz)
{
    nabto_handle_t sessionHandle;
    nabto_status_t status = nabtoOpenSessionBare(&sessionHandle);

    jobject sessionHandleObject = NULL;
    if(status == NABTO_OK) {
        sessionHandleObject = env->NewDirectByteBuffer(
                static_cast<void*>(sessionHandle), sizeof(nabto_handle_t));
    }

    jclass sessionClass = env->FindClass("com/nabto/api/Session");
    if(sessionClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(sessionClass, "<init>", "(Ljava/lang/Object;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(sessionClass, constructor, sessionHandleObject, status);
}



jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoCloseSession(JNIEnv* env,
                                                           jclass thiz,
                                                           jobject sessionObject)
{
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);
    return toNabtoStatus(env, nabtoCloseSession(sessionHandle));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSetBasestationAuthJson(JNIEnv* env,
                                                                        jclass thiz,
                                                                        jstring jsonKeyValuePairs,
                                                                        jobject sessionObject)
{
    jni_string jsonStringNative(env, jsonKeyValuePairs);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);
    return toNabtoStatus(env, nabtoSetBasestationAuthJson(sessionHandle, jsonStringNative));
}


jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcSetDefaultInterface(JNIEnv* env,
                                                                                jclass thiz,
                                                                                jstring interfaceDefinition,
                                                                                jobject sessionObject)
{
    jni_string interfaceDefinitionNative(env, interfaceDefinition);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char* errorMessageNative;
    nabto_status_t status = nabtoRpcSetDefaultInterface(
            sessionHandle, interfaceDefinitionNative, &errorMessageNative);

    jstring errorMessage = NULL;
    if(status == NABTO_FAILED_WITH_JSON_MESSAGE) {
        errorMessage = env->NewStringUTF(errorMessageNative);
        nabtoFree(errorMessageNative);
    }

    jclass rpcResultClass = env->FindClass("com/nabto/api/RpcResult");
    if(rpcResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(rpcResultClass, "<init>", "(Ljava/lang/String;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(rpcResultClass, constructor, errorMessage, status);
}

jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcSetInterface(JNIEnv* env,
                                                                         jclass thiz,
                                                                         jstring host,
                                                                         jstring interfaceDefinition,
                                                                         jobject sessionObject)
{
    jni_string hostNative(env, host);
    jni_string interfaceDefinitionNative(env, interfaceDefinition);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char* errorMessageNative;
    nabto_status_t status = nabtoRpcSetInterface(
            sessionHandle, hostNative, interfaceDefinitionNative, &errorMessageNative);

    jstring errorMessage = NULL;
    if(status == NABTO_FAILED_WITH_JSON_MESSAGE) {
        errorMessage = env->NewStringUTF(errorMessageNative);
        nabtoFree(errorMessageNative);
    }

    jclass rpcResultClass = env->FindClass("com/nabto/api/RpcResult");
    if(rpcResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(rpcResultClass, "<init>", "(Ljava/lang/String;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(rpcResultClass, constructor, errorMessage, status);
}

jobject JNICALL Java_com_nabto_api_NabtoCApiWrapper_nabtoRpcInvoke(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring nabtoUrl,
                                                                   jobject sessionObject)
{
    jni_string nabtoUrlNative(env, nabtoUrl);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char* jsonResponseNative;
    nabto_status_t status = nabtoRpcInvoke(sessionHandle, nabtoUrlNative, &jsonResponseNative);

    jstring jsonResponse = env->NewStringUTF(jsonResponseNative);
    nabtoFree(jsonResponseNative);

    jclass rpcResultClass = env->FindClass("com/nabto/api/RpcResult");
    if(rpcResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(rpcResultClass, "<init>", "(Ljava/lang/String;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(rpcResultClass, constructor, jsonResponse, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoFetchUrl(JNIEnv* env,
                                                          jclass thiz,
                                                          jstring nabtoUrl,
                                                          jobject sessionObject)
{
    jni_string nabtoUrlNative(env, nabtoUrl);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char* resultBufferNative;
    size_t resultLen;
    char* mimeTypeBufferNative;
    nabto_status_t status = nabtoFetchUrl(
            sessionHandle, nabtoUrlNative, &resultBufferNative, &resultLen, &mimeTypeBufferNative);

    jbyteArray resultBuffer = NULL;
    jstring mimeTypeBuffer = NULL;
    if(status == NABTO_OK) {
        resultBuffer = charArrayToJavaArray(env, resultBufferNative, resultLen);
        nabtoFree(resultBufferNative);

        mimeTypeBuffer = env->NewStringUTF(mimeTypeBufferNative);
        nabtoFree(mimeTypeBufferNative);
    }

    jclass urlResultClass = env->FindClass("com/nabto/api/UrlResult");
    if(urlResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(urlResultClass, "<init>", "([BLjava/lang/String;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(urlResultClass, constructor, resultBuffer, mimeTypeBuffer, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoCreateProfile(JNIEnv* env,
                                                            jclass thiz,
                                                            jstring email,
                                                            jstring password)
{
    jni_string emailNative(env, email);
    jni_string passwordNative(env, password);
    return toNabtoStatus(env, nabtoCreateProfile(emailNative, passwordNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoCreateSelfSignedProfile(JNIEnv* env,
                                                                      jclass thiz,
                                                                      jstring commonName,
                                                                      jstring password)
{
    jni_string commonNameNative(env, commonName);
    jni_string passwordNative(env, password);
    return toNabtoStatus(env, nabtoCreateSelfSignedProfile(commonNameNative, passwordNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoRemoveProfile(JNIEnv* env,
                                                               jclass thiz,
                                                               jstring certId)
{
    jni_string certIdNative(env, certId);
    return toNabtoStatus(env, nabtoRemoveProfile(certIdNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoGetFingerprint(JNIEnv* env,
                                                                jclass thiz,
                                                                jstring certId,
                                                                jbyteArray fingerprint){
    jni_string certIdNative(env,certId);
    char fingerprintNative[16];
    nabto_status_t nabtoStatus = nabtoGetFingerprint(certIdNative,fingerprintNative);
    env->SetByteArrayRegion(fingerprint, 0, 16, (jbyte*)fingerprintNative);
    return toNabtoStatus(env,nabtoStatus);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSignup(JNIEnv* env,
                                                     jclass thiz,
                                                     jstring email,
                                                     jstring password)
{
    jni_string emailNative(env, email);
    jni_string passwordNative(env, password);
    return toNabtoStatus(env, nabtoSignup(emailNative, passwordNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoResetAccountPassword(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jstring email)
{
    jni_string emailNative(env, email);
    return toNabtoStatus(env, nabtoResetAccountPassword(emailNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoProbeNetwork(JNIEnv* env,
                                                           jclass thiz,
                                                           jint timeoutMillis,
                                                           jstring host)
{
    jni_string hostNative(env, host);
    return toNabtoStatus(env, nabtoProbeNetwork(timeoutMillis, hostNative));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoSubmitPostData(JNIEnv* env,
                                                                jclass thiz,
                                                                jstring nabtoUrl,
                                                                jbyteArray postData,
                                                                jstring postMimeType,
                                                                jobject sessionObject)
{
    jni_string nabtoUrlNative(env, nabtoUrl);
    jni_string postMimeTypeNative(env, postMimeType);
    jni_byte_array postDataNative(env, postData);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char* resultBufferNative;
    size_t resultLen;
    char* resultMimeTypeBufferNative;
    nabto_status_t status = nabtoSubmitPostData(sessionHandle,
            nabtoUrlNative, postDataNative, postDataNative.length(), postMimeTypeNative,
            &resultBufferNative, &resultLen, &resultMimeTypeBufferNative);

    jbyteArray resultBuffer = NULL;
    jstring resultMimeTypeBuffer = NULL;
    if(status == NABTO_OK) {
        resultBuffer = charArrayToJavaArray(env, resultBufferNative, resultLen);
        nabtoFree(resultBufferNative);

        resultMimeTypeBuffer = env->NewStringUTF(resultMimeTypeBufferNative);
        nabtoFree(resultMimeTypeBufferNative);
    }

    jclass urlResultClass = env->FindClass("com/nabto/api/UrlResult");
    if(urlResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(urlResultClass, "<init>", "([BLjava/lang/String;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(urlResultClass, constructor, resultBuffer, resultMimeTypeBuffer, status);
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetProtocolPrefixes(JNIEnv* env, jclass thiz)
{
    char** prefixesNative;
    int prefixesLength;
    nabto_status_t status = nabtoGetProtocolPrefixes(&prefixesNative, &prefixesLength);

    jobjectArray prefixes = NULL;
    if(status == NABTO_OK) {
        prefixes = stringArrayToJavaArray(env, prefixesNative, prefixesLength);
        for (int i = 0; i < prefixesLength; i++)
            nabtoFree(prefixesNative[i]);
        nabtoFree(prefixesNative);
    }
    return prefixes;
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetCertificates(JNIEnv* env, jclass thiz)
{
    char** certificatesNative;
    int certificatesLength;
    nabto_status_t status = nabtoGetCertificates(&certificatesNative, &certificatesLength);

    jobjectArray certificates = NULL;
    if(status == NABTO_OK) {
        certificates = stringArrayToJavaArray(env, certificatesNative, certificatesLength);
        for (int i = 0; i < certificatesLength; i++)
            nabtoFree(certificatesNative[i]);
        nabtoFree(certificatesNative);
    }
    return certificates;
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoGetSessionToken(JNIEnv* env,
                                                                 jclass thiz,
                                                                 jobject sessionObject)
{
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    char bufferNative[65]; // 64 + 1
    const size_t bufLen = 65;
    size_t resultLen;
    nabto_status_t status = nabtoGetSessionToken(sessionHandle, bufferNative, bufLen, &resultLen);

    jstring buffer = NULL;
    if (status == NABTO_OK && resultLen < bufLen) {
        bufferNative[resultLen] = '\0'; // terminate char array
        buffer = env->NewStringUTF(bufferNative);
    }
    return buffer;
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelOpenTcp(JNIEnv* env,
                                                               jclass thiz,
                                                               jint localPort,
                                                               jstring nabtoHost,
                                                               jstring remoteHost,
                                                               jint remotePort,
                                                               jobject sessionObject)
{
    jni_string nabtoHostNative(env, nabtoHost);
    jni_string remoteHostNative(env, remoteHost);
    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);

    nabto_tunnel_t tunnelHandle;
    nabto_status_t status = nabtoTunnelOpenTcp(
            &tunnelHandle, sessionHandle, localPort, nabtoHostNative, remoteHostNative, remotePort);

    jobject tunnelHandleObject = NULL;
    if(status == NABTO_OK) {
        tunnelHandleObject = env->NewDirectByteBuffer(
                static_cast<void*>(tunnelHandle), sizeof(nabto_tunnel_t));
    }

    jclass tunnelClass = env->FindClass("com/nabto/api/Tunnel");
    if(tunnelClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(tunnelClass, "<init>", "(Ljava/lang/Object;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(tunnelClass, constructor, tunnelHandleObject, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelClose(JNIEnv* env,
                                                          jclass thiz,
                                                          jobject tunnelObject)
{
    nabto_tunnel_t tunnelHandle = getHandle<nabto_tunnel_t>(env, tunnelObject);
    return toNabtoStatus(env, nabtoTunnelClose(tunnelHandle));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoTunnelInfo(JNIEnv* env,
                                                            jclass thiz,
                                                            jobject tunnelObject)
{
    nabto_tunnel_t tunnelHandle = getHandle<nabto_tunnel_t>(env, tunnelObject);

    nabto_tunnel_state_t state = NTCS_UNKNOWN;
    nabto_status_t status = nabtoTunnelInfo(tunnelHandle, NTI_STATUS, sizeof(state), &state);

    unsigned short port = 0;
    nabtoTunnelInfo(tunnelHandle, NTI_PORT, sizeof(port), &port);

    unsigned int version = 0;
    nabtoTunnelInfo(tunnelHandle, NTI_VERSION, sizeof(version), &version);

    int lastError = 0;
    nabtoTunnelInfo(tunnelHandle, NTI_LAST_ERROR, sizeof(lastError), &lastError);

    jclass tunnelInfoResultClass = env->FindClass("com/nabto/api/TunnelInfoResult");
    if(tunnelInfoResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(tunnelInfoResultClass, "<init>", "(IIIII)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(
            tunnelInfoResultClass, constructor, version, state, lastError, port, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamOpen(JNIEnv* env,
                                                            jclass thiz,
                                                            jstring nabtoHost,
                                                            jobject sessionObject)
{
    jni_string nabtoHostNative(env, nabtoHost);

    nabto_handle_t sessionHandle = getHandle<nabto_handle_t>(env, sessionObject);
    nabto_stream_t streamHandle;
    nabto_status_t status = nabtoStreamOpen(&streamHandle, sessionHandle, nabtoHostNative);

    jobject streamHandleObject = NULL;
    if(status == NABTO_OK) {
        streamHandleObject = env->NewDirectByteBuffer(
                static_cast<void*>(streamHandle), sizeof(nabto_stream_t));
    }

    jclass streamClass = env->FindClass("com/nabto/api/Stream");
    if(streamClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(streamClass, "<init>", "(Ljava/lang/Object;I)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(streamClass, constructor, streamHandleObject, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamClose(JNIEnv* env,
                                                          jclass thiz,
                                                          jobject streamObject)
{
    nabto_stream_t streamHandle = getHandle<nabto_stream_t>(env, streamObject);
    return toNabtoStatus(env, nabtoStreamClose(streamHandle));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamRead(JNIEnv* env,
                                                            jclass thiz,
                                                            jobject streamObject)
{
    nabto_stream_t streamHandle = getHandle<nabto_stream_t>(env, streamObject);

    char* resultBufferNative;
    size_t resultBufferLength;
    nabto_status_t status = nabtoStreamRead(streamHandle, &resultBufferNative, &resultBufferLength);

    jbyteArray resultBuffer = NULL;
    if(status == NABTO_OK) {
        resultBuffer = charArrayToJavaArray(env, resultBufferNative, resultBufferLength);
        nabtoFree(resultBufferNative);
    }

    jclass streamReadResultClass = env->FindClass("com/nabto/api/StreamReadResult");
    if(streamReadResultClass == NULL) return NULL;
    jmethodID constructor = env->GetMethodID(streamReadResultClass, "<init>", "([BI)V");
    if(constructor == NULL) return NULL;
    return env->NewObject(streamReadResultClass, constructor, resultBuffer, status);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamWrite(JNIEnv* env,
                                                          jclass thiz,
                                                          jbyteArray data,
                                                          jobject streamObject)
{
    jni_byte_array bufferNative(env, data);
    nabto_stream_t streamHandle = getHandle<nabto_stream_t>(env, streamObject);

    return toNabtoStatus(env, nabtoStreamWrite(streamHandle, bufferNative, bufferNative.length()));
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamConnectionType(JNIEnv* env,
                                                                   jclass thiz,
                                                                   jobject streamObject)
{
    nabto_stream_t streamHandle = getHandle<nabto_stream_t>(env, streamObject);

    nabto_connection_type_t type;
    nabto_status status = nabtoStreamConnectionType(streamHandle, &type);
    return toNabtoConnectionType(env, (status == NABTO_OK) ? type : -1);
}

jobject Java_com_nabto_api_NabtoCApiWrapper_nabtoStreamSetOption(JNIEnv* env,
                                                              jclass thiz,
                                                              jint optionName,
                                                              jbyteArray option,
                                                              jobject streamObject)
{
    jni_byte_array optionNative(env, option);
    nabto_stream_option_t streamOption = static_cast<nabto_stream_option_t>(optionName);
    nabto_stream_t streamHandle = getHandle<nabto_stream_t>(env, streamObject);

    return toNabtoStatus(env, nabtoStreamSetOption(streamHandle, streamOption,
            reinterpret_cast<void*>(optionNative.data()), optionNative.length()));
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoVersion(JNIEnv* env, jclass thiz)
{
    int major, minor;
    nabtoVersion(&major, &minor);

    std::string versionString = std::to_string(major) + "." + std::to_string(minor);
    return env->NewStringUTF(versionString.c_str());
}

jstring Java_com_nabto_api_NabtoCApiWrapper_nabtoVersionString(JNIEnv* env, jclass thiz)
{
    char* version;
    if (nabtoVersionString(&version) == NABTO_OK) {
        jstring res = env->NewStringUTF(version);
        nabtoFree(version);
        return res;
    } else {
        return NULL;
    }
}

jobjectArray Java_com_nabto_api_NabtoCApiWrapper_nabtoGetLocalDevices(JNIEnv* env, jclass thiz)
{
    char** devicesNative;
    int numberOfDevices;
    nabto_status_t status = nabtoGetLocalDevices(&devicesNative, &numberOfDevices);

    jobjectArray devices = NULL;
    if(status == NABTO_OK) {
        devices = stringArrayToJavaArray(env, devicesNative, numberOfDevices);
        for (int i = 0; i < numberOfDevices; i++)
            nabtoFree(devicesNative[i]);
        nabtoFree(devicesNative);
    }
    return devices;
}
