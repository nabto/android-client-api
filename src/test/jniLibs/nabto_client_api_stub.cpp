#include "nabto_client_api_stub_controller.hpp"
#include "nabto_client_api.h"

#include <string>
#include <string.h>
#include <sstream>
#include <iostream>

nabto_status_t NABTOAPI nabtoFree(void* p)
{
    if (p)
        free(p);
    return NABTO_OK;
}

nabto_status_t NABTOAPI nabtoVersion(int* major, int* minor)
{
    parameterValues.clear();
    *major = 11;
    *minor = 22;
    return NABTO_OK;
}

nabto_status_t NABTOAPI nabtoStartup(const char* nabtoHomeDir)
{
    parameterValues.clear();
    if(nabtoHomeDir == NULL) {
        parameterValues["nabtoHomeDir"] = "default";
    } else {
        parameterValues["nabtoHomeDir"] = nabtoHomeDir;
    }    
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoShutdown()
{
    parameterValues.clear();
    return NABTO_OK;
}

nabto_status_t NABTOAPI nabtoSetStaticResourceDir(const char* resourceDir)
{
    parameterValues.clear();
    if(resourceDir != NULL) parameterValues["resourceDir"] = resourceDir;
    return NABTO_OK;
}

nabto_status_t NABTOAPI nabtoGetProtocolPrefixes(char*** prefixes, int* prefixesLength)
{
    parameterValues.clear();
    *prefixesLength = std::stoi(returnValues["prefixesLength"]);
    *prefixes = new char*[*prefixesLength];
    for(int i = 0; i < *prefixesLength; ++i) {
        (*prefixes)[i] = new char[3];
        (*prefixes)[i][0] = 'p';
        (*prefixes)[i][1] = '0' + i;
        (*prefixes)[i][2] = '\0';
    }
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}
                                                        
nabto_status_t NABTOAPI nabtoGetLocalDevices(char*** devices, int* numberOfDevices)
{
    parameterValues.clear();
    *numberOfDevices = std::stoi(returnValues["numberOfDevices"]);
    *devices = new char*[*numberOfDevices];
    for(int i = 0; i < *numberOfDevices; ++i) {
        (*devices)[i] = new char[3];
        (*devices)[i][0] = 'd';
        (*devices)[i][1] = '0' + i;
        (*devices)[i][2] = '\0';
    }
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoProbeNetwork(size_t timeoutMillis, const char* host)
{
    parameterValues.clear();
    parameterValues["timeoutMillis"] = std::to_string(timeoutMillis);
    if(host != NULL) parameterValues["host"] = host;
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoGetCertificates(char*** certificates, int* certificatesLength)
{
    parameterValues.clear();
    *certificatesLength = std::stoi(returnValues["certificatesLength"]);
    *certificates = new char*[*certificatesLength];
    for(int i = 0; i < *certificatesLength; ++i) {
        (*certificates)[i] = new char[3];
        (*certificates)[i][0] = 'c';
        (*certificates)[i][1] = '0' + i;
        (*certificates)[i][2] = '\0';
    }
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}


nabto_status_t NABTOAPI nabtoCreateProfile(const char* email, const char* password)
{
    parameterValues.clear();
    if(email != NULL) parameterValues["email"] = email;
    if(password != NULL) parameterValues["password"] = password;
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoCreateSelfSignedProfile(const char* commonName, const char* password)
{
    parameterValues.clear();
    if(commonName != NULL) parameterValues["commonName"] = commonName;
    if(password != NULL) parameterValues["password"] = password;
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoSignup(const char* email, const char* password)
{
    parameterValues.clear();
    if(email != NULL) parameterValues["email"] = email;
    if(password != NULL) parameterValues["password"] = password;
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoResetAccountPassword(const char* email)
{
    parameterValues.clear();
    if(email != NULL) parameterValues["email"] = email;
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoOpenSession(nabto_handle_t* session,
                                         const char* id,
                                         const char* password)
{
    parameterValues.clear();
    if(id != NULL) parameterValues["id"] = id;
    if(password != NULL) parameterValues["password"] = password;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        size_t handle = 42;
        *session = reinterpret_cast<nabto_handle_t>(handle);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoOpenSessionBare(nabto_handle_t* session)
{
    parameterValues.clear();

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        size_t handle = 42;
        *session = reinterpret_cast<nabto_handle_t>(handle);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoCloseSession(nabto_handle_t session)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoRpcSetDefaultInterface(nabto_handle_t session, 
                                                    const char* interfaceDefinition, 
                                                    char** errorMessage)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    if(interfaceDefinition != NULL) parameterValues["interfaceDefinition"] = interfaceDefinition;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_FAILED_WITH_JSON_MESSAGE) {
        const char* str = "error";
        *errorMessage = new char[strlen(str) + 1];
        strncpy(*errorMessage, str, strlen(str) + 1);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoRpcSetInterface(nabto_handle_t session, 
                                             const char* host, 
                                             const char* interfaceDefinition, 
                                             char** errorMessage)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    if(host != NULL) parameterValues["host"] = host;
    if(interfaceDefinition != NULL) parameterValues["interfaceDefinition"] = interfaceDefinition;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_FAILED_WITH_JSON_MESSAGE) {
        const char* str = "error";
        *errorMessage = new char[strlen(str) + 1];
        strncpy(*errorMessage, str, strlen(str) + 1);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoRpcInvoke(nabto_handle_t session,
                                       const char* nabtoUrl,
                                       char** jsonResponse)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    if(nabtoUrl != NULL) parameterValues["nabtoUrl"] = nabtoUrl;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    const char* str = "json";
    *jsonResponse = new char[strlen(str) + 1];
    strncpy(*jsonResponse, str, strlen(str) + 1);

    return status;
}

nabto_status_t NABTOAPI nabtoFetchUrl(nabto_handle_t session, 
                                      const char* nabtoUrl, 
                                      char** resultBuffer, 
                                      size_t* resultLen, 
                                      char** mimeTypeBuffer)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    if(nabtoUrl != NULL) parameterValues["nabtoUrl"] = nabtoUrl;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        const char* bufStr = "content";
        *resultLen = strlen(bufStr);
        *resultBuffer = new char[*resultLen];
        strncpy(*resultBuffer, bufStr, *resultLen);

        const char* mimeStr = "mime type";
        *mimeTypeBuffer = new char[strlen(mimeStr) + 1];
        strncpy(*mimeTypeBuffer, mimeStr, strlen(mimeStr) + 1);
    }

    return status;
}


nabto_status_t NABTOAPI nabtoSubmitPostData(nabto_handle_t session, 
                                            const char* nabtoUrl, 
                                            const char* postBuffer,
                                            size_t postLen,
                                            const char* postMimeType,
                                            char** resultBuffer,
                                            size_t* resultLen,
                                            char** resultMimeTypeBuffer)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    if(nabtoUrl != NULL) parameterValues["nabtoUrl"] = nabtoUrl;
    parameterValues["postBuffer"] = std::string(postBuffer, postLen);
    parameterValues["postLen"] = std::to_string(postLen);
    if(postMimeType != NULL) parameterValues["postMimeType"] = postMimeType;

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        const char* bufStr = "content";
        *resultLen = strlen(bufStr);
        *resultBuffer = new char[*resultLen];
        strncpy(*resultBuffer, bufStr, *resultLen);

        const char* mimeStr = "mime type";
        *resultMimeTypeBuffer = new char[strlen(mimeStr) + 1];
        strncpy(*resultMimeTypeBuffer, mimeStr, strlen(mimeStr) + 1);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoGetSessionToken(nabto_handle_t session,
                                             char* buffer,
                                             size_t bufLen,
                                             size_t* resultLen)
{
    parameterValues.clear();
    size_t handle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(handle);
    parameterValues["bufLen"] = std::to_string(bufLen);

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK && bufLen == 65) {
        memset(buffer, 'x', 64);
        * resultLen = 64;
    }

    return status;
}

nabto_status_t NABTOAPI nabtoStreamOpen(nabto_stream_t* stream,
                                        nabto_handle_t session,
                                        const char* serverId)
{
    parameterValues.clear();
    size_t streamHandle = 43;
    *stream = reinterpret_cast<nabto_stream_t>(streamHandle);

    size_t sessionHandle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(sessionHandle);

    if(serverId != NULL) parameterValues["serverId"] = serverId;

    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoStreamClose(nabto_stream_t stream)
{
    parameterValues.clear();
    size_t streamHandle = reinterpret_cast<size_t>(stream);
    parameterValues["streamHandle"] = std::to_string(streamHandle);
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoStreamRead(nabto_stream_t stream,
                                        char** resultBuffer,
                                        size_t* resultLen)
{
    parameterValues.clear();
    size_t streamHandle = reinterpret_cast<size_t>(stream);
    parameterValues["streamHandle"] = std::to_string(streamHandle);

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        const char* bufStr = "data";
        *resultLen = strlen(bufStr);
        *resultBuffer = new char[*resultLen];
        strncpy(*resultBuffer, bufStr, *resultLen);
    }

    return status;
}

nabto_status_t NABTOAPI nabtoStreamWrite(nabto_stream_t stream, const char* buf, size_t len)
{
    parameterValues.clear();
    size_t streamHandle = reinterpret_cast<size_t>(stream);
    parameterValues["streamHandle"] = std::to_string(streamHandle);
    parameterValues["buf"] = std::string(buf, len);
    parameterValues["len"] = std::to_string(len);
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoStreamConnectionType(nabto_stream_t stream,
                                                  nabto_connection_type_t* type)
{
    parameterValues.clear();
    size_t streamHandle = reinterpret_cast<size_t>(stream);
    parameterValues["streamHandle"] = std::to_string(streamHandle);

    *type = NCT_P2P;

    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoStreamSetOption(nabto_stream_t stream,
                                             nabto_stream_option_t optionName,
                                             const void *optionValue,
                                             size_t optionLength)
{
    parameterValues.clear();
    size_t streamHandle = reinterpret_cast<size_t>(stream);
    parameterValues["streamHandle"] = std::to_string(streamHandle);
    parameterValues["optionName"] = std::to_string(optionName);
    parameterValues["optionValue"] = std::string(static_cast<const char*>(optionValue), optionLength);
    parameterValues["optionLength"] = std::to_string(optionLength);
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoTunnelOpenTcp(nabto_tunnel_t* tunnel,
                                           nabto_handle_t session,
                                           int localPort,
                                           const char* nabtoHost,
                                           const char* remoteHost,
                                           int remotePort)
{
    parameterValues.clear();
    size_t tunnelHandle = 44;
    *tunnel = reinterpret_cast<nabto_tunnel_t>(tunnelHandle);

    size_t sessionHandle = reinterpret_cast<size_t>(session);
    parameterValues["sessionHandle"] = std::to_string(sessionHandle);

    parameterValues["localPort"] = std::to_string(localPort);
    if(nabtoHost != NULL) parameterValues["nabtoHost"] = nabtoHost;
    if(remoteHost != NULL) parameterValues["remoteHost"] = remoteHost;
    parameterValues["remotePort"] = std::to_string(remotePort);

    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoTunnelClose(nabto_tunnel_t tunnel) {
    parameterValues.clear();
    size_t tunnelHandle = reinterpret_cast<size_t>(tunnel);
    parameterValues["tunnelHandle"] = std::to_string(tunnelHandle);
    return static_cast<nabto_status>(std::stoi(returnValues["status"]));
}

nabto_status_t NABTOAPI nabtoTunnelInfo(nabto_tunnel_t tunnel,
                                        nabto_tunnel_info_selector_t index,
                                        size_t size,
                                        void* info)
{
    parameterValues.clear();
    size_t tunnelHandle = reinterpret_cast<size_t>(tunnel);
    parameterValues["tunnelHandle"] = std::to_string(tunnelHandle);

    nabto_status status = static_cast<nabto_status>(std::stoi(returnValues["status"]));

    if(status == NABTO_OK) {
        if(index == NTI_VERSION && size == sizeof(unsigned int)) {
            *((unsigned int*)info) = 123;
        } else if(index == NTI_STATUS && size == sizeof(nabto_tunnel_state_t)) {
            *((nabto_tunnel_state_t*)info) = NTCS_REMOTE_RELAY;
        } else if(index == NTI_LAST_ERROR && size == sizeof(int)) {
            *((int*)info) = -123;
        } else if(index == NTI_PORT && size == sizeof(unsigned short)) {
            *((unsigned short*)info) = 234;
        }
    }

    return status;
}
