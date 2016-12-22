package com.nabto.api;

/**
 * The NabtoStatus enumeration is the set of status values that most
 * of the functions in the Client API return.
 */
public enum NabtoStatus {
    /**
     * The operation was successful.
     */
    OK,

    /**
     * No user profile found in the home directory.
     */
    NO_PROFILE,

    /**
     * The client could not read the configuration file.
     */
    ERROR_READING_CONFIG,

    /**
     * The nabtoStartup() function must be the first function called.
     */
    API_NOT_INITIALIZED,

    /**
     * The operation requires a valid session. Perhaps an invalid session
     * handle was specified.
     */
    INVALID_SESSION,

    /**
     * A certificate or private key files could not be opened.
     */
    OPEN_CERT_OR_PK_FAILED,

    /**
     * A private key could not be decrypted with specified password.
     */
    UNLOCK_PK_FAILED,

    /**
     * Could not login to portal to sign certificate. An invalid
     * email/password was given for active portal. The active portal
     * is given as the urlPortalDomain in the configuration file.
     */
    PORTAL_LOGIN_FAILURE,

    /**
     * The portal failed when signing certificate.
     */
    CERT_SIGNING_ERROR,

    /**
     * The portal could not save a signed certificate.
     */
    CERT_SAVING_FAILURE,

    /**
     * User cannot sign up with specified email address as it is
     * already in use.
     */
    ADDRESS_IN_USE,

    /**
     * User cannot sign up with specified email address, as it is invalid.
     */
    INVALID_ADDRESS,

    /**
     * No network available.
     */
    NO_NETWORK,

    /**
     * Client could not connect to specified host.
     */
    CONNECT_TO_HOST_FAILED,

    /**
     * Client peer does not support streaming.
     */
    STREAMING_UNSUPPORTED,

    /**
     * The operation requires a valid stream. Perhaps an invalid stream
     * handle was specified.
     */
    INVALID_STREAM,

    /**
     * Unacknowledged stream data is pending.
     */
    DATA_PENDING,

    /**
     * All stream data slots are full.
     */
    BUFFER_FULL,

    /**
     * An unspecified error occurred. It is necessary to check the
     * log file to find out what actually went wrong.
     */
    FAILED,

    /**
     * The operation requires a valid tunnel. Perhaps an invalid tunnel
     * handle was specified.
     */
    INVALID_TUNNEL,

    /**
     * A parameter to a function is not supported.
     */
    ILLEGAL_PARAMETER,

    /**
     * The operation requires a valid resource. Perhaps an invalid
     * asynchronous resource was specified.
     */
    INVALID_RESOURCE,

    /**
     * The stream option is invalid.
     */
    INVALID_STREAM_OPTION,

    /**
     * The stream options argument is invalid, e.g. wrong size etc.
     */
    INVALID_STREAM_OPTION_ARGUMENT,

    /**
     * The operation has been aborted.
     */
    ABORTED,

    /**
     * The Stream has been closed gracefully.
     */
    STREAM_CLOSED,

    /**
     * An error occurred with detailed described in JSON document
     */
    FAILED_WITH_JSON_MESSAGE,

    /**
     * There was not set an RPC interface for host prior to invocation
     */
    RPC_INTERFACE_NOT_SET,

    /**
     * The interface set for this host does not specify this request
     */
    RPC_NO_SUCH_REQUEST,

    /**
     * Requested device is offline
     */
    RPC_DEVICE_OFFLINE,

    /**
     * Response could not be decoded.
     */
    RPC_RESPONSE_DECODE_FAILURE,

    /**
     * Problem communicating with RPC target device
     */
    NABTO_RPC_COMMUNICATION_PROBLEM,

    /**
     * Timeout when connecting to remote device.
     */
    NABTO_CONNECT_TIMEOUT,

    /**
     * Number of possible error codes. This must always be last!
     */
    ERROR_CODE_COUNT;

    static NabtoStatus fromInteger(int val) {
        if (val < NabtoStatus.values().length && val >= 0) {
            return NabtoStatus.values()[val];
        } else {
            return FAILED;
        }
    }

    int toInteger() {
        return this.ordinal();
    }
}
