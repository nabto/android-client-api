package com.nabto.api;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, NabtoCApiWrapper.class})
@SuppressStaticInitializationFor("com.nabto.api.NabtoCApiWrapper")
public class NabtoApiTest {
    final String DUMMY_NABTO_HOME_DIRECTORY = "nabto home directory";
    final String DUMMY_NABTO_RESOURCE_DIRECTORY = "nabto resource directory";

    private NabtoApi api;

    @Before
    public void setUp() {
        NabtoAssetManager assetManager = mock(NabtoAssetManager.class);
        when(assetManager.getNabtoHomeDirectory()).thenReturn(DUMMY_NABTO_HOME_DIRECTORY);
        when(assetManager.getNabtoResourceDirectory()).thenReturn(DUMMY_NABTO_RESOURCE_DIRECTORY);
        mockStatic(Log.class);
        mockStatic(NabtoCApiWrapper.class);
        api = new NabtoApi(assetManager);
    }

    @Test
    public void versionTest() {
        when(NabtoCApiWrapper.nabtoVersion())
                .thenReturn("version string");

        String version = api.version();

        assertEquals("version string", version);
    }

    @Test
    public void startupSuccessTest() {
        when(NabtoCApiWrapper.nabtoStartup(anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.startup();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStartup(eq(DUMMY_NABTO_HOME_DIRECTORY));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void startupFailedTest() {
        when(NabtoCApiWrapper.nabtoStartup(anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.startup();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStartup(eq(DUMMY_NABTO_HOME_DIRECTORY));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void shutdownSuccessTest() {
        when(NabtoCApiWrapper.nabtoShutdown())
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.shutdown();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoShutdown();

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void shutdownFailedTest() {
        when(NabtoCApiWrapper.nabtoShutdown())
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.shutdown();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoShutdown();

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void setStaticResourceDirSuccessTest() {
        when(NabtoCApiWrapper.nabtoSetStaticResourceDir(anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.setStaticResourceDir();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSetStaticResourceDir(eq(DUMMY_NABTO_RESOURCE_DIRECTORY));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void setStaticResourceDirFailedTest() {
        when(NabtoCApiWrapper.nabtoSetStaticResourceDir(anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.setStaticResourceDir();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSetStaticResourceDir(eq(DUMMY_NABTO_RESOURCE_DIRECTORY));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void getProtocolPrefixesSuccessTest() {
        String[] prefixesArray = new String[]{"a", "b"};
        when(NabtoCApiWrapper.nabtoGetProtocolPrefixes())
                .thenReturn(prefixesArray);

        Collection<String> prefixes = api.getProtocolPrefixes();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(Arrays.asList(prefixesArray), prefixes);
    }

    @Test
    public void getProtocolPrefixesFailedTest() {
        when(NabtoCApiWrapper.nabtoGetProtocolPrefixes())
                .thenReturn(null);

        Collection<String> prefixes = api.getProtocolPrefixes();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(new ArrayList<String>(), prefixes);
    }

    @Test
    public void getLocalDevicesSuccessTest() {
        String[] localDevicesArray = new String[]{"a", "b"};
        when(NabtoCApiWrapper.nabtoGetLocalDevices())
                .thenReturn(localDevicesArray);

        Collection<String> devices = api.getLocalDevices();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetLocalDevices();

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(Arrays.asList(localDevicesArray), devices);
    }

    @Test
    public void getLocalDevicesFailedTest() {
        when(NabtoCApiWrapper.nabtoGetLocalDevices())
                .thenReturn(null);

        Collection<String> devices = api.getLocalDevices();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetLocalDevices();

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(new ArrayList<String>(), devices);
    }

    @Test
    public void probeNetworkSuccessTest() {
        when(NabtoCApiWrapper.nabtoProbeNetwork(anyInt(), anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.probeNetwork(42, "hostname");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoProbeNetwork(eq(42), eq("hostname"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void probeNetworkFailedTest() {
        when(NabtoCApiWrapper.nabtoProbeNetwork(anyInt(), anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.probeNetwork(42, "hostname");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoProbeNetwork(eq(42), eq("hostname"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void getCertificatesSuccessTest() {
        String[] certificatesArray = new String[]{"a", "b"};
        when(NabtoCApiWrapper.nabtoGetCertificates())
                .thenReturn(certificatesArray);

        Collection<String> certificates = api.getCertificates();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetCertificates();

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(Arrays.asList(certificatesArray), certificates);
    }

    @Test
    public void getCertificatesFailedTest() {
        when(NabtoCApiWrapper.nabtoGetCertificates()).thenReturn(null);

        Collection<String> certificates = api.getCertificates();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetCertificates();

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(new ArrayList<String>(), certificates);
    }

    @Test
    public void createProfileSuccessTest() {
        when(NabtoCApiWrapper.nabtoCreateProfile(anyString(), anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.createProfile("email", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCreateProfile(eq("email"), eq("password"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void createProfileFailedTest() {
        when(NabtoCApiWrapper.nabtoCreateProfile(anyString(), anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.createProfile("email", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCreateProfile(eq("email"), eq("password"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void createSelfSignedProfileSuccessTest() {
        when(NabtoCApiWrapper.nabtoCreateSelfSignedProfile(anyString(), anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.createSelfSignedProfile("commonName", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCreateSelfSignedProfile(eq("commonName"), eq("password"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void createSelfSignedProfileFailedTest() {
        when(NabtoCApiWrapper.nabtoCreateSelfSignedProfile(anyString(), anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.createSelfSignedProfile("commonName", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCreateSelfSignedProfile(eq("commonName"), eq("password"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void signupSuccessTest() {
        when(NabtoCApiWrapper.nabtoSignup(anyString(), anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.signup("email", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSignup(eq("email"), eq("password"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void signupFailedTest() {
        when(NabtoCApiWrapper.nabtoSignup(anyString(), anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.signup("email", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSignup(eq("email"), eq("password"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void resetAccountPasswordSuccessTest() {
        when(NabtoCApiWrapper.nabtoResetAccountPassword(anyString()))
                .thenReturn(NabtoStatus.OK.toInteger());

        NabtoStatus status = api.resetAccountPassword("email");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoResetAccountPassword(eq("email"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void resetAccountPasswordFailedTest() {
        when(NabtoCApiWrapper.nabtoResetAccountPassword(anyString()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        NabtoStatus status = api.resetAccountPassword("email");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoResetAccountPassword(eq("email"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void openSessionSuccessTest() {
        Session wrapperSession = new Session("handle", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoOpenSession(anyString(), anyString()))
                .thenReturn(wrapperSession);

        Session session = api.openSession("id", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoOpenSession(eq("id"), eq("password"));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperSession, session);
    }

    @Test
    public void openSessionFailedTest() {
        Session wrapperSession = new Session(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoOpenSession(anyString(), anyString()))
                .thenReturn(wrapperSession);

        Session session = api.openSession("id", "password");

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoOpenSession(eq("id"), eq("password"));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperSession, session);
    }

    @Test
    public void openSessionBareSuccessTest() {
        Session wrapperSession = new Session("handle", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoOpenSessionBare())
                .thenReturn(wrapperSession);

        Session session = api.openSessionBare();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoOpenSessionBare();

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperSession, session);
    }

    @Test
    public void openSessionBareFailedTest() {
        Session wrapperSession = new Session(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoOpenSessionBare())
                .thenReturn(wrapperSession);

        Session session = api.openSessionBare();

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoOpenSessionBare();

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperSession, session);
    }

    @Test
    public void closeSessionSuccessTest() {
        when(NabtoCApiWrapper.nabtoCloseSession(anyObject()))
                .thenReturn(NabtoStatus.OK.toInteger());

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.closeSession(session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCloseSession(eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void closeSessionFailedTest() {
        when(NabtoCApiWrapper.nabtoCloseSession(anyObject()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.closeSession(session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoCloseSession(eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void closeSessionFailedInvalidSessionTest() {
        Session session = null;
        NabtoStatus status = api.closeSession(session);
        assertEquals(NabtoStatus.INVALID_SESSION, status);

        session = new Session(null, NabtoStatus.OK.toInteger());
        status = api.closeSession(session);
        assertEquals(NabtoStatus.INVALID_SESSION, status);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoCloseSession(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void rpcSetDefaultInterfaceSuccessTest() {
        RpcResult wrapperRpcResult = new RpcResult("json", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoRpcSetDefaultInterface(anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcSetDefaultInterface("interface", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcSetDefaultInterface(eq("interface"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcSetDefaultInterfaceFailedTest() {
        RpcResult wrapperRpcResult = new RpcResult(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoRpcSetDefaultInterface(anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcSetDefaultInterface("interface", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcSetDefaultInterface(eq("interface"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcSetDefaultInterfaceFailedInvalidSessionTest() {
        Session session = null;
        RpcResult rpcResult = api.rpcSetDefaultInterface("interface", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        session = new Session(null, NabtoStatus.OK.toInteger());
        rpcResult = api.rpcSetDefaultInterface("interface", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoRpcSetDefaultInterface(anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void rpcSetInterfaceSuccessTest() {
        RpcResult wrapperRpcResult = new RpcResult("json", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoRpcSetInterface(anyString(), anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcSetInterface("host", "interface", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcSetInterface(eq("host"), eq("interface"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcSetInterfaceFailedTest() {
        RpcResult wrapperRpcResult = new RpcResult(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoRpcSetInterface(anyString(), anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcSetInterface("host", "interface", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcSetInterface(eq("host"), eq("interface"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcSetInterfaceFailedInvalidSessionTest() {
        Session session = null;
        RpcResult rpcResult = api.rpcSetInterface("host", "interface", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        session = new Session(null, NabtoStatus.OK.toInteger());
        rpcResult = api.rpcSetInterface("host", "interface", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoRpcSetInterface(anyString(), anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void rpcInvokeSuccessTest() {
        RpcResult wrapperRpcResult = new RpcResult("json", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoRpcInvoke(anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcInvoke("nabtoUrl", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcInvoke(eq("nabtoUrl"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcInvokeFailedTest() {
        RpcResult wrapperRpcResult = new RpcResult(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoRpcInvoke(anyString(), anyObject()))
                .thenReturn(wrapperRpcResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        RpcResult rpcResult = api.rpcInvoke("nabtoUrl", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoRpcInvoke(eq("nabtoUrl"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperRpcResult, rpcResult);
    }

    @Test
    public void rpcInvokeFailedInvalidSessionTest() {
        Session session = null;
        RpcResult rpcResult = api.rpcInvoke("nabtoUrl", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        session = new Session(null, NabtoStatus.OK.toInteger());
        rpcResult = api.rpcInvoke("nabtoUrl", session);
        assertEquals(NabtoStatus.INVALID_SESSION, rpcResult.getStatus());
        assertEquals(null, rpcResult.getJson());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoRpcInvoke(anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void fetchUrlSuccessTest() {
        UrlResult wrapperUrlResult = new UrlResult(new byte[]{1,2}, "mime", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoFetchUrl(anyString(), anyObject()))
                .thenReturn(wrapperUrlResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        UrlResult urlResult = api.fetchUrl("nabtoUrl", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoFetchUrl(eq("nabtoUrl"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperUrlResult, urlResult);
    }

    @Test
    public void fetchUrlFailedTest() {
        UrlResult wrapperUrlResult = new UrlResult(null, null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoFetchUrl(anyString(), anyObject()))
                .thenReturn(wrapperUrlResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        UrlResult urlResult = api.fetchUrl("nabtoUrl", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoFetchUrl(eq("nabtoUrl"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperUrlResult, urlResult);
    }

    @Test
    public void fetchUrlFailedInvalidSessionTest() {
        Session session = null;
        UrlResult urlResult = api.fetchUrl("nabtoUrl", session);
        assertEquals(NabtoStatus.INVALID_SESSION, urlResult.getStatus());
        assertEquals(null, urlResult.getMimeType());
        assertEquals(null, urlResult.getResult());

        session = new Session(null, NabtoStatus.OK.toInteger());
        urlResult = api.fetchUrl("nabtoUrl", session);
        assertEquals(NabtoStatus.INVALID_SESSION, urlResult.getStatus());
        assertEquals(null, urlResult.getMimeType());
        assertEquals(null, urlResult.getResult());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoFetchUrl(anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void submitPostDataSuccessTest() {
        UrlResult wrapperUrlResult = new UrlResult(new byte[]{1,2}, "mime", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoSubmitPostData(anyString(), any(byte[].class), anyString(), anyObject()))
                .thenReturn(wrapperUrlResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        UrlResult urlResult = api.submitPostData("nabtoUrl", new byte[]{1,2}, "post mime", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSubmitPostData(
                eq("nabtoUrl"), eq(new byte[]{1,2}), eq("post mime"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperUrlResult, urlResult);
    }

    @Test
    public void submitPostDataFailedTest() {
        UrlResult wrapperUrlResult = new UrlResult(null, null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoSubmitPostData(anyString(), any(byte[].class), anyString(), anyObject()))
                .thenReturn(wrapperUrlResult);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        UrlResult urlResult = api.submitPostData("nabtoUrl", new byte[]{1,2}, "post mime", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoSubmitPostData(
                eq("nabtoUrl"), eq(new byte[]{1,2}), eq("post mime"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperUrlResult, urlResult);
    }

    @Test
    public void submitPostDataFailedInvalidSessionTest() {
        Session session = null;
        UrlResult urlResult = api.submitPostData("nabtoUrl", new byte[]{1,2}, "post mime", session);
        assertEquals(NabtoStatus.INVALID_SESSION, urlResult.getStatus());
        assertEquals(null, urlResult.getMimeType());
        assertEquals(null, urlResult.getResult());

        session = new Session(null, NabtoStatus.OK.toInteger());
        urlResult = api.submitPostData("nabtoUrl", new byte[]{1,2}, "post mime", session);
        assertEquals(NabtoStatus.INVALID_SESSION, urlResult.getStatus());
        assertEquals(null, urlResult.getMimeType());
        assertEquals(null, urlResult.getResult());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoSubmitPostData(anyString(), any(byte[].class), anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void getSessionTokenSuccessTest() {
        when(NabtoCApiWrapper.nabtoGetSessionToken(anyObject()))
                .thenReturn("token");

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        String token = api.getSessionToken(session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetSessionToken(session.getSession());

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals("token", token);
    }

    @Test
    public void getSessionTokenFailedTest() {
        when(NabtoCApiWrapper.nabtoGetSessionToken(anyObject()))
                .thenReturn(null);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        String token = api.getSessionToken(session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoGetSessionToken(session.getSession());

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals("", token);
    }

    @Test
    public void getSessionTokenFailedInvalidSessionTest() {
        Session session = null;
        String token = api.getSessionToken(session);
        assertEquals("", token);

        session = new Session(null, NabtoStatus.OK.toInteger());
        token = api.getSessionToken(session);
        assertEquals("", token);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoGetSessionToken(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamOpenSuccessTest() {
        Stream wrapperStream = new Stream("handle", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoStreamOpen(anyString(), anyObject()))
                .thenReturn(wrapperStream);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        Stream stream = api.streamOpen("nabtoHost", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamOpen(eq("nabtoHost"), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperStream, stream);
    }

    @Test
    public void streamOpenFailedTest() {
        Stream wrapperStream = new Stream("handle", NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoStreamOpen(anyString(), anyObject()))
                .thenReturn(wrapperStream);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        Stream stream = api.streamOpen("nabtoHost", session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamOpen(eq("nabtoHost"), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperStream, stream);
    }

    @Test
    public void streamOpenFailedInvalidSessionTest() {
        Session session = null;
        Stream stream = api.streamOpen("nabtoHost", session);
        assertEquals(NabtoStatus.INVALID_SESSION, stream.getStatus());
        assertEquals(null, stream.getStream());

        session = new Session(null, NabtoStatus.OK.toInteger());
        stream = api.streamOpen("nabtoHost", session);
        assertEquals(NabtoStatus.INVALID_SESSION, stream.getStatus());
        assertEquals(null, stream.getStream());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamOpen(anyString(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamCloseSuccessTest() {
        when(NabtoCApiWrapper.nabtoStreamClose(anyObject()))
                .thenReturn(NabtoStatus.OK.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamClose(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamClose(eq(stream.getStream()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void streamCloseFailedTest() {
        when(NabtoCApiWrapper.nabtoStreamClose(anyObject()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamClose(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamClose(eq(stream.getStream()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void streamCloseFailedInvalidStreamTest() {
        Stream stream = null;
        NabtoStatus status = api.streamClose(stream);
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        stream = new Stream(null, NabtoStatus.OK.toInteger());
        status = api.streamClose(stream);
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamClose(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamReadSuccessTest() {
        StreamReadResult wrapperReadResult = new StreamReadResult(new byte[]{1,2}, NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoStreamRead(anyObject()))
                .thenReturn(wrapperReadResult);

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        StreamReadResult readResult = api.streamRead(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamRead(eq(stream.getStream()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperReadResult, readResult);
    }

    @Test
    public void streamReadFailedTest() {
        StreamReadResult wrapperReadResult = new StreamReadResult(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoStreamRead(anyObject()))
                .thenReturn(wrapperReadResult);

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        StreamReadResult readResult = api.streamRead(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamRead(eq(stream.getStream()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperReadResult, readResult);
    }

    @Test
    public void streamReadFailedInvalidStreamTest() {
        Stream stream = null;
        StreamReadResult readResult = api.streamRead(stream);
        assertEquals(NabtoStatus.INVALID_STREAM, readResult.getStatus());
        assertEquals(null, readResult.getData());

        stream = new Stream(null, NabtoStatus.OK.toInteger());
        readResult = api.streamRead(stream);
        assertEquals(NabtoStatus.INVALID_STREAM, readResult.getStatus());
        assertEquals(null, readResult.getData());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamRead(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamWriteSuccessTest() {
        when(NabtoCApiWrapper.nabtoStreamWrite(any(byte[].class), anyObject()))
                .thenReturn(NabtoStatus.OK.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamWrite(stream, new byte[]{1,2});

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamWrite(eq(new byte[]{1,2}), eq(stream.getStream()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void streamWriteFailedTest() {
        when(NabtoCApiWrapper.nabtoStreamWrite(any(byte[].class), anyObject()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamWrite(stream, new byte[]{1,2});

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamWrite(eq(new byte[]{1,2}), eq(stream.getStream()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void streamWriteFailedInvalidStreamTest() {
        Stream stream = null;
        NabtoStatus status = api.streamWrite(stream, new byte[]{1,2});
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        stream = new Stream(null, NabtoStatus.OK.toInteger());
        status = api.streamWrite(stream, new byte[]{1,2});
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamWrite(any(byte[].class), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamConnectionTypeSuccessTest() {
        when(NabtoCApiWrapper.nabtoStreamConnectionType(anyObject()))
                .thenReturn(NabtoConnectionType.RELAY_MICRO.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoConnectionType type = api.streamConnectionType(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamConnectionType(eq(stream.getStream()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoConnectionType.RELAY_MICRO, type);
    }

    @Test
    public void streamConnectionTypeFailedTest() {
        when(NabtoCApiWrapper.nabtoStreamConnectionType(anyObject()))
                .thenReturn(-1);

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoConnectionType type = api.streamConnectionType(stream);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamConnectionType(eq(stream.getStream()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoConnectionType.UNKNOWN, type);
    }

    @Test
    public void streamConnectionTypeFailedInvalidStreamTest() {
        Stream stream = null;
        NabtoConnectionType type = api.streamConnectionType(stream);
        assertEquals(NabtoConnectionType.UNKNOWN, type);

        stream = new Stream(null, NabtoStatus.OK.toInteger());
        type = api.streamConnectionType(stream);
        assertEquals(NabtoConnectionType.UNKNOWN, type);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamConnectionType(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void streamSetOptionSuccessTest() {
        when(NabtoCApiWrapper.nabtoStreamSetOption(anyInt(), any(byte[].class), anyObject()))
                .thenReturn(NabtoStatus.OK.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamSetOption(stream, NabtoStreamOption.SEND_TIMEOUT, 42);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamSetOption(eq(
                NabtoStreamOption.SEND_TIMEOUT.toInteger()), eq(new byte[]{0,0,0,42}), eq(stream.getStream()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void streamSetOptionFailedTest() {
        when(NabtoCApiWrapper.nabtoStreamSetOption(anyInt(), any(byte[].class), anyObject()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        Stream stream = new Stream("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.streamSetOption(stream, NabtoStreamOption.SEND_TIMEOUT, 42);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoStreamSetOption(
                eq(NabtoStreamOption.SEND_TIMEOUT.toInteger()), eq(new byte[]{0,0,0,42}), eq(stream.getStream()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void streamSetOptionFailedInvalidStreamTest() {
        Stream stream = null;
        NabtoStatus status = api.streamSetOption(stream, NabtoStreamOption.SEND_TIMEOUT, 42);
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        stream = new Stream(null, NabtoStatus.OK.toInteger());
        status = api.streamSetOption(stream, NabtoStreamOption.SEND_TIMEOUT, 42);
        assertEquals(NabtoStatus.INVALID_STREAM, status);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoStreamSetOption(anyInt(), any(byte[].class), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void tunnelOpenTcpSuccessTest() {
        Tunnel wrapperTunnel = new Tunnel("handle", NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoTunnelOpenTcp(anyInt(), anyString(), anyString(), anyInt(), anyObject()))
                .thenReturn(wrapperTunnel);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        Tunnel tunnel = api.tunnelOpenTcp(42, "nabtoHost", "remoteHost", 24, session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelOpenTcp(
                eq(42), eq("nabtoHost"), eq("remoteHost"), eq(24), eq(session.getSession()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperTunnel, tunnel);
    }

    @Test
    public void tunnelOpenTcpFailedTest() {
        Tunnel wrapperTunnel = new Tunnel(null, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoTunnelOpenTcp(anyInt(), anyString(), anyString(), anyInt(), anyObject()))
                .thenReturn(wrapperTunnel);

        Session session = new Session("handle", NabtoStatus.OK.toInteger());
        Tunnel tunnel = api.tunnelOpenTcp(42, "nabtoHost", "remoteHost", 24, session);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelOpenTcp(
                eq(42), eq("nabtoHost"), eq("remoteHost"), eq(24), eq(session.getSession()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperTunnel, tunnel);
    }

    @Test
    public void tunnelOpenTcpFailedInvalidSessionTest() {
        Session session = null;
        Tunnel tunnel = api.tunnelOpenTcp(42, "nabtoHost", "remoteHost", 24, session);
        assertEquals(NabtoStatus.INVALID_SESSION, tunnel.getStatus());
        assertEquals(null, tunnel.getTunnel());

        session = new Session(null, NabtoStatus.OK.toInteger());
        tunnel = api.tunnelOpenTcp(42, "nabtoHost", "remoteHost", 24, session);
        assertEquals(NabtoStatus.INVALID_SESSION, tunnel.getStatus());
        assertEquals(null, tunnel.getTunnel());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoTunnelOpenTcp(anyInt(), anyString(), anyString(), anyInt(), anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void tunnelCloseSuccessTest() {
        when(NabtoCApiWrapper.nabtoTunnelClose(anyObject()))
                .thenReturn(NabtoStatus.OK.toInteger());

        Tunnel tunnel = new Tunnel("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.tunnelClose(tunnel);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelClose(eq(tunnel.getTunnel()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void tunnelCloseFailedTest() {
        when(NabtoCApiWrapper.nabtoTunnelClose(anyObject()))
                .thenReturn(NabtoStatus.FAILED.toInteger());

        Tunnel tunnel = new Tunnel("handle", NabtoStatus.OK.toInteger());
        NabtoStatus status = api.tunnelClose(tunnel);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelClose(eq(tunnel.getTunnel()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(NabtoStatus.FAILED, status);
    }

    @Test
    public void tunnelCloseFailedInvalidTunnelTest() {
        Tunnel tunnel = null;
        NabtoStatus status = api.tunnelClose(tunnel);
        assertEquals(NabtoStatus.INVALID_TUNNEL, status);

        tunnel = new Tunnel(null, NabtoStatus.OK.toInteger());
        status = api.tunnelClose(tunnel);
        assertEquals(NabtoStatus.INVALID_TUNNEL, status);

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoTunnelClose(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }

    @Test
    public void tunnelInfoSuccessTest() {
        TunnelInfoResult wrapperTunnelInfo =
                new TunnelInfoResult(1, NabtoTunnelState.REMOTE_P2P.toInteger(), 2, 3, NabtoStatus.OK.toInteger());
        when(NabtoCApiWrapper.nabtoTunnelInfo(anyObject()))
                .thenReturn(wrapperTunnelInfo);

        Tunnel tunnel = new Tunnel("handle", NabtoStatus.OK.toInteger());
        TunnelInfoResult tunnelInfo = api.tunnelInfo(tunnel);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelInfo(eq(tunnel.getTunnel()));

        PowerMockito.verifyStatic(times(0));
        Log.d(anyString(), anyString());

        assertEquals(wrapperTunnelInfo, tunnelInfo);
    }

    @Test
    public void tunnelInfoFailedTest() {
        TunnelInfoResult wrapperTunnelInfo =
                new TunnelInfoResult(0, NabtoTunnelState.UNKNOWN.toInteger(), 0, 0, NabtoStatus.FAILED.toInteger());
        when(NabtoCApiWrapper.nabtoTunnelInfo(anyObject()))
                .thenReturn(wrapperTunnelInfo);

        Tunnel tunnel = new Tunnel("handle", NabtoStatus.OK.toInteger());
        TunnelInfoResult tunnelInfo = api.tunnelInfo(tunnel);

        PowerMockito.verifyStatic();
        NabtoCApiWrapper.nabtoTunnelInfo(eq(tunnel.getTunnel()));

        PowerMockito.verifyStatic();
        Log.d(eq(api.getClass().getSimpleName()), anyString());

        assertEquals(wrapperTunnelInfo, tunnelInfo);
    }

    @Test
    public void tunnelInfoFailedInvalidTunnelTest() {
        Tunnel tunnel = null;
        TunnelInfoResult info = api.tunnelInfo(tunnel);
        assertEquals(NabtoStatus.INVALID_TUNNEL, info.getStatus());
        assertEquals(NabtoTunnelState.UNKNOWN, info.getTunnelState());
        assertEquals(0, info.getPort());
        assertEquals(0, info.getLastError());
        assertEquals(0, info.getVersion());

        tunnel = new Tunnel(null, NabtoStatus.OK.toInteger());
        info = api.tunnelInfo(tunnel);
        assertEquals(NabtoStatus.INVALID_TUNNEL, info.getStatus());
        assertEquals(NabtoTunnelState.UNKNOWN, info.getTunnelState());
        assertEquals(0, info.getPort());
        assertEquals(0, info.getLastError());
        assertEquals(0, info.getVersion());

        PowerMockito.verifyStatic(times(0));
        NabtoCApiWrapper.nabtoTunnelInfo(anyObject());

        PowerMockito.verifyStatic(times(2));
        Log.d(eq(api.getClass().getSimpleName()), anyString());
    }
}