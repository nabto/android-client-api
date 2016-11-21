package com.nabto.api;

import android.content.Context;
import android.util.Log;

import java.util.Collection;

/**
 * The NabtoClient is a simple way to communicate with a Nabto device.
 * <p>
 *     Currently, streams and tunnels are not supported. Use {@link NabtoApi} to access all Nabto
 *     client API features.
 * </p>
 *
 * <p>Example:</p>
 * <pre>{@code
 * NabtoClient client = new NabtoClient(this);
 * client.init("guest", "");
 * UrlResult result = client.fetchUrl("nabto://demo1.nabduino.net/temperature.json?");
 * if (result.getStatus() == NabtoStatus.OK) {
 *     String html = new String(result.getResult());
 *     // do something with HTML string
 * }
 * }</pre>
 */
public class NabtoClient {
    private Context context;
    private NabtoApi nabtoApi;
    private Session session;
    private boolean initialized;
    private String email;
    private String password;

    private static int PROBE_NETWORK_TIMEOUT_MILLIS = 2000;

    /**
     * Create a new Nabto client.
     *
     * @param context App context.
     */
    public NabtoClient(Context context) {
        this.context = context;
        this.nabtoApi = new NabtoApi(context);
    }

    /**
     * Initialize the Nabto application using a specified profile.
     *
     * @param email      The id of an existing certificate.
     * @param password   Password for encrypted private key file.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#NO_NETWORK}</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}</li>
     *              <li>{@link NabtoStatus#CERT_SIGNING_ERROR}</li>
     *              <li>{@link NabtoStatus#CERT_SAVING_FAILURE} (unprobable)</li>
     *              <li>{@link NabtoStatus#OPEN_CERT_OR_PK_FAILED} (unprobable)</li>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED} (unprobable)</li>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG} (unprobable)</li>
     *              <li>{@link NabtoStatus#FAILED} (unprobable)</li>
     *          </ul>
     */
    public NabtoStatus init(String email, String password) {
        this.email = email;
        this.password = password;

        session = openSession(email, password);
        NabtoStatus status = session.getStatus();
        if(status == NabtoStatus.UNLOCK_PK_FAILED || status == NabtoStatus.OPEN_CERT_OR_PK_FAILED) {
            status = createProfile(email, password);
            if(status != NabtoStatus.OK)
                return status;
        }
        session = openSession(email, password);
        status = session.getStatus();
        if(status != NabtoStatus.OK) {
            return status;
        }

        initialized = true;
        return status;
    }

    public void pause() {
        nabtoApi.closeSession(session);
        nabtoApi.shutdown();
    }

    public boolean resume() {
        startup();
        Session s = null;
        if (initialized) {
            session = openSession(email, password);
            if (session.getStatus() != NabtoStatus.OK) {
                Log.d(this.getClass().getSimpleName(), "Failed to resume api");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Get the Nabto software version ("[major].[minor]").
     *
     * @return The Nabto software version.
     */
    public String nabtoVersion() {
        return nabtoApi.version();
    }

    /**
     * Tells the Nabto client API the location of the static resource directory ("share/nabto")
     * and initializes the Nabto client API.
     *
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#FAILED}: The path to home directory wasn't found OR error
     *              in the C API wrapper</li>
     *          </ul>
     */
    private NabtoStatus startup() {
        nabtoApi.setStaticResourceDir();
        return nabtoApi.startup();
    }

    /**
     * Returns a collection of known prefixes in the location specified by prefixes.
     *
     * @return  Collection of known prefixes in the location specified by prefixes.
     */
    public Collection<String> getProtocolPrefixes() {
        startup();
        return nabtoApi.getProtocolPrefixes();
    }

    /**
     * Return a collection of local discoverable devices.
     *
     * @return  Collection of local discoverable devices.
     */
    public Collection<String> getLocalDevices() {
        startup();
        return nabtoApi.getLocalDevices();
    }

    /**
     * Try to connect to probe service to test for network connectivity.
     * <p>
     *     If NULL is specified as host, the portal host name from the configuration is used.
     * </p>
     * <p>
     *     This function can be used to make the underlying platform start the network.
     * </p>
     *
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#NO_NETWORK}: The host couldn't be reached.</li>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#FAILED}: The path to home directory wasn't found OR error
     *              in the C API wrapper</li>
     *          </ul>
     */
    public NabtoStatus probeNetwork() {
        NabtoStatus status = nabtoApi.probeNetwork(PROBE_NETWORK_TIMEOUT_MILLIS, null);
        if (status == NabtoStatus.API_NOT_INITIALIZED) {
            status = startup();
            if(status == NabtoStatus.OK) {
                status = nabtoApi.probeNetwork(PROBE_NETWORK_TIMEOUT_MILLIS, null);
            }
        }
        return status;
    }

    /**
     * Returns a collection of currently known certificates on the system.
     *
     * @return  Collection of currently known certificates on the system.
     */
    public Collection<String> getCertificates() {
        startup();
        return nabtoApi.getCertificates();
    }

    /**
     * Creates a Nabto client profile (private key + signed certificate) on this
     * computer for specified registered Nabto user.
     * <p>
     *     The base station needed to be contacted for signing certificates is
     *     read from the configuration file (default is www.nabto.com).
     * </p>
     * <p>
     *     This function will fail with {@link NabtoStatus#PORTAL_LOGIN_FAILURE} if
     *     the specified user was registered on a different portal such as www.nabtotest.com.
     *     So the user must either register on the portal expected by the Nabto
     *     client API or change {@code urlPortalDomain} in the configuration file to point
     *     to the portal where the user is registered.
     * </p>
     * <p>
     *     The given password is used to access the portal for specified user. The
     *     password will also be used for encrypting the private key.
     * </p>
     *
     * @param email     Email address of user, as registered on portal.
     * @param password  Password for accessing portal for specified user.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#NO_NETWORK}: The host couldn't be reached.</li>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}: Bad password was specified.</li>
     *              <li>{@link NabtoStatus#CERT_SAVING_FAILURE}: Could not save signed certificate.</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}: Invalid email and/or password.</li>
     *              <li>{@link NabtoStatus#CERT_SIGNING_ERROR}: Failed to sign certificate request.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    private NabtoStatus createProfile(String email, String password) {
        NabtoStatus status = probeNetwork();
        if(status != NabtoStatus.OK) {
            return status;
        }
        status = nabtoApi.createProfile(email, password);
        if (status == NabtoStatus.API_NOT_INITIALIZED) {
            status = startup();
            if(status == NabtoStatus.OK) {
                status = nabtoApi.createProfile(email, password);
            }
        }
        return status;
    }

    /**
     * Signs up for a Nabto account on the portal (host name defined in the
     * configuration file). Invokes web service and initiates sending of
     * confirmation email.
     *
     * @param email     Email address of user to register on portal.
     * @param password  Requested password for portal.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#NO_NETWORK}: The host couldn't be reached.</li>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_ADDRESS}: Invalid email address.</li>
     *              <li>{@link NabtoStatus#ADDRESS_IN_USE}: Email address already in use.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public NabtoStatus signup(String email, String password) {
        NabtoStatus status = probeNetwork();
        if(status != NabtoStatus.OK) {
            return status;
        }
        status = nabtoApi.signup(email, password);
        if (status == NabtoStatus.API_NOT_INITIALIZED) {
            status = startup();
            if(status == NabtoStatus.OK) {
                status = nabtoApi.signup(email, password);
            }
        }
        return status;
    }

    /**
     * Requests reset of account password on the portal (host name defined in
     * the configuration file).
     *
     * @param email     Email address of user to registered on portal.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#NO_NETWORK}: The host couldn't be reached.</li>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}: Invalid email and/or password.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public NabtoStatus resetAccountPassword(String email) {
        NabtoStatus status = probeNetwork();
        if(status != NabtoStatus.OK) {
            return status;
        }
        status = nabtoApi.resetAccountPassword(email);
        if (status == NabtoStatus.API_NOT_INITIALIZED) {
            status = startup();
            if(status == NabtoStatus.OK) {
                status = nabtoApi.resetAccountPassword(email);
            }
        }
        return status;
    }

    /**
     * Starts a new Nabto data retrieval session using the specified profile.
     * <p>
     *     The email address given must be an id of an existing certificate
     *     for a certain Nabto enabled device. If the device is unencrypted, specify
     *     the empty string for the password argument.
     * </p>
     * <p>
     *     This function returns a session object that must be used in subsequent
     *     client API invocations.
     * </p>
     * <p>
     *     When the session is no longer needed, close it by using
     *     the {@link NabtoApi#closeSession(Session)} function.
     * </p>
     *
     * @param email      The id of an existing certificate.
     * @param password   Password for encrypted private key file. Specify
     *                   an empty string for an unencrypted key file.
     * @return  A {@link Session} object. If the function succeeds, the return value of
     *          {@link Session#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link Session#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#OPEN_CERT_OR_PK_FAILED}: No matching profile found for
     *              the given email address.</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}: A bad password was specified in the
     *              password argument.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    private Session openSession(String email, String password) {
        Session session = nabtoApi.openSession(email, password);
        if(session.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new Session(null, status.toInteger());
            }
            session = nabtoApi.openSession(email, password);
        }
        return session;
    }

    /**
     * Sets the default RPC interface to use when later invoking
     * {@link #rpcInvoke(String)}.
     * <p>
     *     Can be overriden for specific hosts with
     *     {@link #rpcSetInterface(String, String)}.
     * </p>
     * <p>
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains
     *     error details. Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param interfaceDefinition  The interface definition as XML formatted string.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid. See
     *              {@link #init(String, String)}</li>
     *              <li>{@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}: An error occurred with details
     *              in {@link RpcResult#getJson()}.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public RpcResult rpcSetDefaultInterface(String interfaceDefinition) {
        RpcResult result = nabtoApi.rpcSetDefaultInterface(interfaceDefinition, session);
        if(result.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new RpcResult(null, status.toInteger());
            }
            result = nabtoApi.rpcSetDefaultInterface(interfaceDefinition, session);
        }
        return result;
    }

    /**
     * Sets the RPC interface to use for a specific host when later invoking
     * {@link #rpcInvoke(String)}.
     * <p>
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains
     *     error details. Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param nabtoHost            The host for which the interface is to be used
     *                             later RPC invocations.
     * @param interfaceDefinition  The interface definition as XML formatted string.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid. See
     *              {@link #init(String, String)}</li>
     *              <li>{@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}: An error occurred with details
     *              in {@link RpcResult#getJson()}.</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public RpcResult rpcSetInterface(String nabtoHost, String interfaceDefinition) {
        RpcResult result = nabtoApi.rpcSetInterface(nabtoHost, interfaceDefinition, session);
        if(result.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new RpcResult(null, status.toInteger());
            }
            result = nabtoApi.rpcSetInterface(nabtoHost, interfaceDefinition, session);
        }
        return result;
    }

    /**
     * Retrieves data synchronously from specified {@code nabto://URL}.
     * <p>
     *     As compared to {@link #fetchUrl(String)}, this function only
     *     supports function invocation and not displaying contents of an HTML DD bundle.
     *     This also means the interface file is to specified prior to invocation using either
     *     {@link #rpcSetInterface(String, String)} or
     *     {@link #rpcSetDefaultInterface(String)}
     * </p>
     * <p>
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains
     *     error details. Otherwise, the JSON string is undefined.
     * </p>
     * <p>
     *     On successful return ({@link RpcResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link RpcResult#getJson()} returns a JSON response representing the response document.
     *     Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param nabtoUrl       The URL to retrieve.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK} and
     *          {@link RpcResult#getJson()} returns the JSON response. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid. See
     *              {@link #init(String, String)}</li>
     *              <li>{@link NabtoStatus#RPC_INTERFACE_NOT_SET}: An interface was not set prior
     *              to invoking.</li>
     *              <li>{@link NabtoStatus#RPC_NO_SUCH_REQUEST}: The specified RPC function does
     *              not exist in request</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public RpcResult rpcInvoke(String nabtoUrl) {
        RpcResult result = nabtoApi.rpcInvoke(nabtoUrl, session);
        if(result.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new RpcResult(null, status.toInteger());
            }
            result = nabtoApi.rpcInvoke(nabtoUrl, session);
        }
        return result;
    }

    /**
     * Retrieves data synchronously from specified {@code nabto://URL}.
     * <p>
     *     On successful return ({@link UrlResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link UrlResult#getResult()} returns the HTML code. The MIME type of the data
     *     is returned by {@link UrlResult#getMimeType()}.
     * </p>
     *
     * @param nabtoUrl        The URL to retrieve.
     * @return  A {@link UrlResult} object. If the function succeeds, the return value of
     *          {@link UrlResult#getStatus()} is {@link NabtoStatus#OK} and
     *          {@link UrlResult#getResult()} returns the HTML code. If the function fails,
     *          the return value of {@link UrlResult#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid. See
     *              {@link #init(String, String)}</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public UrlResult fetchUrl(String nabtoUrl) {
        UrlResult result = nabtoApi.fetchUrl(nabtoUrl, session);
        if(result.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new UrlResult(null, null, status.toInteger());
            }
            result = nabtoApi.fetchUrl(nabtoUrl, session);
        }
        return result;
    }

    /**
     * Submits specified data synchronously to specified URL.
     * <p>
     *     If destination host is HTTP enabled, use HTTP POST semantics
     *     for the submission. Note: Only "application/x-www-form-urlencoded" type
     *     data is currently supported as data for submission (any type of data may
     *     still be retrieved, though).
     * </p>
     * <p>
     *     On successful return ({@link UrlResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link UrlResult#getResult()} returns the HTML code. The MIME type of the data
     *     is returned by {@link UrlResult#getMimeType()}.
     * </p>
     *
     * @param nabtoUrl              The URL to submit data to.
     * @param postData              The data to submit.
     * @param postMimeType          MIME type of data to submit.
     * @return  A {@link UrlResult} object. If the function succeeds, the return value of
     *          {@link UrlResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails,
     *          the return value of {@link UrlResult#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#ERROR_READING_CONFIG}: The config file wasn't read.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid. See
     *              {@link #init(String, String)}</li>
     *              <li>{@link NabtoStatus#FAILED}: Unknown error.</li>
     *          </ul>
     */
    public UrlResult submitPostData(String nabtoUrl, byte[] postData,
                                    String postMimeType) {
        UrlResult result = nabtoApi.submitPostData(nabtoUrl, postData, postMimeType, session);
        if(result.getStatus() == NabtoStatus.API_NOT_INITIALIZED) {
            NabtoStatus status = startup();
            if(status != NabtoStatus.OK) {
                return new UrlResult(null, null, status.toInteger());
            }
            result = nabtoApi.submitPostData(nabtoUrl, postData, postMimeType, session);
        }
        return result;
    }

    /**
     * Reads the session token of the application's session handle. This token is
     * typically supplied in html requests.
     * @return  If the function succeeds, the return value is the session token.
     *          If the function fails, the return value is an empty string. This can occur if the
     *          Nabto client is not initialized (see
     *          {@link #init(String, String)}) or
     *          an unspecified error occurred.
     */
    public String getSessionToken() {
        return nabtoApi.getSessionToken(session);
    }
}