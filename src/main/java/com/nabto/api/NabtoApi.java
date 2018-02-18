package com.nabto.api;

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Nabto client API.
 *
 * <p>Example:</p>
 * <pre>{@code
 * NabtoApi api = new NabtoApi(new NabtoAndroidAssetManager(this));
 * NabtoStatus status = api.startup();
 * if (status == NabtoStatus.OK) {
 *     Session session = api.openSession("guest", "");
 *     if (session.getStatus() == NabtoStatus.OK) {
 *         String url = "nabto://demo1.nabduino.net/temperature.json?";
 *         UrlResult result = client.fetchUrl(url, session);
 *         if (result.getStatus() == NabtoStatus.OK) {
 *             String html = new String(result.getResult());
 *             // do something with HTML string
 *         }
 *         api.closeSession(session);
 *     }
 *     api.shutdown();
 * }
 * }</pre>
 */
public class NabtoApi {
    private NabtoAssetManager assetManager;

    public NabtoApi(NabtoAssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Get the Nabto software version ("[major].[minor]").
     *
     * @return The Nabto software version.
     * @deprecated use versionString intead
     */
    @Deprecated
    public String version() {
        return NabtoCApiWrapper.nabtoVersion();
    }

    /**
     * Get the Nabto software version string, following semver scheme with prerelease suffix:
     *   "major.minor.patch[-prelease-suffix]". Eg., 4.1.1-rc.1+23474.
     *
     * @return The Nabto software version string.
     */
    public String versionString() {
        return NabtoCApiWrapper.nabtoVersionString();
    }


    /**
     * Initializes the Nabto client API.
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
    public NabtoStatus startup() {
        NabtoCApiWrapper.nabtoSetStaticResourceDir(assetManager.getNabtoResourceDirectory());
        String dir = assetManager.getNabtoHomeDirectory();
        NabtoStatus status =  NabtoCApiWrapper.nabtoStartup(dir);
        if (status == NabtoStatus.OK) {
            status = NabtoCApiWrapper.nabtoInstallDefaultStaticResources(assetManager.getNabtoHomeDirectory());
            if (status == NabtoStatus.OK) {
                Log.i(this.getClass().getSimpleName(), "Started Nabto Client SDK version " + versionString() + " in " + dir);
            } else {
                Log.e(this.getClass().getSimpleName(), "Nabto started but resources could not be installed in " + dir);
            }
        } else {
            Log.e(this.getClass().getSimpleName(), "Failed to startup Nabto client API: " + status);
        }
        return status;
    }

    /**
     * Override default static resource dir.
     * @param dir the dir override
     *
     * @return  The return value is always {@link NabtoStatus#OK}.
     */
    public NabtoStatus setStaticResourceDir(String dir) {
        return NabtoCApiWrapper.nabtoInstallDefaultStaticResources(dir);
    }

    /**
     * Terminates the Nabto client API.
     * <p>
     *     Releases any resources held by the Nabto client API.
     *     After each successful call to {@link #startup()} call this
     *     function when the Nabto client API is no longer needed. This function
     *     can block for a small amount of time until all current sessions has
     *     closed properly.
     * </p>
     * <p>
     *     Upon return the Nabto client API is no longer available. The Nabto
     *     client API can be re-initialized by calling {@link #startup()} again.
     * </p>
     *
     * @return {@link NabtoStatus#OK} is the only value returned.
     */
    public NabtoStatus shutdown() {
        NabtoStatus status = NabtoCApiWrapper.nabtoShutdown();
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to shutdown Nabto client API: " + status);
        }
        return status;
    }

    /**
     * Set API option. See nabto_client_api.h for details
     * @param name  The name of the option to change.
     * @param value The new value of the option.
     * @return {@link NabtoStatus#OK} if ok
     */
    public NabtoStatus setOption(String name, String value) {
        NabtoStatus status = NabtoCApiWrapper.nabtoSetOption(name, value);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to set option on Nabto client API: " + status);
        }
        return status;
    }

    /**
     * Returns a collection of known prefixes in the location specified by prefixes.
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @return  Collection of known prefixes in the location specified by prefixes.
     */
    public Collection<String> getProtocolPrefixes() {
        ArrayList<String> prefixes = new ArrayList<String>();
        String[] res = NabtoCApiWrapper.nabtoGetProtocolPrefixes();
        if (res != null) {
            for (String s : res) {
                prefixes.add(s);
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "Failed to get protocol prefixes.");
        }
        return prefixes;
    }

    /**
     * Return a collection of local discoverable devices.
     *
     * @return  Collection of local discoverable devices.
     */
    public Collection<String> getLocalDevices() {
        ArrayList<String> devices = new ArrayList<String>();
        String[] res = NabtoCApiWrapper.nabtoGetLocalDevices();
        if (res != null) {
            for (String s : res) {
                devices.add(s);
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "Failed to get local devices.");
        }
        return devices;
    }

    /**
     * Try to connect to probe service to test for network connectivity.
     * <p>
     *     If NULL is specified as host, the portal host name from the configuration is used.
     * </p>
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     * <p>
     *     This function can be used to make the underlying platform start the network.
     * </p>
     *
     * @param timeoutMillis    timeout before concluding a connection could not be
     *                         established.
     * @param hostname         hostname of probe service to connect to, using
     *                         default if NULL.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#NO_NETWORK}: The host couldn't be reached.</li>
     *              <li>{@link NabtoStatus#FAILED}: Error in the C API wrapper.</li>
     *          </ul>
     */
    public NabtoStatus probeNetwork(int timeoutMillis, String hostname) {
        NabtoStatus status = NabtoCApiWrapper.nabtoProbeNetwork(timeoutMillis, hostname);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to probe network: " + status);
        }
        return status;
    }

    /**
     * Returns a collection of currently known certificates on the system.
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @return  Collection of currently known certificates on the system.
     */
    public Collection<String> getCertificates() {
        ArrayList<String> certificates = new ArrayList<String>();
        String[] res = NabtoCApiWrapper.nabtoGetCertificates();
        if (res != null) {
            for (String s : res) {
                certificates.add(s);
            }
        } else {
            Log.d(this.getClass().getSimpleName(), "Failed to get certificates.");
        }
        return certificates;
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
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @param id     id of user, as registered on portal.
     * @param password  Password for accessing portal for specified user.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}: Bad password was specified.</li>
     *              <li>{@link NabtoStatus#CERT_SAVING_FAILURE}: Could not save signed certificate.</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}: Invalid email and/or password.</li>
     *              <li>{@link NabtoStatus#CERT_SIGNING_ERROR}: Failed to sign certificate request.</li>
     *              <li>{@link NabtoStatus#FAILED}: Lookup failed for some unspecified reason.</li>
     *          </ul>
     */
    public NabtoStatus createProfile(String id, String password) {
        NabtoStatus status = NabtoCApiWrapper.nabtoCreateProfile(id, password);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to create profile: " + status);
        }
        return status;
    }

    /**
     * Remove a Nabto client profile (private key + certificate) on this
     * computer for specified user.
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @param   certId identification of certificate to remove
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#CERT_SAVING_FAILURE}: Failed to remove certificate.</li>
     *              <li>{@link NabtoStatus#FAILED}: Lookup failed for some unspecified reason.</li>
     *          </ul>
     */
    public NabtoStatus removeProfile(String certId) {
        NabtoStatus status = NabtoCApiWrapper.nabtoRemoveProfile(certId);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to remove profile: " + status);
        }
        return status;
    }

    /**
     * Creates a Nabto self signed profile.
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     * <p>
     *     This creates a self signed certificate. The identity of such certificate cannot be
     *     trusted but the fingerprint of the certificate can be trusted in the device. This does
     *     not rely on the {@link #signup(String, String)} function. After the profile has been
     *     created it can be used in the open session function.
     * </p>
     * @param commonName  The common name part of the selfsigned certificate which is going to be made.
     * @param password    The password which protects the private key.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}: Bad password was specified.</li>
     *              <li>{@link NabtoStatus#CERT_SAVING_FAILURE}: Could not save signed certificate.</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}: Invalid email and/or password.</li>
     *              <li>{@link NabtoStatus#CERT_SIGNING_ERROR}: Failed to sign certificate request.</li>
     *              <li>{@link NabtoStatus#FAILED}: Lookup failed for some unspecified reason.</li>
     *          </ul>
     */
    public NabtoStatus createSelfSignedProfile(String commonName, String password) {
        NabtoStatus status = NabtoCApiWrapper.nabtoCreateSelfSignedProfile(commonName, password);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to create profile: " + status);
        }
        return status;
    }

    /**
     * Retrieve public key fingerprint for certificate with specified id.
     * @param certId        Certificate ID from which the fingerprint should be retreived
     * @param fingerprint   The RSA public key fingerprint (buffer of 16 bytes owned by caller)
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#OPEN_CERT_OR_PK_FAILED}: No matching certificate found
     *              for the given id.</li>
     *              <li>{@link NabtoStatus#FAILED}: Lookup failed for some unspecified reason.</li>
     *          </ul>
     */
    public NabtoStatus getFingerprint(String certId, String[] fingerprint){
        byte [] byteFingerprint = new byte[16];
        NabtoStatus ret = NabtoCApiWrapper.nabtoGetFingerprint(certId, byteFingerprint);
        for (int i = 0; i<byteFingerprint.length; i++){
            fingerprint[0] += String.format("%02x",byteFingerprint[i]);
//            String str = String.format("%02x",byteFingerprint[i]);
//            fingerprint[0] += i!=0 ? ":" + str : str; // removed ':' insertion here, should be done in application
        }
        return ret;
    }

    /**
     * Signs up for a Nabto account on the portal (host name defined in the
     * configuration file). Invokes web service and initiates sending of
     * confirmation email.
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @param email     Email address of user to register on portal.
     * @param password  Requested password for portal.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_ADDRESS}: Invalid email address.</li>
     *              <li>{@link NabtoStatus#ADDRESS_IN_USE}: Email address already in use.</li>
     *              <li>{@link NabtoStatus#FAILED}: Profile creation failed for some unspecified
     *              reason.</li>
     *          </ul>
     */
    public NabtoStatus signup(String email, String password) {
        NabtoStatus status = NabtoCApiWrapper.nabtoSignup(email, password);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to sign up: " + status);
        }
        return status;
    }

    /**
     * Requests reset of account password on the portal (host name defined in
     * the configuration file).
     * <p>
     *     The {@link #startup()} function must have been called prior to calling this
     *     function.
     * </p>
     *
     * @param email     Email address of user to registered on portal.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#PORTAL_LOGIN_FAILURE}: Invalid email and/or password.</li>
     *              <li>{@link NabtoStatus#FAILED}: Sign up failed for some unspecified reason.</li>
     *          </ul>
     */
    public NabtoStatus resetAccountPassword(String email) {
        NabtoStatus status = NabtoCApiWrapper.nabtoResetAccountPassword(email);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to reset account password: " + status);
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
     *     the {@link #closeSession(Session)} function.
     * </p>
     * <p>
     *     The {@link #startup()} function must have been called prior to calling
     *     this function.
     * </p>
     *
     * @param id         The id of an existing certificate. Either an email address or commonName.
     * @param password   Password for encrypted private key file. Specify
     *                   an empty string for an unencrypted key file.
     * @return  A {@link Session} object. If the function succeeds, the return value of
     *          {@link Session#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link Session#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#OPEN_CERT_OR_PK_FAILED}: No matching profile found for
     *              the given email address.</li>
     *              <li>{@link NabtoStatus#UNLOCK_PK_FAILED}: A bad password was specified in the
     *              password argument.</li>
     *              <li>{@link NabtoStatus#FAILED}: The login failed for some unspecified reason.</li>
     *          </ul>
     */
    public Session openSession(String id, String password) {
        Session session = NabtoCApiWrapper.nabtoOpenSession(id, password);
        if(session.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to open session: " + session.getStatus());
        }
        return session;
    }

    /**
     * Starts a new Nabto data retrieval session without using a certificate.
     * <p>
     *     This function returns a session object that must be used in subsequent
     *     client API invocations.
     * </p>
     * <p>
     *     When the session is no longer needed, close it by using
     *     the {@link #closeSession(Session)} function.
     * </p>
     * <p>
     *     The {@link #startup()} function must have been called prior to calling
     *     this function.
     * </p>
     *
     * @return  A {@link Session} object. If the function succeeds, the return value of
     *          {@link Session#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link Session#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#FAILED}: Error in the C API wrapper.</li>
     *          </ul>
     */
    public Session openSessionBare() {
        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        if(session.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to open bare session: " + session.getStatus());
        }
        return session;
    }

    /**
     * Closes the specified Nabto session, preventing further data retrieval.
     * <p>
     *     The session object passed to this function must be returned by a call to either
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     * <p>
     *     Upon return the session object is no longer valid.
     * </p>
     *
     * @param session        session object
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session object was invalid.</li>
     *          </ul>
     */
    public NabtoStatus closeSession(Session session) {
        NabtoStatus status = NabtoCApiWrapper.nabtoCloseSession(session);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to close session: " + status);
        }
        return status;
    }

    /**
     * Set basestation auth information on connect requests.
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
     * <p>
     *     The session object passed to this function must be returned by a call to either
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     *
     * @param session   session handle
     * @param jsonKeyValuePairs  valid json key value pairs like '{"foo": "bar", "baz": "quux" }'
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session object was invalid.</li>
     *          </ul>
     */
    public NabtoStatus setBasestationAuthJson(String jsonKeyValuePairs, Session session) {
        NabtoStatus status = NabtoCApiWrapper.nabtoSetBasestationAuthJson(jsonKeyValuePairs, session);
        if (status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to set basestation authentication info: " + status);
        }
        return status;
    }

    /**
     * Sets the default RPC interface to use when later invoking
     * {@link #rpcInvoke(String, Session)}.
     * <p>
     *     Can be overriden for specific hosts with
     *     {@link #rpcSetInterface(String, String, Session)}.
     * </p>
     * <p>
     *     The session object given must have been obtained by a call to
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     * <p>
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains
     *     error details. Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param interfaceDefinition  The interface definition as XML formatted string.
     * @param session              session handle.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}: An error occurred with details
     *              in {@link RpcResult#getJson()}.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred handling the
     *              request.</li>
     *          </ul>
     */
    public RpcResult rpcSetDefaultInterface(String interfaceDefinition, Session session) {
        RpcResult rpcResult = NabtoCApiWrapper
                .nabtoRpcSetDefaultInterface(interfaceDefinition, session);
        if(rpcResult.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to set default RPC interface: " + rpcResult.getStatus());
        }
        return rpcResult;
    }

    /**
     * Sets the RPC interface to use for a specific host when later invoking
     * {@link #rpcInvoke(String, Session)}.
     * <p>
     *     The session object given must have been obtained by a call to
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     * <p>
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains
     *     error details. Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param nabtoHost            The host for which the interface is to be used
     *                             later RPC invocations.
     * @param interfaceDefinition  The interface definition as XML formatted string.
     * @param session              session handle.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}: An error occurred with details
     *              in {@link RpcResult#getJson()}.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred handling the
     *              request.</li>
     *          </ul>
     */
    public RpcResult rpcSetInterface(String nabtoHost, String interfaceDefinition, Session session) {
        RpcResult rpcResult = NabtoCApiWrapper
                .nabtoRpcSetInterface(nabtoHost, interfaceDefinition, session);
        if(rpcResult.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to set RPC interface: " + rpcResult.getStatus());
        }
        return rpcResult;
    }

    /**
     * Retrieves data synchronously from specified {@code nabto://URL} on
     * specified session.
     * <p>
     *     As compared to {@link #fetchUrl(String, Session)}, this function only
     *     supports function invocation and not displaying contents of an HTML DD bundle.
     *     This also means the interface file is to specified prior to invocation using either
     *     {@link #rpcSetInterface(String, String, Session)} or
     *     {@link #rpcSetDefaultInterface(String, Session)}
     * </p>
     * <p>
     *     The session object given must have been obtained by a call to
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     * <p>
     *     On successful return ({@link RpcResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link RpcResult#getJson()} contains a JSON response representing the response document.
     *     If a non-API level error occurred ({@link RpcResult#getStatus()} returns
     *     {@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}), {@link RpcResult#getJson()} contains error
     *     details in JSON format.
     * </p>
     * <p>
     *     On successful return ({@link RpcResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link RpcResult#getJson()} returns a JSON response representing the response document.
     *     Otherwise, the JSON string is undefined.
     * </p>
     *
     * @param nabtoUrl       The URL to retrieve.
     * @param session        session handle.
     * @return  A {@link RpcResult} object. If the function succeeds, the return value of
     *          {@link RpcResult#getStatus()} is {@link NabtoStatus#OK} and
     *          {@link RpcResult#getJson()} returns the JSON response. If the function fails, the
     *          return value of {@link RpcResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED_WITH_JSON_MESSAGE}: An error occurred, details
     *              can be found in JSON format in the response.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred handling the
     *              request.</li>
     *          </ul>
     */
    public RpcResult rpcInvoke(String nabtoUrl, Session session) {
        RpcResult rpcResult = NabtoCApiWrapper.nabtoRpcInvoke(nabtoUrl, session);
        if(rpcResult.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to invoke RPC: " + rpcResult.getStatus());
        }
        return rpcResult;
    }

   
    /**
     * Retrieves data synchronously from specified {@code nabto://URL} on specified
     * session.
     * <p>
     *     The session object given must have been obtained by a call to
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
     * </p>
     * <p>
     *     On successful return ({@link UrlResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link UrlResult#getResult()} returns the HTML code. The MIME type of the data
     *     is returned by {@link UrlResult#getMimeType()}.
     * </p>
     *
     * @param nabtoUrl        The URL to retrieve.
     * @param session         session handle.
     * @return  A {@link UrlResult} object. If the function succeeds, the return value of
     *          {@link UrlResult#getStatus()} is {@link NabtoStatus#OK} and
     *          {@link UrlResult#getResult()} returns the HTML code. If the function fails,
     *          the return value of {@link UrlResult#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred handling the
     *              request.</li>
     *          </ul>
     */
    public UrlResult fetchUrl(String nabtoUrl, Session session) {
        UrlResult result = NabtoCApiWrapper.nabtoFetchUrl(nabtoUrl, session);
        if(result.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to fetch URL: " + result.getStatus());
        }
        return result;
    }

    /**
     * Submits specified data synchronously to specified URL through specified
     * session.
     * <p>
     *     If destination host is HTTP enabled, use HTTP POST semantics
     *     for the submission. Note: Only "application/x-www-form-urlencoded" type
     *     data is currently supported as data for submission (any type of data may
     *     still be retrieved, though).
     * </p>
     * <p>
     *     The session object given must have been obtained by a call to
     *     {@link #openSession(String, String)} or {@link #openSessionBare()}.
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
     * @param session               session handle.
     * @return  A {@link UrlResult} object. If the function succeeds, the return value of
     *          {@link UrlResult#getStatus()} is {@link NabtoStatus#OK}. If the function fails,
     *          the return value of {@link UrlResult#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred handling the
     *              request.</li>
     *          </ul>
     */
    public UrlResult submitPostData(String nabtoUrl, byte[] postData,
                                    String postMimeType, Session session) {
        UrlResult result = NabtoCApiWrapper.nabtoSubmitPostData(nabtoUrl, postData,
                postMimeType, session);
        if(result.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to submit post data: " + result.getStatus());
        }
        return result;
    }

    /**
     * Reads the session token of the specified session handle. This token is
     * typically supplied in html requests.
     * @param session        Session handle.
     * @return  If the function succeeds, the return value is the session token.
     *          If the function fails, the return value is an empty string. This can occur if the
     *          Nabto client is not initialized (see {@link #startup()} or an unspecified
     *          error occurred.
     */
    public String getSessionToken(Session session) {
        String token = NabtoCApiWrapper.nabtoGetSessionToken(session);
        if(token == null) {
            Log.d(this.getClass().getSimpleName(), "Failed get session token.");
            token = "";
        }
        return token;
    }

    /**
     * Opens a stream on an existing session to a Nabto enabled device.
     * <p>
     *     This function returns a stream handle that must be used in subsequent
     *     client API invocations.
     * </p>
     * <p>
     *     The caller must call {@link #streamClose(Stream)} when the stream is no
     *     longer needed.
     * </p>
     * <p>
     *     An open session handle must have been created prior to calling this function.
     * </p>
     *
     * @param nabtoHost      The host to open a stream to.
     * @param session        session handle
     * @return  A {@link Stream} object. If the function succeeds, the return value of
     *          {@link Stream#getStatus()} is {@link NabtoStatus#OK}. If the function fails,
     *          the return value of {@link Stream#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#STREAMING_UNSUPPORTED}: Peer does not support streaming.</li>
     *              <li>{@link NabtoStatus#CONNECT_TO_HOST_FAILED}: Could not connect to specified
     *              host.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred creating the
     *              stream.</li>
     *          </ul>
     */
    public Stream streamOpen(String nabtoHost, Session session) {
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(nabtoHost, session);
        if(stream.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to open stream: " + stream.getStatus());
        }
        return stream;
    }

    /**
     * Closes an open stream.
     * <p>
     *     The return value is {@link NabtoStatus#DATA_PENDING} if unacknowledged data is still
     *     pending on the stream. Keep invoking {@link #streamClose(Stream)} until the
     *     return value is {@link NabtoStatus#OK}. Alternatively close the owing session to force
     *     closing all the open streams with pending data.
     * </p>
     * <p>
     *     The handle passed to {@link #streamClose(Stream)} must previously have been
     *     opened by a call to {@link #streamOpen(String, Session)}.
     * </p>
     *
     * @param stream         stream handle
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM}: Stream object was invalid.</li>
     *              <li>{@link NabtoStatus#DATA_PENDING}: Unacknowledged data is pending.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred closing
     *              the stream.</li>
     *          </ul>
     */
    public NabtoStatus streamClose(Stream stream) {
        NabtoStatus status = NabtoCApiWrapper.nabtoStreamClose(stream);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to close stream: " + status);
        }
        return status;
    }

    /**
     * Reads some data from an open stream.
     * <p>
     *     This call blocks based on the stream option set by
     *     {@link #streamSetOption(Stream, NabtoStreamOption, int)}:
     * </p>
     * <ul>
     *     <li>{@link NabtoStreamOption#RECEIVE_TIMEOUT} = -1: blocking (default)</li>
     *     <li>{@link NabtoStreamOption#RECEIVE_TIMEOUT} = 0: non-blocking</li>
     *     <li>{@link NabtoStreamOption#RECEIVE_TIMEOUT} = n: blocks for up to n milliseconds.</li>
     * </ul>
     * <p>
     *     The stream handle given must have been obtained by a call to
     *     {@link #streamOpen(String, Session)}.
     * </p>
     * <p>
     *     On successful return ({@link StreamReadResult#getStatus()} returns
     *     {@link NabtoStatus#OK}), ({@link StreamReadResult#getData()}} returns the received data.
     *     Otherwise, the received data array is undefined.
     * </p>
     * @param stream         stream handle
     * @return  A {@link StreamReadResult} object. If the function succeeds, the return value of
     *          {@link StreamReadResult#getStatus()} is {@link NabtoStatus#OK} and
     *          {@link StreamReadResult#getData()} returns the received data. If the function fails,
     *          the return value of {@link Stream#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM}: Stream handle was invalid.</li>
     *              <li>{@link NabtoStatus#STREAM_CLOSED}: The stream was closed gracefully.</li>
     *              <li>{@link NabtoStatus#ABORTED}: The operation was aborted.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred reading the
     *              stream.</li>
     *          </ul>
     */
    public StreamReadResult streamRead(Stream stream) {
        StreamReadResult result = NabtoCApiWrapper.nabtoStreamRead(stream);
        if(result.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to read from stream: " + result.getStatus());
        }
        return result;
    }

    /**
     * Writes given data to a stream.
     * <p>
     *     This call blocks based on the stream option set by
     *     {@link #streamSetOption(Stream, NabtoStreamOption, int)}:
     * </p>
     * <ul>
     *     <li>{@link NabtoStreamOption#SEND_TIMEOUT} = -1: blocking until all data is sent or
     *     queued (default)</li>
     *     <li>{@link NabtoStreamOption#SEND_TIMEOUT} = 0: non-blocking</li>
     *     <li>{@link NabtoStreamOption#SEND_TIMEOUT} = n: blocks for up to n milliseconds.</li>
     * </ul>
     * <p>
     *     The stream handle given must have been obtained by a call to
     *     {@link #streamOpen(String, Session)}.
     * </p>
     * <p>
     *     If {@link NabtoStatus#BUFFER_FULL} is returned you must retransmit the data, no data
     *     has been queued. If {@link NabtoStatus#OK} is returned all the data is queued in
     *     the transmission queue.
     * </p>
     *
     * @param stream         stream handle
     * @param data           data to write
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM}: Stream handle was invalid.</li>
     *              <li>{@link NabtoStatus#BUFFER_FULL}: There's not room for the bytes in the send
     *              queue.</li>
     *              <li>{@link NabtoStatus#STREAM_CLOSED}: The stream was closed gracefully.</li>
     *              <li>{@link NabtoStatus#ABORTED}: The operation was aborted.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred writing
     *              the stream.</li>
     *          </ul>
     */
    public NabtoStatus streamWrite(Stream stream, byte[] data) {
        NabtoStatus status = NabtoCApiWrapper.nabtoStreamWrite(data, stream);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(), "Failed to write to stream: " + status);
        }
        return status;
    }

    /**
     * Retrieve the Nabto connection type of the underlying connection
     * for the given stream.
     *
     * @param stream         stream handle
     * @return  The connection type. If the function fails due to an unintialized Nabto client (see
     *          {@link #startup()}), an invalid stream handle, or any unspecified error,
     *          {@link NabtoConnectionType#UNKNOWN} is returned.
     */
    public NabtoConnectionType streamConnectionType(Stream stream) {
        return NabtoCApiWrapper.nabtoStreamConnectionType(stream);
    }

    /**
     * Set stream options (see {@link #streamRead(Stream)} and
     * {@link #streamWrite(Stream, byte[])} for possible options).
     * <p>
     *     The stream handle given must have been obtained by a call to
     *     {@link #streamOpen(String, Session)}.
     * </p>
     *
     * @param stream   Stream handle.
     * @param option   Option name to set.
     * @param value    Option value.
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM}: Stream object was invalid.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM_OPTION}: Option is invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred.</li>
     *          </ul>
     */
    public NabtoStatus streamSetOption(Stream stream, NabtoStreamOption option, int value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.putInt(value);
        NabtoStatus status = NabtoCApiWrapper
                .nabtoStreamSetOption(option.toInteger(), byteBuffer.array(), stream);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),  "Failed to set stream option: " + status);
        }
        return status;
    }

    /**
     * Opens a TCP tunnel to a remote server through a Nabto enabled device.
     * <p>
     *     In case the TCP tunnel should connect to the Nabto enabled
     *     device itself - that is the device is the server - specify "localhost"
     *     in the remoteHost parameter.
     * </p>
     * <p>
     *     This function returns a tunnel handle that must be used in
     *     subsequent client API invocations.
     * </p>
     * <p>
     *     The caller must call {@link #tunnelClose(Tunnel)} when the tunnel
     *     is no longer needed.
     * </p>
     * <p>
     *     If zero is chosen for localPort, the client chooses a suitable port.
     *     After successful connection use {@link #tunnelInfo(Tunnel)}} to get
     *     the listening port.
     * </p>
     * <p>
     *     An open session handle must have been created prior to calling this function.
     * </p>
     * <p>
     *     After successful return one is now able to connect to the
     *     local TCP port and communicate with the remote server.
     * </p>
     * <pre>{@code
     *      +--------+           +--------+               +--------+
     *      | nabto  |   nabto   | nabto  |   tcp/ip      | remote |
     *   |--+ client +----~~~----+ device +----~~~-----|--+ server |
     * port | API    |           | "host" |          port |        |
     *      +--------+           +--------+               +--------+
     * }</pre>
     *
     * @param localPort   The local TCP port to listen on (0 = PORT_ANY)
     * @param nabtoHost   The remote Nabto host to connect to.
     * @param remoteHost  The host the remote endpoint establishes a TCP
     *                    connection to.
     * @param remotePort  The TCP port to connect to on remoteHost.
     * @param session     The session handle returned by a previous call to
     *                    the  {@link #openSession(String, String)} or
     *                    {@link #openSessionBare()} function.
     * @return  A {@link Tunnel} object. If the function succeeds, the return value of
     *          {@link Tunnel#getStatus()} is {@link NabtoStatus#OK}. If the function fails,
     *          the return value of {@link Tunnel#getStatus()} is one of the following
     *          values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_SESSION}: Session handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred creating the
     *              tunnel.</li>
     *          </ul>
     */
    public Tunnel tunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Session session) {
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(localPort, nabtoHost, remoteHost,
                remotePort, session);
        if(tunnel.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to open TCP tunnel: " + tunnel.getStatus());
        }
        return tunnel;
    }

    /**
     * Closes an open tunnel.
     * <p>
     *     The tunnel handle given must have been obtained by a call to
     *     {@link #tunnelOpenTcp(int, String, String, int, Session)}.
     * </p>
     *
     * @param tunnel         tunnel handle
     * @return  If the function succeeds, the return value is {@link NabtoStatus#OK}.
     *          If the function fails, the return value is one of the
     *          following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_TUNNEL}: Tunnel handle was invalid.</li>
     *              <li>{@link NabtoStatus#INVALID_STREAM_OPTION}: Option is invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred closing the
     *              tunnel.</li>
     *          </ul>
     */
    public NabtoStatus tunnelClose(Tunnel tunnel) {
        NabtoStatus status = NabtoCApiWrapper.nabtoTunnelClose(tunnel);
        if(status != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),  "Failed to close tunnel: " + status);
        }
        return status;
    }

    /**
     * Retrieves information on an (open) tunnel.
     * <p>
     *     The tunnel handle given must have been obtained by a call to
     *     {@link #tunnelOpenTcp(int, String, String, int, Session)}.
     * </p>
     * <p>
     *     On success, ({@link TunnelInfoResult#getStatus()} returns {@link NabtoStatus#OK}),
     *     {@link TunnelInfoResult#getTunnelState()} returns the tunnel state and
     *     {@link TunnelInfoResult#getPort()}} returns the listening port. If the function fails
     *     both output parameters are undefined.
     * </p>
     *
     * @param tunnel         tunnel handle
     * @return  A {@link TunnelInfoResult} object. If the function succeeds, the return value of
     *          {@link Tunnel#getStatus()} is {@link NabtoStatus#OK} and it contains valid result
     *          parameters. If the function fails, the return value of
     *          {@link TunnelInfoResult#getStatus()} is one of the following values.
     *          <ul>
     *              <li>{@link NabtoStatus#API_NOT_INITIALIZED}: The {@link #startup()}
     *              function is the first function to call to initialize the Nabto client.</li>
     *              <li>{@link NabtoStatus#INVALID_TUNNEL}: Tunnel handle was invalid.</li>
     *              <li>{@link NabtoStatus#FAILED}: An unspecified error occurred retrieving the
     *              tunnel info.</li>
     *          </ul>
     */
    public TunnelInfoResult tunnelInfo(Tunnel tunnel) {
        TunnelInfoResult info = NabtoCApiWrapper.nabtoTunnelInfo(tunnel);
        if(info.getStatus() != NabtoStatus.OK) {
            Log.d(this.getClass().getSimpleName(),
                    "Failed to get tunnel info: " + info.getStatus());
        }
        return info;
    }
}
