#ifndef _NABTO_CLIENT_API_HPP
#define _NABTO_CLIENT_API_HPP
/*
* Copyright (C) 2010-2013 Nabto Aps - All Rights Reserved
*/

/**
 * @file The complete Client API
 *
 * @mainpage Introduction
 *
 *
 * ---------------------------------------------------------------------------
 * The Nabto Client API
 * ---------------------------------------------------------------------------
 *
 * The Nabto client API exposes Nabto functionality that enables you to
 * access any Nabto enabled device on the Internet. You incorporate the
 * Nabto client API in your own Nabto client applications or your own
 * plug-ins for other client software such as web browsers.
 *
 * The Nabto client API is divided into five major parts.
 *
 *  1.   The session API.
 *  2.   The RPC API.
 *  3.   The streaming API.
 *  4.   The tunnel API.
 *  5.   The profile management API.
 *
 *  The Session API is typically the first you will use to prepare a
 *  session context with a user profile for further API invocation.
 *
 *  The RPC API is a set of functions that enables you to invoke
 *  functions on a remote Nabto enabled device.
 *
 *  The streaming API is a set of functions that enables you to make a
 *  permanent connection to a Nabto enabled device (assuming you have the
 *  right credentials). You will be able to read and write data from and to
 *  the device through the connection (Nabto stream).
 *
 *  The tunnel API is a set of functions that enables you to make a TCP
 *  connection to a remote server through a Nabto enabled device. You can
 *  use a standard TCP/IP socket to read and write data from and to the
 *  remote server.
 *
 *  The profile management API can be used to create use profiles for
 *  use in Nabto sessions.
 */

#include <stdint.h>

#ifndef WIN32
#include "sys/types.h"
#endif

#ifndef DOXYGEN_SHOULD_SKIP_THIS

#if defined(_WIN32)
#define NABTOAPI __stdcall
#if defined(NABTO_WIN32_API_STATIC)
#define NABTO_DECL_PREFIX extern
#elif defined(NABTO_CLIENT_API_EXPORTS)
#define NABTO_DECL_PREFIX __declspec(dllexport)
#else
#define NABTO_DECL_PREFIX __declspec(dllimport)
#endif
#else
#define NABTOAPI
#define NABTO_DECL_PREFIX extern
#endif

#endif /* DOXYGEN_SHOULD_SKIP_THIS */



#ifdef __cplusplus
extern "C" {
#endif
    
//-----------------------------------------------------------------------------
// The Nabto client API - enumerations
//-----------------------------------------------------------------------------

/**
 * The nabto_status enumeration is the set of status values that most
 * of the functions in the Client API returns.
 * @since 3.0.2
 */
enum nabto_status {
    /**
     * The operation was successful.
     * @since 3.0.2
     */
    NABTO_OK = 0,

    /**
     * No user profile found in the home directory.
     * @since 3.0.2
     */
    NABTO_NO_PROFILE = 1,

    /**
     * The client could not read the configuration file.
     * @since 3.0.2
     */
    NABTO_ERROR_READING_CONFIG = 2,

    /**
     * The nabtoStartup() function must be the first function called.
     * @since 3.0.2
     */
    NABTO_API_NOT_INITIALIZED = 3,

    /**
     * The operation requires a valid session. Perhaps an invalid session
     * handle was specified.
     * @since 3.0.2
     */
    NABTO_INVALID_SESSION = 4,

    /**
     * A certificate or private key files could not be opened.
     * @since 3.0.2
     */
    NABTO_OPEN_CERT_OR_PK_FAILED = 5,

    /**
     * A private key could not be decrypted with specified password.
     * @since 3.0.2
     */
    NABTO_UNLOCK_PK_FAILED = 6,

    /**
     * Could not login to portal to sign certificate. An invalid
     * email/password was given for active portal. The active portal
     * is given as the urlPortalDomain in the configuration file.
     * @since 3.0.2
     */
    NABTO_PORTAL_LOGIN_FAILURE = 7,

    /**
     * The portal failed when signing certificate.
     * @since 3.0.2
     */
    NABTO_CERT_SIGNING_ERROR = 8,

    /**
     * The portal could not save a signed certificate.
     * @since 3.0.2
     */
    NABTO_CERT_SAVING_FAILURE = 9,

    /**
     * User cannot sign up with specified email address as it is
     * already in use.
     * @since 3.0.2
     */
    NABTO_ADDRESS_IN_USE = 10,

    /**
     * User cannot sign up with specified email address, as it is invalid.
     * @since 3.0.2
     */
    NABTO_INVALID_ADDRESS = 11,

    /**
     * No network available.
     * @since 3.0.2
     */
    NABTO_NO_NETWORK = 12,

    /**
     * Client could not connect to specified host.
     * @since 3.0.2
     */
    NABTO_CONNECT_TO_HOST_FAILED = 13,

    /**
     * Client peer does not support streaming.
     * @since 3.0.2
     */
    NABTO_STREAMING_UNSUPPORTED = 14,

    /**
     * The operation requires a valid stream. Perhaps an invalid stream
     * handle was specified.
     * @since 3.0.2
     */
    NABTO_INVALID_STREAM = 15,

    /**
     * Unacknowledged stream data is pending.
     * @since 3.0.2
     */
    NABTO_DATA_PENDING = 16,

    /**
     * All stream data slots are full.
     * @since 3.0.2
     */
    NABTO_BUFFER_FULL = 17,

    /**
     * An unspecified error occurred. It is necessary to check the
     * log file to find out what actually went wrong.
     * @since 3.0.2
     */
    NABTO_FAILED = 18,

    /**
     * The operation requires a valid tunnel. Perhaps an invalid tunnel
     * handle was specified.
     * @since 3.0.2
     */
    NABTO_INVALID_TUNNEL = 19,

    /**
     * A parameter to a function is not supported.
     * @since 3.0.2
     */
    NABTO_ILLEGAL_PARAMETER = 20,

    /**
     * The operation requires a valid resource. Perhaps an invalid
     * asynchronous resource was specified.
     * @since 3.0.2
     */
    NABTO_INVALID_RESOURCE = 21,

    /**
     * The stream option is invalid.
     * @since 3.0.6
     */
    NABTO_INVALID_STREAM_OPTION = 22,

    /**
     * The stream options argument is invalid, e.g. wrong size etc.
     * @since 3.0.6
     */
    NABTO_INVALID_STREAM_OPTION_ARGUMENT = 23,

    /**
     * The operation has been aborted.
     * @since 3.0.8
     */
    NABTO_ABORTED = 24,

    /**
     * The Stream has been closed gracefully.
     * @since 3.0.8
     */
    NABTO_STREAM_CLOSED = 25,

    /**
     * An error occurred with detailed described in JSON document
     * @since 3.0.15
     */
    NABTO_FAILED_WITH_JSON_MESSAGE = 26,

    /**
     * Number of possible error codes. This must always be last!
     */
    NABTO_ERROR_CODE_COUNT
};

/**
 * The nabto_connection_type enumeration is telling the underlying
 * connection type for a stream.
 * @since 3.0.2
 */
enum nabto_connection_type {
    /**
     * The stream is running on a local connection (no Internet).
     * @since 3.0.2
     */
    NCT_LOCAL = 0,

    /**
     * The stream is running on a direct connection (peer-to-peer).
     * @since 3.0.2
     */
    NCT_P2P = 1,

    /**
     * The stream is running on a fallback connection through the
     * base-station.
     * @since 3.0.2
     */
    NCT_RELAY = 2,

    /**
     * The stream is running on an unrecognized connection type. This
     * value indicates that the connection is lost, since we always
     * know the underlying connection type if it exists.
     * @since 3.0.2
     */
    NCT_UNKNOWN = 3,

    /**
     * The stream is running on a connection that runs through a relay
     * node on the Internet. The device is capable of using TCP/IP and
     * the connection runs directly from the device to the relay node
     * to the client.
     * @since 3.0.2
     */
    NCT_RELAY_MICRO = 4
};

/**
 * The nabto_stream_option enumeration is listing the valid options that
 * can be applied to a nabto stream.
 */
enum nabto_stream_option {
    /**
     * Set timeout for receive operations in ms.
     * The argument is an int. -1 means wait indefinitly, 0 means non-blocking.
     * Default value is -1
     * @since 3.0.6
     */
    NSO_RCVTIMEO = 1,

    /**
     * Set timeout for send operations in ms.
     * The argument is an int. -1 means infinite, 0 means non-blocking.
     * @since 3.0.6
     */
    NSO_SNDTIMEO = 2
};

/**
 * The nabto_tunnel_info_selector enumeration selects the kind of info
 * that the nabtoTunnelInfo function returns.
 * @since 3.0.2
 */
enum nabto_tunnel_info_selector {
    /**
     * Caller is requesting the protocol version of the tunnel (unsigned int).
     * @since 3.0.2
     */
    NTI_VERSION = 0,

    /**
     * Caller is requesting the current tunnel state (nabto_tunnel_state_t).
     * @since 3.0.2
     */
    NTI_STATUS = 1,

    /**
     * Caller is requesting the error code of the last error (int).
     * @since 3.0.2
     */
    NTI_LAST_ERROR = 2,

    /**
     * Caller is requesting the tunnel's local port (unsigned
     * short). Note: Tunnel must have been successfully established
     * (NTI_STATUS in {NTCS_LOCAL, NTCS_REMOTE_*}) prior to invoking,
     * otherwise result is undefined.
     * @since 3.0.12
     */
    NTI_PORT = 3
};

/**
 * The nabto_tunnel_state enumeration is telling the state of the tunnel
 * and the underlying connection type for a stream carrying the tunnel.
 * @since 3.0.2
 */
enum nabto_tunnel_state {
    /**
     * The tunnel is closed.
     * @since 3.0.2
     */
    NTCS_CLOSED = -1,

    /**
     * The tunnel is connecting.
     * @since 3.0.2
     */
    NTCS_CONNECTING = 0,

    /**
     * The other end of the tunnel (the device) has disappeared. The client
     * must connect again.
     * @since 3.0.2
     */
    NTCS_READY_FOR_RECONNECT = 1,

    /**
     * The tunnel is connected and the tunnel is running on an unrecognized
     * connection type. This value indicates an internal error, since we
     * always know the underlying connection type if it exists.
     * @since 3.0.2
     */
    NTCS_UNKNOWN = 2,

    /**
     * The tunnel is connected and the tunnel is running on a local
     * connection (no Internet).
     * @since 3.0.2
     */
    NTCS_LOCAL = 3,

    /**
     * The tunnel is connected and the tunnel is running on a direct
     * connection (peer-to-peer).
     * @since 3.0.2
     */
    NTCS_REMOTE_P2P = 4,

    /**
     * The tunnel is connected and the tunnel is running on a fallback
     * connection through the base-station.
     * @since 3.0.2
     */
    NTCS_REMOTE_RELAY = 5,

    /**
     * The tunnel is connected and the tunnel is running on a connection
     * that runs through a relay node on the Internet. The device is
     * capable of using TCP/IP and the connection runs directly from the
     * device to the relay node to the client.
     * @since 3.0.2
     */
    NTCS_REMOTE_RELAY_MICRO = 6
};

//-----------------------------------------------------------------------------
// The Nabto client API - typedefs
//-----------------------------------------------------------------------------

/**
 * This is a typedef for the nabto_tunnel_state enumeration.
 * @since 3.0.2
 */
typedef enum nabto_tunnel_state nabto_tunnel_state_t;

/**
 * This is a typedef for the nabto_status enumeration.
 * @since 3.0.2
 */
typedef enum nabto_status nabto_status_t;

/**
 * This is a typedef for the nabto_connection_type enumeration.
 * @since 3.0.2
 */
typedef enum nabto_connection_type nabto_connection_type_t;

/**
 * This is a typedef for the nabto_stream_option enumeration.
 * @since 3.0.6
 */
typedef enum nabto_stream_option nabto_stream_option_t;

/**
 * This is a typedef for the nabto_tunnel_info_selector enumeration.
 * @since 3.0.2
 */
typedef enum nabto_tunnel_info_selector nabto_tunnel_info_selector_t;

//-----------------------------------------------------------------------------
// The Nabto client API - handles
//-----------------------------------------------------------------------------

/**
 * This is a struct to an opaque data type - representing a session.
 * @since 3.0.2
 */
struct nabto_opaque_handle;

/**
 * This is a struct to an opaque data type - representing a stream.
 * @since 3.0.2
 */
struct nabto_opaque_stream;

/**
 * This is a struct to an opaque data type - representing a tunnel.
 * @since 3.0.2
 */
struct nabto_opaque_tunnel;

/**
 * This is a struct to an opaque data type - representing an asynchronous
 * request.
 * @since 3.0.2
 */
struct nabto_opaque_async_resource;

/**
 * This is a handle to an open session.
 * @since 3.0.2
 */
typedef struct nabto_opaque_handle * nabto_handle_t;

/**
 * This is a handle to an open stream.
 * @since 3.0.2
 */
typedef struct nabto_opaque_stream * nabto_stream_t;

/**
 * This is a handle to an open tunnel.
 * @since 3.0.2
 */
typedef struct nabto_opaque_tunnel * nabto_tunnel_t;

/**
 * This is a handle to an asynchronous request.
 * @since 3.0.2
 */
typedef struct nabto_opaque_async_resource * nabto_async_resource_t;

//-----------------------------------------------------------------------------
// Session API.
//-----------------------------------------------------------------------------

/**
 * Initializes the Nabto client API.
 *
 * Specify an alternative location of the Nabto home directory (for
 * certificates and log files) in the nabtoHomeDir parameter. The only
 * requirement is that the parent directory must be writable by the
 * user.

 * The nabtoStartup function must be invoked prior to invoking most of the
 * other functions in the Nabto client API.
 *
 * The nabtoSetApplicationName and nabtoSetOption functions
 * must be called before this function if changes to the Nabto client API
 * is needed.
 *
 * @param nabtoHomeDir   New location of the Nabto home directory. Specify
 *                       NULL to use the system default home directory.
 * @return  If the client API has been initialized, the return value
 *          is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_FAILED                 | if path to home directory wasn't found.
 * NABTO_ERROR_READING_CONFIG   | if config file wasn't read.
 *
 *  Remember to call nabtoShutdown after successful nabtoStartup.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStartup(const char* nabtoHomeDir);

/**
 * Terminates the Nabto client API.
 * Releases any resources held by the Nabto client API.
 * After each successful call to nabtoStartup call this function when the
 * Nabto client API is no longer needed. This function can block for a
 * small amount of time until all current sessions has closed properly.
 * @return    NABTO_OK. This is the only value returned.
 *
 * Upon return the Nabto client API is no longer available. The Nabto
 * client API can be re-initialized by calling nabtoStartup again.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoShutdown(void);


/**
 * Starts a new Nabto session as context for RPC, stream or tunnel
 * invocation using the specified profile.
 *
 * @param session    A pointer to a variable that receives the session
 *                   handle upon successful return.
 * @param id         The id of an existing certificate. Either an email address or commonName
 * @param password   Password for encrypted private key file. Specify
 *                   an empty string for an unencrypted key file.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_OPEN_CERT_OR_PK_FAILED | No matching profile found for the given @a email address.
 * NABTO_UNLOCK_PK_FAILED       | A bad password was specified in the @a password argument.
 * NABTO_FAILED                 | The login failed for some unspecified reason.
 *
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoOpenSession(nabto_handle_t* session,
                                                           const char* id,
                                                           const char* password);

/**
 * Closes the specified Nabto session and frees internal ressources.
 * The handle passed to nabtoCloseSession must previously have been opened
 * by a call to nabtoOpenSession.
 * @param session        session handle
 * @return  If the function succeeds, the return value is NABTO_OK.
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 *
 * Upon return the session handle is no longer valid.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoCloseSession(nabto_handle_t session);

/**
 * Set basestation auth information on connect requests
 * 
 * This feature together with a webhook installed on the basestation
 * allows the client to send an access token in the connect
 * request. This way the basestation can contact a third party service
 * and verify that a connect with the given key value pairs is allowed
 * to a specific device.
 * 
 * The key value pairs are copied into an internal structure and can
 * safely be forgotten after the call.
 * 
 * @param session   session handle
 * @param jsonKeyValuePairs  valid json key value pairs like '{"foo": "bar", "baz": "quux" }'
 * @return  If the function succeeds, the return value is NABTO_OK
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSetBasestationAuthJson(nabto_handle_t session,
                                                                      const char* jsonKeyValuePairs);


/**
 * Get the Nabto software version (major.minor.patch[-prerelease tag]+build)
 * 
 * int main() {
 *     char* version;
 *     nabtoVersionString(&version);
 *     printf("%s\n", version);
 *     nabtoFree(version);
 * }
 *
 * @param version  The returned version string, it has to be freed
 * afterwards with nabtoFree()
 * @return NABTO_OK unless error occurs
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoVersionString(char** version);
    
//-----------------------------------------------------------------------------
// The RPC API
//-----------------------------------------------------------------------------

/**
 * Sets the default RPC interface to use when later invoking nabtoRpcInvoke().
 * Can be overriden for specific hosts with nabtoRpcSetInterface.
 * The session handle given must have been obtained by a call to
 * nabtoOpenSession().
 * On successful return, the result buffer contains a JSON response representing
 * the response document on a an error message if a non-API level error
 * occurred. Caller must free result buffer.
 * Note that if this function fails, the output parameters are unchanged.
 * @param session              session handle.
 * @param interfaceDefinition  The interface definition as XML formatted string.
 * @param errorMessage          If set, will be set to null-terminated JSON
 *                             formatted error message from API if return
 *                             indicates so (caller must free)
 * @return  If the request has been answered and the buffers filled with
 *          the data from the answer, the return value is NABTO_OK.
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                    | Meaning
 * ----------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED     | The nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION         | session handle was invalid.
 * NABTO_FAILED_WITH_JSON_MESSAGE| an error occurred with details in the specified error buffer (caller must free)
 * NABTO_FAILED                  | an unspecified error occurred handling the request.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoRpcSetDefaultInterface(nabto_handle_t session,
                                                                      const char* interfaceDefinition,
                                                                      char** errorMessage);

/**
 * Sets the RPC interface to use for a specifc host when later invoking
 * nabtoRpcInvoke().
 * The session handle given must have been obtained by a call to
 * nabtoOpenSession().
 * On successful return, the result buffer contains a JSON response representing
 * the response document on a an error message if a non-API level error
 * occurred. Caller must free result buffer.
 * Note that if this function fails, the output parameters are unchanged.
 * @param session              session handle.
 * @param host                 The host for which the interface is to be used
 *                             later RPC invocations.
 * @param interfaceDefinition  The interface definition as XML formatted string.
 * @param errorMessage          If set, will be set to null-terminated JSON
 *                             formatted error message from API if return
 *                             indicates so (caller must free)
 * @return  If the request has been answered and the buffers filled with
 *          the data from the answer, the return value is NABTO_OK.
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                    | Meaning
 * ----------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED     | The nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION         | session handle was invalid.
 * NABTO_FAILED_WITH_JSON_MESSAGE| an error occurred with details in the specified error buffer (caller must free)
 * NABTO_FAILED                  | an unspecified error occurred handling the request.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoRpcSetInterface(nabto_handle_t session,
                                                               const char* host,
                                                               const char* interfaceDefinition,
                                                               char** errorMessage);

/**
 * Retrieves data synchronously from specified nabto:// URL on
 * specified session. As compared to nabtoFethUrl, this function only
 * supports function invocation and not displaying contents of an HTML
 * DD bundle. This also means the interface file is to be specified prior
 * to invocation using either nabtoRpcSetInterface or
 * nabtoRpcSetDefaultInterface.
 * The session handle given must have been obtained by a call to
 * nabtoOpenSession.
 * On successful return, the result buffer contains a JSON response representing
 * the response document on a an error message if a non-API level error
 * occurred. Caller must free result buffer.
 * Note that if this function fails, the output parameters are unchanged.
 * @param session        session handle.
 * @param nabtoUrl       The URL to retrieve.
 * @param jsonResponse   Location to place the pointer to the newly allocated
 *                       null-terminated JSON string (must be freed by caller
 *                       when status is NABTO_OK)
 * @return  If the request has been answered and the buffers filled with
 *          the data from the answer, the return value is NABTO_OK.
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                     | Meaning
 * ------------------------------ | ---------------------
 * NABTO_API_NOT_INITIALIZED      | The nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION          | session handle was invalid.
 * NABTO_FAILED_WITH_JSON_MESSAGE | an error occurred, details can be found in JSON format in the response.
 * NABTO_FAILED                   | an unspecified error occurred handling the request.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoRpcInvoke(nabto_handle_t session,
                                                         const char* nabtoUrl,
                                                         char** jsonResponse);

//-----------------------------------------------------------------------------
// The stream API
//-----------------------------------------------------------------------------

/**
 * Opens a stream on an existing session to a Nabto enabled device.
 * The server ID given must be an id of a "host" on the device.
 * This function returns a stream handle that must be used in subsequent
 * client API invocations.
 * The caller must call nabtoStreamClose when the stream (handle) is no
 * longer needed.
 * An open session handle must have been created prior to calling this
 * function.
 * @param stream         Location where the stream handle will be copied upon
 *                       return.
 * @param session        session handle
 * @param serverId       The host to open a stream to.
 * @return  If a stream has been created, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 * NABTO_STREAMING_UNSUPPORTED  | peer does not support streaming.
 * NABTO_CONNECT_TO_HOST_FAILED | could not connect to specified host.
 * NABTO_FAILED                 | an unspecified error occurred creating the stream.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamOpen(nabto_stream_t* stream,
                                                          nabto_handle_t session,
                                                          const char* serverId);

/**
 * Closes an open stream.
 * The return value is NABTO_DATA_PENDING if unacknowledged data is still
 * pending on the stream. Keep invoking nabtoStreamClose until the return
 * value is NABTO_OK. Alternatively close the owing session to force
 * closing all the open streams with pending data.
 * The handle passed to nabtoStreamClose must previously have been opened
 * by a call to nabtoStreamOpen.
 * @param stream         stream handle
 * @return  If the stream has been closed, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_DATA_PENDING           | unacknowledged data is pending.
 * NABTO_FAILED                 | an unspecified error occurred closing the stream.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamClose(nabto_stream_t stream);

/**
 * Reads some data from an open stream.
 * This call blocks based on the stream option NSO_RCVTIMEO. -1: blocking.
 * 0: non-blocking. <n>: blocks for up to <n> milliseconds.
 * The stream handle given must have been obtained by a call to
 * nabtoStreamOpen.
 * On successful return the given result buffer is filled with newly
 * allocated memory containing the data received from peer.
 * It is the responsibility of the caller to release the buffer again using
 * the nabtoFree function.
 * Note that if this function fails, the output parameters are unchanged.
 * @param stream         stream handle
 * @param resultBuffer   Location to place the pointer to the newly allocated
 *                       buffer.
 * @param resultLen      Location to place the number of bytes in the buffer.
 * @return  If data has been read from the stream and the buffers has been
 *          filled with the data, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_STREAM_CLOSED          | the stream was closed gracefully.
 * NABTO_ABORTED                | the operation was aborted.
 * NABTO_FAILED                 | an unspecified error occurred reading the stream.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamRead(nabto_stream_t stream,
                                                          char** resultBuffer,
                                                          size_t* resultLen);

/**
 * Reads any available data into specified buffer from a stream.
 *
 * The stream handle given must have been obtained by a call to
 * nabtoStreamOpen.
 * On successful return the given result buffer is filled with data
 * received from peer.
 * Note that if this function fails, the output parameters are unchanged.
 * @param stream         stream handle
 * @param buffer         Pointer to buffer that will receive the data.
 * @param bufLen         Size in bytes of the given buffer.
 * @param resultLen      Location where the number of bytes written to buffer
 *                       is saved.
 * @return  If data has been read from the stream and the buffers has been
 *          filled with the data, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_STREAM_CLOSED          | the stream was closed gracefully.
 * NABTO_ABORTED                | the operation was aborted.
 * NABTO_FAILED                 | an unspecified error occurred reading the stream.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamReadIntoBuf(nabto_stream_t stream,
                                                                 char* buffer,
                                                                 size_t bufLen,
                                                                 size_t* resultLen);

/**
 * Writes given data to a stream.
 * The semantics of whether it blocks or not is controlled by the
 * NSO_SNDTIMEO stream option. -1: blocking until all data is sent
 * or queued. 0: non blocking. <n>: block for up to <n> milliseconds.
 * The stream handle given must have been obtained by a call to
 * nabtoStreamOpen.
 * If NABTO_BUFFER_FULL is returned you must retransmit the data, no data
 * has been queued. If NABTO_OK is returned all the data is queued in
 * the transmission queue.
 * @param stream         stream handle
 * @param buf            The data to send.
 * @param len            The size of the buffer to send
 * @return  If data has been written to the stream, the return value
 *          is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_BUFFER_FULL            | if there's not room for the bytes in the send queue.
 * NABTO_STREAM_CLOSED          | the stream was closed gracefully.
 * NABTO_ABORTED                | the operation was aborted.
 * NABTO_FAILED                 | an unspecified error occurred writing the stream.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamWrite(nabto_stream_t stream,
                                                           const char* buf,
                                                           size_t len);

/**
 * Retrieve the Nabto connection type of the underlying connection
 * for the given stream. The connection can be a local connection
 * on the LAN. It can be a peer-to-peer connection on the Internet
 * or a connection relayed through the base station.
 * @param stream         stream handle
 * @param type           Location where the Nabto connection type will
 *                       be returned.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred retrieving the type.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamConnectionType(nabto_stream_t stream,
                                                                    nabto_connection_type_t* type);

/**
 * Set stream options, in a setsockopt style.
 * @param stream         Stream handle.
 * @param optionName     Option name to set.
 * @param optionValue    Option value.
 * @param optionLength   Option Length.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_STREAM         | stream handle was invalid.
 * NABTO_INVALID_STREAM_OPTION  | if option is invalid.
 * NABTO_ERROR                  | an unspecified error occurred.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoStreamSetOption(nabto_stream_t stream,
                                                               nabto_stream_option_t optionName,
                                                               const void *optionValue,
                                                               size_t optionLength);


//-----------------------------------------------------------------------------
// The tunnel API
//-----------------------------------------------------------------------------

/**
 * Opens a TCP tunnel to a remote server through a Nabto enabled device.
 *
 * @param tunnel      A pointer to a variable that receives the tunnel
 *                    handle upon successful return.
 * @param session     The session handle returned by a previous call to
 *                    the @b nabtoOpenSession or @b nabtoOpenSessionBare
 *                    function.
 * @param localPort   The local TCP port to listen on (0 = PORT_ANY)
 * @param nabtoHost   The remote Nabto host to connect to.
 * @param remoteHost  The host the remote endpoint establishes a TCP
 *                    connection to.
 * @param remotePort  The TCP port to connect to on remoteHost.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred creating the tunnel.
 *
 * @code
 *      +--------+           +--------+               +--------+
 *      | nabto  |   nabto   | nabto  |   tcp/ip      | remote |
 *   |--+ client +----~~~----+ device +----~~~-----|--+ server |
 * port | API    |           | "host" |          port |        |
 *      +--------+           +--------+               +--------+
 * @endcode
 *
 * @remark In case the TCP tunnel should connect to the Nabto enabled
 * device itself - that is the device is the server - specify "localhost"
 * in the remoteHost parameter.
 *
 * @remark This function returns a tunnel handle that must be used in
 * subsequent client API invocations.
 *
 * @remark The caller must call @b nabtoTunnelClose when the tunnel
 * (handle) is no longer needed.
 *
 * @remark If zero is chosen for localPort, the client chooses a suitable port.
 * After successful connection use nabtoTunnelInfo to get the listening port.
 *
 * @remark An open session handle must have been created prior to
 * calling this function.
 *
 * @remark After successful return one is now able to connect to the
 * local TCP port and communicate with the remote server.
 *
 * @since 3.0.2
 *
 * @sa
 * @b nabtoStreamOpen
 * @b nabtoTunnelClose
 * @b nabtoTunnelInfo
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoTunnelOpenTcp(nabto_tunnel_t* tunnel,
                                                             nabto_handle_t session,
                                                             int localPort,
                                                             const char* nabtoHost,
                                                             const char* remoteHost,
                                                             int remotePort);

/**
 * Closes an open tunnel.
 * The handle passed to nabtoTunnelClose must previously have been opened
 * by a call to nabtoTunnelOpenTcp.
 * @param tunnel         tunnel handle
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_TUNNEL         | tunnel handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred closing the tunnel.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoTunnelClose(nabto_tunnel_t tunnel);

/**
 * Retrieves information on an (open) tunnel.
 * The tunnel handle given must have been obtained by a call to
 * nabtoTunnelOpenTcp.
 * On successful return the given buffer is filled with information about
 * the tunnel.
 * Note that if this function fails, the output parameters are unchanged.
 * It is legal to call with a size of zero and info set to NULL. In that
 * case no data are returned, but the return value is still valid.
 * @param tunnel         tunnel handle
 * @param index          Number specifying what kind of info to retrieve.
 * @param size           Size in bytes of the buffer supplied.
 * @param info           Location of a buffer of size bytes to be filled.
 * @return  If the information has been written to the buffer, the return value
 *          is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_TUNNEL         | tunnel handle was invalid.
 * NABTO_ILLEGAL_PARAMETER      | if index is illegal or size too small.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoTunnelInfo(nabto_tunnel_t tunnel,
                                                          nabto_tunnel_info_selector_t index,
                                                          size_t size,
                                                          void* info);


//-----------------------------------------------------------------------------
// Profile management API
//-----------------------------------------------------------------------------

/**
 * Important: Only use this function if you use your own dedicated
 * user management services as per agreement with Nabto - and hence do
 * NOT use if developing a standard Nabto Cloud / AppMyProduct based
 * app. In those cases, please use only self-signed cert
 * authentication + fingerprint based ACL security - see
 * nabtoCreateSelfSignedProfile and TEN036 "Security in Nabto
 * Solutions"
 *
 * This function creates a private key and a signed certificate on
 * this computer for a specified user that already must exist in a
 * central user management service.
 *
 * This function will fail with NABTO_PORTAL_LOGIN_FAILURE if the specified
 * user does not exist.
 *
 * The given password is used to access the portal for specified user. The
 * password will also be used for encrypting the private key.
 *
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param email     Email address of user, as registered on portal.
 * @param password  Password for accessing portal for specified user.
 * @return  If a profile has been created, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_UNLOCK_PK_FAILED       | if bad password was specified.
 * NABTO_CERT_SAVING_FAILURE    | could not save signed certificate.
 * NABTO_PORTAL_LOGIN_FAILURE   | invalid email and/or password.
 * NABTO_CERT_SIGNING_ERROR     | failed to sign certificate request.
 * NABTO_FAILED                 | lookup failed for some unspecified reason.
*/
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoCreateProfile(const char* email,
                                                             const char* password);

/**
 * Creates a Nabto self signed profile. The identity of such
 * certificate cannot be trusted but the fingerprint of the
 * certificate can be trusted in the device. After the profile has
 * been created it can be used in the open session function.
 * 
 * @param id          The id used to create the selfsigned certificate, typically an email or user name.
 * @param password    The password which protects the private key.
 * @return If the self signed certificate has been created the function returns NABTO_OK
 *         If it fails one of the following error codes will be returned.
 * 
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_CERT_SAVING_FAILURE    | could not save signed certificate.
 * NABTO_CERT_SIGNING_ERROR     | failed to sign certificate request.
 * NABTO_FAILED                 | lookup failed for some unspecified reason.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoCreateSelfSignedProfile(const char* id,
                                                                       const char* password);
/**
 * Remove profile certificate for given id.
 * @param id   The ID of the profile to be removed
 * @return If the profile certificate was removed the function returns NABTO_OK
 *         If it fails one of the following error codes will be returned.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_OPEN_CERT_OR_PK_FAILED | The provided ID could not be found.
 * NABTO_FAILED                 | Failed to remove the certificate file from the filesystem
 * NABTO_CERT_SAVING_FAILURE    | Failed to remove the key file
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoRemoveProfile(const char* id);

/**
 * Retrieve public key fingerprint for profile with specified id.
 * @param id           The id of the profile for which to get the fingerprint.
 * @param fingerprint  The RSA public key fingerprint (buffer of 16 bytes owned by caller)
 * @return If the fingerprint was calculated the function returns NABTO_OK and writes the fingerprint.
 *         If it fails one of the following error codes will be returned.
 * 
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_OPEN_CERT_OR_PK_FAILED | No matching certificate found for the given id
 * NABTO_FAILED                 | lookup failed for some unspecified reason.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetFingerprint(const char* id,
                                                              char fingerprint[16]);

//-----------------------------------------------------------------------------
// Logging
//-----------------------------------------------------------------------------

/**
 * Definition of the callback function for the nabtoRegisterLogCallback
 * function. This should use the NABTOAPI calling convention in the future.
 * @param line     The line to log.
 * @param size     The length of the line.
 */
typedef void (*NabtoLogCallbackFunc)(const char* line , size_t size);

/**
 * Register a log callback function.
 * @param callback     The callback which is called for every log line.
 * @return    NABTO_OK. This is the only value returned.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoRegisterLogCallback(NabtoLogCallbackFunc callback);


//-----------------------------------------------------------------------------
// Query
//-----------------------------------------------------------------------------

/**
 * Return a english string representation of a given nabto_status_t status code
 * @param status  the status code to be translated to text.
 * @return  the text.
 */
NABTO_DECL_PREFIX const char* NABTOAPI nabtoStatusStr(nabto_status_t status);


/**
 * Returns an array of known prefixes in the location specified by prefixes.
 * The array is NULL terminated. Both the array and the prefix strings has
 * been allocated in memory.
 * It is the callers responsibility to free both the array *and* the prefixes
 * using the nabtoFree function.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param prefixes        points to a location that will receive the null
 *                        terminated array of allocated c strings.
 * @param prefixesLength  points to a location that will receive the number of
 *                        entries in the array.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_FAILED                 | if protocol prefixes could not be retrieved.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetProtocolPrefixes(char*** prefixes,
                                                                   int* prefixesLength);


/**
 * Returns the currently known certificates on the system.
 * The array is NULL terminated. Both the array and the prefix strings has
 * been allocated in memory.
 * It is the callers responsibility to free both the array *and* the prefixes
 * using the nabtoFree function.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param certificates        points to a location that will receive the null
 *                            terminated array of allocated c strings containing the profile IDs.
 * @param certificatesLength  points to a location that will receive the number of
 *                            entries in the array.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_FAILED                 | if certificates could not be retrieved.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetCertificates(char*** certificates,
                                                               int* certificatesLength);

/**
 * Return a list of local discoverable devices.
 * @param devices          a pointer to an array of char *
 * @param numberOfDevices  the length of returned devices
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_FAILED                 | if local devices could not be retrieved.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetLocalDevices(char*** devices,
                                                               int* numberOfDevices);


//-----------------------------------------------------------------------------
// Miscellaneous
//-----------------------------------------------------------------------------

/**
 * Frees allocated memory made by functions in the Nabto client API.
 * @param p   Pointer to the memory to free.
 * @return    NABTO_OK. This is the only value returned.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoFree(void* p);
    
/**
 * Sets the name of your client application. The Nabto client API will
 * use this name to identify data from your application wherever
 * possible. Call this function before nabtoStartup if it should have
 * any effect.
 * @param applicationName    The name of your application name
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_ILLEGAL_PARAMETER      | if name is NULL or empty.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSetApplicationName(const char* applicationName);

/**
 * Tells the Nabto client API the location of the static resource
 * directory.  This function will override the default behaviour of
 * searching several directories relative to the install location of
 * the Nabto client API.
 *
 * You may call this function at anytime to change the location of the
 * static resource directory. Calling this function with an empty (or
 * NULL) resourceDir will revert to default search behaviour.
 *
 * @param resourceDir        The location of the static resource directory.
 * @return    NABTO_OK. This is the only value returned.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSetStaticResourceDir(const char* resourceDir);

/**
 * Install default static resources.
 *
 * The default static resources includes the following resources:
 *
 * roots/ca.crt
 * roots/ca-2016.crt
 * roots/cacert.pem
 * users/guest.crt
 * users/guest.key
 * 
 * If resourceDir is null then the resources is installed into the user's homedir.
 * 
 * If resourceDir is not null and files are installed into a non-default location,
 * nabtoSetStaticResourceDir must be called seperately with resourceDir for the SDK to be able to
 * locate the files subsequently.
 *
 * Small code example for static resource installation:
 * 
 * nabto_handle_t session;
 * nabtoStartup(NULL);
 * nabtoInstallDefaultStaticResources(NULL);
 * // Resources can now be used for communication!
 * nabtoOpenSession(&session, "user", "pass");
 * nabtoShutdown();
 *
 * @param  resourceDir The location to install the resources into.
 * @return NABTO_OK if resources are installed.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoInstallDefaultStaticResources(const char* resourceDir);


/**
 * Invoke before nabtoStartup to change a few options. The following
 * public options are available:
 *
 *    Option                 Values
 * +----------------------+-----------------------------------------------+
 * | configFileName       | any valid file name                           |
 * +----------------------+-----------------------------------------------+
 *
 * The configFileName option changes the file name of the Nabto
 * configuration file (which is located in the Nabto home directory). The
 * default file name is nabto_config.ini. 
 * @param name               The name of the option to change.
 * @param value              The new value of the option.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_ILLEGAL_PARAMETER      | if name is NULL or empty.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSetOption(const char* name,
                                                         const char* value);
//-----------------------------------------------------------------------------
// Deprecated functions - most due to HTML DD (and even older app types)
// being deprecated
//-----------------------------------------------------------------------------

/**
 * DEPRECATED: as it does not contain PATCH, prerelease and build information
 * Get the Nabto software version
 * @param major  The returned major version.
 * @param minor  The returned minor version.
 * @return NABTO_OK
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoVersion(int* major, int* minor);

/**
 * DEPRECATED: public platform has not supported this since pre
 * 3.0.0 (2010), only used internally in (also deprecated) browser plugins
 */
enum nabto_async_post_data_status {
    NAPDS_OK = 0,
    NAPDS_CLOSED = 1
};

/**
 * Not truly deprecated as it might be introduced for async functions
 * in the future but currently only usable with deprecated functions.
 */
enum nabto_async_status {
    /**
     * The Nabto Client has received the mime type of the requested data.
     * @since 3.0.2
     */
    NAS_MIMETYPE_AVAILABLE = 0,

    /**
     * The Nabto Client has received a part of the requested data.
     * @since 3.0.2
     */
    NAS_CHUNK_READY = 1,
    
    /**
     * The Nabto Client ends the request.
     * @since 3.0.2
     */
    NAS_CLOSED = 2
};


/**
 * Not truly deprecated as it might be introduced for async functions
 * in the future but currently only usable with deprecated functions.
 */
typedef enum nabto_async_status nabto_async_status_t;

/**
 * DEPRECATED: HTML DD based application are deprecated, use
 * rpcInvoke instead
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoFetchUrl(nabto_handle_t session,
                                                        const char* nabtoUrl,
                                                        char** resultBuffer,
                                                        size_t* resultLen,
                                                        char** mimeTypeBuffer);

/**
 * DEPRECATED: public platform has not supported this since pre
 * 3.0.0 (2010), only used internally in (also deprecated) browser plugins
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSubmitPostData(nabto_handle_t session,
                                                              const char* nabtoUrl,
                                                              const char* postBuffer,
                                                              size_t postLen,
                                                              const char* postMimeType,
                                                              char** resultBuffer,
                                                              size_t* resultLen,
                                                              char** resultMimeTypeBuffer);

/**
 * DEPRECATED: HTML DD based application are deprecated, use
 * rpcInvoke instead
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetSessionToken(nabto_handle_t session,
                                                               char* buffer,
                                                               size_t bufLen,
                                                               size_t* resultLen);

/**
 * DEPRECATED: HTML DD based application are deprecated, use
 * rpcInvoke instead (but no async version exists yet)
 */
typedef void (NABTOAPI *NabtoAsyncStatusCallbackFunc)(nabto_async_status_t status,
                                                      void* arg,
                                                      void* userData);
/**
 * DEPRECATED: public platform has not supported this since pre 3.0.0
 * (2010), only used internally in (also deprecated) browser plugins
 */
typedef enum nabto_async_post_data_status nabto_async_post_data_status_t;

/**
 * DEPRECATED: public platform has not supported this since pre 3.0.0
 * (2010), only used internally in (also deprecated) browser plugins
 */
typedef nabto_async_post_data_status_t (NABTOAPI *NabtoAsyncPostDataCallbackFunc)(char* buf,
                                                                                  size_t bufSize,
                                                                                  size_t* actualSize,
                                                                                  void* userData);
    
/**
 * DEPRECATED: public SDK has not supported this since pre 3.0.0
 * (2010), only used internally in (also deprecated) browser plugins
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncSetPostData(nabto_async_resource_t resource,
                                                                const char* mimeType,
                                                                NabtoAsyncPostDataCallbackFunc cb,
                                                                void* userData);

/**
 * DEPRECATED: public SDK has not supported this since pre 3.0.0
 * (2010), only used internally in (also deprecated) browser plugins
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncPostData(nabto_async_resource_t resource,
                                                             const char* data,
                                                             size_t dataLength);

/**
 * DEPRECATED: public SDK has not supported this since pre 3.0.0
 * (2010), only used internally in (also deprecated) browser plugins
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncPostClose(nabto_async_resource_t resource);

/**
 * DEPRECATED: HTML DD based application are deprecated, use
 * rpcInvoke instead (but no async version exists yet)
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncFetch(nabto_async_resource_t resource,
                                                          NabtoAsyncStatusCallbackFunc cb,
                                                          void* userData);

/**
 * DEPRECATED: HTML DD based application are deprecated, use
 * rpcInvoke instead (but no async version exists yet)
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetAsyncData(nabto_async_resource_t resource,
                                                            char* buf,
                                                            size_t bufSize,
                                                            size_t* actualSize);
/**
 * DEPRECATED: only used in now deprecated browser plugins
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoOpenSessionBare(nabto_handle_t* session);

/**
 * Not truly deprecated as it might be introduced for initializing
 * other async handles in the future. Currently only prepares use of
 * deprecated functions.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncInit(nabto_handle_t session,
                                                         nabto_async_resource_t* resource,
                                                         const char* url);
/**
 * Not truly deprecated as it might be introduced for closing other
 * async handles in the future. Currently only closes handles created
 * with deprecated methods.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncClose(nabto_async_resource_t resource);

/**
 * Not truly deprecated as it might be introduced for aborting other
 * async handles in the future. Currently only aborts deprecated async
 * function calls.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAbortAsync(nabto_async_resource_t resource);

/**
 * DEPRECATED: Central Nabto based user management is deprecated in
 * favor of self-signed certificates and RSA fingerprint based access
 * control. See TEN036.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSignup(const char* email,
                                                      const char* password);

/**
 * DEPRECATED: Central Nabto based user management is deprecated in
 * favor of self-signed certificates and RSA fingerprint based access
 * control. See TEN036.
*/
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoResetAccountPassword(const char* email);

/**
 * DEPRECATED: use platform specific mechanisms instead
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoProbeNetwork(size_t timeoutMillis,
                                                            const char* host);

    
#ifdef __cplusplus
} // extern c
#endif

#endif // _NABTO_CLIENT_API_HPP
