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
 *  1.   Configuration and initialization API.
 *  2.   The session API.
 *  3.   The streaming API.
 *  4.   The tunnel API.
 *  5.   The portal API.
 *
 *  The configuration and initialization API is typically the first you
 *  will use. Here you can modify the behavior of the client API. For
 *  instance, change the location of the log file, change language of the
 *  html code returned by the session API, etc. You must also call
 *  nabtoStartup first to enable most of the functions in the other APIs to
 *  work properly.
 *
 *  The session API is a set of functions that enables you to make
 *  synchronous and asynchronous request to any Nabto enabled device
 *  (assuming you have the right credentials).
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
 *  The portal API is a set of functions that enables you to manage
 *  accounts and passwords on the Nabto portal.
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
     * Timeout when connecting to remote device.
     * @since 3.0.15
     */
    NABTO_CONNECT_TIMEOUT = 27,

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

/**
 * The nabto_async_status enumeration is telling the reason why the
 * Nabto Client calls the NabtoAsyncStatusCallbackFunc callback function.
 * @since 3.0.2
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
 * The nabto_async_post_data_status enumeration is the set of values
 * the NabtoAsyncPostDataCallbackFunc callback function returns.
 * @since 3.0.2
 */
enum nabto_async_post_data_status {
    /**
     * The callback function has filled the buffer with the requested data.
     * @since 3.0.2
     */
    NAPDS_OK = 0,

    /**
     * The post data stream has reached the end (no more data).
     * @since 3.0.2
     */
    NAPDS_CLOSED = 1
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

/**
 * This is a typedef for the nabto_async_status enumeration.
 * @since 3.0.2
 */
typedef enum nabto_async_status nabto_async_status_t;

/**
 * This is a typedef for the nabto_async_post_data_status enumeration.
 * @since 3.0.2
 */
typedef enum nabto_async_post_data_status nabto_async_post_data_status_t;


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
// The session API - synchronous
//-----------------------------------------------------------------------------

/**
 * Starts a new Nabto data retrieval session using the specified profile.
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
 * @remark The @a email address given must be an id of an existing certificate
 * for a certain Nabto enabled device. If the device is unencrypted, specify
 * the empty string for the @a password argument.
 * This function returns a session handle that must be used in subsequent
 * client API invocations.
 *
 * @remark When the session handle is no longer needed, close it by using
 * the @b nabtoCloseSession function.
 *
 * @remark The @b nabtoStartup function must have been called prior to calling
 * this function.
 *
 * @remark The following example shows you how to open a single Nabto session.
 * @code{.c}
#include <nabto_client_api.h>

nabto_status_t st;
nabto_handle_t session;

st = nabtoStartup(NULL);
if (st != NABTO_OK) exit(1);

st = nabtoOpenSession(&session, "me@domain.com", "secret");
if (st != NABTO_OK) {
   nabtoShutdown();
   exit(1);
}

...

nabtoCloseSession(session);
nabtoShutdown();
 * @endcode
 *
 * @since 3.0.2
 *
 * @sa
 * @b nabtoStartup
 * @b nabtoCloseSession
 * @b nabtoOpenSessionBare
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoOpenSession(nabto_handle_t* session,
                                                           const char* id,
                                                           const char* password);


/**
 * Starts a new Nabto data retrieval session without using a certificate.
 * This function returns a session handle that must be used in subsequent
 * client API invocations.
 * The caller must call nabtoCloseSession when the session (handle) is no
 * longer needed.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param session        Location where the session handle will be copied upon
 *                       return.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoOpenSessionBare(nabto_handle_t* session);

/**
 * Closes the specified Nabto session, preventing further data retrieval.
 * The handle passed to nabtoCloseSession must previously have been opened
 * by a call to either nabtoOpenSession or nabtoOpenSessionBare.
 * @param session        session handle
 * @return  If the function succeeds, the return value is NABTO_OK.@n
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
 * @param errorBuffer          If set, will be set to null-terminated JSON
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
 * @param errorBuffer          If set, will be set to null-terminated JSON
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
 * DD bundle. This also means the interface file is to specified prior
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

/**
 * Retrieves data synchronously from specified nabto:// URL on specified
 * session. The session handle given must have been obtained by a call to either
 * nabtoOpenSession or nabtoOpenSessionBare.
 * On successful return the given result buffer is filled with newly
 * allocated HTML code in the language specified in nabtoSetOption.
 * The MIME type of the data is returned in the mimeType output parameter
 * as a zero-terminated string.
 * It is the responsibility of the caller to release the buffer and mime
 * type string again using the nabtoFree function.
 * Note that if this function fails, the output parameters are unchanged.
 * @param session         session handle.
 * @param nabtoUrl        The URL to retrieve.
 * @param resultBuffer    Location to place the pointer to the newly allocated
 *                        buffer.
 * @param resultLen       Location to place the number of bytes in the buffer.
 * @param mimeTypeBuffer  Location to place the pointer to the newly allocated
 *                        mime type.
 * @return  If the request has been answered and the buffers filled with
 *          the data from the answer, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred handling the request.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoFetchUrl(nabto_handle_t session,
                                                        const char* nabtoUrl,
                                                        char** resultBuffer,
                                                        size_t* resultLen,
                                                        char** mimeTypeBuffer);

/**
 * Submits specified data synchronously to specified URL through specified
 * session. If destination host is HTTP enabled, use HTTP POST semantics
 * for the submission. Note: Only "application/x-www-form-urlencoded" type
 * data is currently supported as data for submission (any type of data may
 * still be retrieved, though).
 * The session handle given must have been obtained by a call to either
 * nabtoOpenSession or nabtoOpenSessionBare.
 * On successful return the given result buffer is filled with newly
 * allocated HTML code in the language specified in nabtoSetOption.
 * The MIME type of the data is returned in the mimeType output parameter
 * as a zero-terminated string.
 * It is the responsibility of the caller to release the buffer and mime
 * type string again using the nabtoFree function.
 * Note that if this function fails, the output parameters are unchanged.
 * @param session               session handle.
 * @param nabtoUrl              The URL to submit data to.
 * @param postBuffer            The data to submit.
 * @param postLen               The length in bytes of data to submit.
 * @param postMimeType          MIME type of data to submit.
 * @param resultBuffer          Location to place the pointer to the newly
 *                              allocated buffer.
 * @param resultLen             Location to place the number of bytes in the
 *                              buffer.
 * @param resultMimeTypeBuffer  Location to place the pointer to the newly
 *                              allocated mime type.
 * @return  If the request has been answered and the buffers filled with
 * the data from the answer, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred handling the request.
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
 * Reads the session token of the specified session handle. This token is
 * typically supplied in html requests.
 * @param session        Session handle.
 * @param buffer         Pointer to buffer that will receive the token.
 * @param bufLen         Size in bytes of the given buffer.
 * @param resultLen      Number of bytes copied to the buffer upon successful
 *                       return.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_FAILED                 | an unspecified error occurred.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetSessionToken(nabto_handle_t session,
                                                               char* buffer,
                                                               size_t bufLen,
                                                               size_t* resultLen);


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
 * @remark
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
// The session API - asynchronous
//-----------------------------------------------------------------------------

/**
 * Definition of the asynchronous callback function.
 * This callback function is called every time something happens in
 * an asynchronous request.
 * Start the asynchronous request in the following way:
 *    nabtoAsyncInit(..., &resource, ...);
 *    nabtoAsyncSetPostData(resource,....);
 *    nabtoAsyncFetch(resource, ...);
 * You must implement this callback function and pass it to nabtoAsyncFetch
 * to retrieve the result of your request.
 *
 * @param status    The reason why this function is called.
 * @param arg       Additional data - see table below.
 * @param userData  The user data passed in nabtoAsyncFetch.
 *
 * The arg has the following content depending on "status":
 *
 *     Status/reason      |     Type     |    Content
 * ---------------------- | ------------ | -----------------------------
 * NAS_MIMETYPE_AVAILABLE | const char * | The MIME type of the data
 * NAS_CHUNK_READY        | size_t *     | Number of bytes available. Call the @b nabtoGetAsyncData function to retrieve the data.
 * NAS_CLOSED             | void         | The request has ended
 * ---------------------- | ------------ | -----------------------------
 */
typedef void (NABTOAPI *NabtoAsyncStatusCallbackFunc)(nabto_async_status_t status,
                                                      void* arg,
                                                      void* userData);

/**
 * Definition of the asynchronous data retriever callback function.
 * This callback function is called every time the Nabto client API needs
 * more data for a POST request.
 * You must implement this callback function and pass it to
 * nabtoAsyncSetPostData. When the callback function is called you need
 * to fill in a chunk of post data in a non-blocking fashion.
 *
 * @param buf         Pointer to a buffer to fill with the data to send.
 * @param bufSize     The size in bytes of the buffer.
 * @param actualSize  Return the number of bytes actually written to the buffer.
 * @param userData    The user data passed in nabtoAsyncSetPostData.
 * @return    NAPDS_OK      the buffer has been filled with *actualSize bytes.
 *            NAPDS_CLOSED  if the post data stream has reached eof.
 */
typedef nabto_async_post_data_status_t (NABTOAPI *NabtoAsyncPostDataCallbackFunc)(char* buf,
                                                                                  size_t bufSize,
                                                                                  size_t* actualSize,
                                                                                  void* userData);


/**
 * Initialize an asynchronous resource with an request URL.
 * To get the result from the resource call the nabtoAsyncFetch function.
 * This function returns a asynchronous resource that must be used in
 * subsequent client API invocations.
 * An open session handle must have been created prior to calling this
 * function.
 * @param session   session handle
 * @param resource  Location where the asynchronous resource will be copied
 *                  upon return.
 * @param url       The URL to retrieve.
 * @return  If an asynchronous resource has been created, the return value
 *          is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_SESSION        | session handle was invalid.
 * NABTO_FAILED                 | an unspecified error occurred creating the resource.
 *
 * @sa
 * @b nabtoAsyncClose
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncInit(nabto_handle_t session,
                                                         nabto_async_resource_t* resource,
                                                         const char* url);

/**
 * Close an asynchronous resource. After you are finished using a resource a
 * call to this function releases the resource and all it's resources.
 * @param resource  The asynchronous resource.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred.
 *
 * @sa
 * @b nabtoAsyncInit
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncClose(nabto_async_resource_t resource);

/**
 * Sets up a callback function that will be called whenever post data is
 * needed.
 * The callback function must deliver post data in a non-blocking
 * fashion.
 * @param resource  asynchronous resource
 * @param mimeType  The MIME type of the post data.
 * @param cb        Application specific callback function.
 * @param userData  Application data passed to the callback "as-is".
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncSetPostData(nabto_async_resource_t resource,
                                                                const char* mimeType,
                                                                NabtoAsyncPostDataCallbackFunc cb,
                                                                void* userData);

/**
 * MISSING DOCUMENTATION
 * @param resource    asynchronous resource
 * @param data        pointer to ??
 * @param dataLength  size of buffer??
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred starting the fetch.
 *
 * @sa
 * @b nabtoAsyncInit
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncPostData(nabto_async_resource_t resource,
                                                             const char* data,
                                                             size_t dataLength);

/**
 * MISSING DOCUMENTATION
 * @param resource  asynchronous resource
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred starting the fetch.
 *
 * @sa
 * @b nabtoAsyncInit
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncPostClose(nabto_async_resource_t resource);

/**
 * Sets up a callback function that will be called whenever response data
 * is ready and starts the fetch of the data.
 * The nabtoAsyncInit function must have been called prior to calling this
 * function.
 * @param resource  asynchronous resource
 * @param cb        Application specific callback function.
 * @param userData  Application data passed to the callback "as-is".
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred starting the fetch.
 *
 * @sa
 * @b nabtoAsyncInit
 * @b nabtoGetAsyncData
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAsyncFetch(nabto_async_resource_t resource,
                                                          NabtoAsyncStatusCallbackFunc cb,
                                                          void* userData);

/**
 * Retrieves response data requested asynchronously.
 * This function is typically called from inside the
 * NabtoAsyncStatusCallbackFunc when called with NAS_CHUNK_READY.
 * When all data has been fetched from a nabto_async_resource_t it's
 * automatically garbage collected, which means no explicit resource
 * deallocation is necessary.
 * @param resource    asynchronous resource
 * @param buf         Pointer to a buffer to fill with the data to send.
 * @param bufSize     The size in bytes of the buffer.
 * @param actualSize  Return the number of bytes actually written to the buffer.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred retrieving data.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetAsyncData(nabto_async_resource_t resource,
                                                            char* buf,
                                                            size_t bufSize,
                                                            size_t* actualSize);

/**
 * Aborts a outstanding asynchronous request.
 * @param resource    asynchronous resource
 * @return  If the request has been aborted, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_RESOURCE       | asynchronous resource was invalid.
 * NABTO_FAILED                 | an unspecified error occurred aborting request.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoAbortAsync(nabto_async_resource_t resource);


//-----------------------------------------------------------------------------
// Configuration and initialization API.
//-----------------------------------------------------------------------------

/**
 * Get the Nabto software version
 * @param major  The returned major version.
 * @param minor  The returned minor version.
 * @return NABTO_OK
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoVersion(int* major, int* minor);

/**
 * Initializes the Nabto client API.
 * Also initializes the Nabto home directory by removing temporary files
 * and (re)creating the Nabto configuration file.
 * The Nabto home directory also contains directories with certificate
 * stores (users/, roots/), log files (logs/) and the Nabto configuration
 * file (nabto_config.ini). The home directory (and sub-directories) will
 * be created if non-existing.
 * Specify an alternative location of the Nabto home directory in the
 * nabtoHomeDir parameter. The only requirement is that the parent
 * directory must be writable by the user.
 * The nabtoStartup function must be invoked prior to invoking many of the
 * other functions in the Nabto client API.
 * Remember that the nabtoSetApplicationName and nabtoSetOption functions
 * must be called before this function if changes to the Nabto client API
 * is needed.
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
 * Sets the name of your client application. The Nabto client API will use
 * this name to identify data from your application wherever possible.
 * Currently only the log file will use the client application name. The
 * nabtoStartup function will create a log file with the name of the
 * application appending -log.
 * The default application name is nabto-api - hence the default log file
 * name is nabto-api-log.
 * Call this function before nabtoStartup if it should have any effect.
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
 * Tells the Nabto client API the location of the static resource directory.
 * This function will override the default behaviour. The default behaviour
 * is to search several directories relative to the install location of the
 * Nabto client API. Using this function will force the Nabto client API to
 * look for static html driver data in one fixed place.
 * You may call this function at anytime to change the location of the
 * static resource directory.
 * Calling this function with an empty (or NULL) resourceDir will revert to
 * default search behaviour.
 * @param resourceDir        The location of the static resource directory.
 * @return    NABTO_OK. This is the only value returned.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSetStaticResourceDir(const char* resourceDir);

/**
 * Changes the behaviour of the Nabto client API in different ways.@n
 * Each option is given by its name and the new value is passed in the
 * value parameter.@n
 * It is best to call this function before nabtoStartup to be sure the
 * changes have any effect.@n
 * The following options are available:
 *    Option                 Values
 * +----------------------+-----------------------------------------------+
 * | language             | "en" or "da"                                  |
 * | backupConfig         | "yes" or "no"                                 |
 * | configFileName       | any valid file name                           |
 * | clientWebServicePort | 16-bit port number, eg: (const char *) 5583   |
 * +----------------------+-----------------------------------------------+
 * The language option changes the language of the html returned by the
 * Nabto session API. The default language is "en".@n
 * The backupConfig option determines whether to make a backup of the Nabto
 * configuration file in the Nabto home directory before startup. The
 * default is "no". This option must be changed before calling the
 * nabtoStartup function to have any effect.@n
 * The configFileName option changes the file name of the Nabto
 * configuration file (which is located in the Nabto home directory). The
 * default file name is nabto_config.ini. This option must be changed
 * before calling the nabtoStartup function to have any effect.@n
 * The clientWebServicePort option starts an internal web service that
 * allows you to interrogate the internals of the client using a standard
 * web browser. The port number for the web service to listen for incoming
 * requests is given as an 16-bit number cast directly to a const char *.
 * When the web service is running you may inspect the internals using
 * requests like:    http://localhost:5583/api/1/client/threads
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
// Portal
//-----------------------------------------------------------------------------

/**
 * Retrieves email address of installed Nabto client profile.
 * On successful return the given buffer is filled with a newly allocated
 * string containing the email address associated with profile.
 * It is the responsibility of the caller to release the string again using
 * the nabtoFree function.
 * Note that if this function fails, the output parameters are unchanged.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @deprecated  Use nabtoGetCertificates instead since this function
 *              just returns one email address out of potentially many
 *              possible addresses.
 * @param email         Location to place the pointer to the newly allocated
 *                      email address.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_NO_PROFILE             | if no profiles exists.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoLookupExistingProfile(char** email);

/**
 * Creates a Nabto client profile (private key + signed certificate) on this
 * computer for specified registered Nabto user.
 * Note: The base station needed to be contacted for signing certificates is
 * read from the configuration file (default is www.nabto.com).
 * This function will fail with NABTO_PORTAL_LOGIN_FAILURE if the specified
 * user was registered on a different portal such as www.nabtotest.com.
 * So the user must either register on the portal expected by the Nabto
 * client API or change urlPortalDomain in the configuration file to point
 * to the portal where the user is registered.
 * The given password is used to access the portal for specified user. The
 * password will also be used for encrypting the private key.
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
 * Creates a Nabto self signed profile. This creates a self signed
 * certificate. The identity of such certificate cannot be trusted but
 * the fingerprint of the certificate can be trusted in the
 * device. This does not rely on the nabtoSignup function. After the
 * profile has been created it can be used in the open session function.
 * 
 * @param commonName  The common name part of the selfsigned certificate which is going to be made.
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
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoCreateSelfSignedProfile(const char* commonName,
                                                                       const char* password);
    
/**
 * Retrieve public key fingerprint for certificate with specified id.
 * @param certId  The certificate id (common name) of certificate.
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
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoGetFingerprint(const char* certId,
                                                              char fingerprint[16]);

/**
 * Signs up for a Nabto account on the portal (host name defined in the
 * configuration file). Invokes web service and initiates sending of
 * confirmation email.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param email     Email address of user to register on portal.
 * @param password  Requested password for portal.
 * @return  If an account has been created, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_INVALID_ADDRESS        | invalid email address.
 * NABTO_ADDRESS_IN_USE         | email address already in use.
 * NABTO_FAILED                 | profile creation failed for some unspecified reason.
*/
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoSignup(const char* email,
                                                      const char* password);


/**
 * Requests reset of account password on the portal (host name defined in
 * the configuration file).
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * @param email     Email address of user to registered on portal.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_PORTAL_LOGIN_FAILURE   | invalid email and/or password.
 * NABTO_FAILED                 | sign up failed for some unspecified reason.
*/
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoResetAccountPassword(const char* email);


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
 *                            terminated array of allocated c strings.
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
 * Try to connect to probe service to test for network connectivity. If NULL is
 * specified as host, the portal host name from the configuration is used.
 * The nabtoStartup function must have been called prior to calling this
 * function.
 * This function can be used to make the underlying platform start the network
 * it's useful for platforms like Android where the network is shutdown if it's
 * not in use.
 * @param timeoutMillis    timeout before concluding a connection could not be
 *                         established.
 * @param host             hostname of probe service to connect to, using
 *                         default if NULL.
 * @return  If the function succeeds, the return value is NABTO_OK.@n
 *          If the function fails, the return value is one of the
 *          following values.
 *
 * Error code                   | Meaning
 * ---------------------------- | ---------------------
 * NABTO_API_NOT_INITIALIZED    | The @b nabtoStartup function is the first function to call to initialize the Nabto client.
 * NABTO_NO_NETWORK             | if the host couldn't be reached.
 */
NABTO_DECL_PREFIX nabto_status_t NABTOAPI nabtoProbeNetwork(size_t timeoutMillis,
                                                            const char* host);


#ifdef __cplusplus
} // extern c
#endif

#endif // _NABTO_CLIENT_API_HPP