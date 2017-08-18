package com.nabto.api;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/** 
 * This class tests the JNI bindings using a C library (src/test/jniLibs/nabto_client_api_stub.cpp)
 * that stubs the Nabto Client SDK.
 */
public class NabtoCApiWrapperTest {
    final String DUMMY_EMAIL = "dummy@nabto.com";
    final String DUMMY_PASSWORD = "dummy password";
    final String DUMMY_HOST = "dummy.nabto.com";

    @Test
    public void nabtoVersionTest() {
        String version = NabtoCApiWrapper.nabtoVersion();

        assertEquals("11.22", version);
    }

    @Test
    public void nabtoStartupTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoStartup("dir");

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("dir", paramVals.get("nabtoHomeDir"));

        // test ok default home dir
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        status = NabtoCApiWrapper.nabtoStartup(null);

        assertEquals(NabtoStatus.OK, status);

        paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("default", paramVals.get("nabtoHomeDir"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.ERROR_READING_CONFIG.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        status = NabtoCApiWrapper.nabtoStartup("dir");

        assertEquals(NabtoStatus.ERROR_READING_CONFIG, status);
    }

    @Test
    public void nabtoShutdownTest() {
        NabtoStatus status = NabtoCApiWrapper.nabtoShutdown();

        assertEquals(NabtoStatus.OK, status);
    }
    
    @Test
    public void nabtoSetOptionTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoSetOption("option name", "option value");

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("option name", paramVals.get("name"));
        assertEquals("option value", paramVals.get("value"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        status = NabtoCApiWrapper.nabtoSetOption("option name", "option value");

        assertEquals(NabtoStatus.FAILED, status);

        // test NULL resilience
        NabtoCApiWrapper.nabtoSetOption(null, null);
    }

    @Test
    public void nabtoSetStaticResourceDirTest() {
        NabtoStatus status = NabtoCApiWrapper.nabtoSetStaticResourceDir("dir");

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("dir", paramVals.get("resourceDir"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoSetStaticResourceDir(null);
    }

    @Test
    public void nabtoInstallDefaultStaticResourcesTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoInstallDefaultStaticResources("dir");

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("dir", paramVals.get("resourceDir"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoInstallDefaultStaticResources(null);
    }


    @Test
    public void nabtoGetProtocolPrefixesTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("prefixesLength", "3");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        String[] prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertEquals(3, prefixes.length);
        assertEquals("p0", prefixes[0]);
        assertEquals("p1", prefixes[1]);
        assertEquals("p2", prefixes[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("prefixesLength", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertEquals(0, prefixes.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("prefixesLength", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertNull(prefixes);
    }

    @Test
    public void nabtoGetLocalDevicesTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("numberOfDevices", "3");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        String[] devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertEquals(3, devices.length);
        assertEquals("d0", devices[0]);
        assertEquals("d1", devices[1]);
        assertEquals("d2", devices[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("numberOfDevices", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertEquals(0, devices.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("numberOfDevices", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertNull(devices);
    }

    @Test
    public void nabtoProbeNetworkTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoProbeNetwork(1, "host");

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("1", paramVals.get("timeoutMillis"));
        assertEquals("host", paramVals.get("host"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.API_NOT_INITIALIZED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        status = NabtoCApiWrapper.nabtoProbeNetwork(1, "host");

        assertEquals(NabtoStatus.API_NOT_INITIALIZED, status);

        // test NULL resilience
        NabtoCApiWrapper.nabtoProbeNetwork(-1, null);
    }

    @Test
    public void nabtoGetCertificatesTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("certificatesLength", "3");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        String[] certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertEquals(3, certificates.length);
        assertEquals("c0", certificates[0]);
        assertEquals("c1", certificates[1]);
        assertEquals("c2", certificates[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("certificatesLength", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertEquals(0, certificates.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("certificatesLength", "0");
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertNull(certificates);
    }

    @Test
    public void nabtoCreateProfileTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoCreateProfile(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCreateProfile(null, null);
    }
    
    @Test
    public void nabtoRemoveProfileTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoRemoveProfile(DUMMY_EMAIL);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("id"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoRemoveProfile(null);
    }


    @Test
    public void nabtoCreateSelfSignedProfileTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = 
                NabtoCApiWrapper.nabtoCreateSelfSignedProfile(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("commonName"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCreateSelfSignedProfile(null, null);
    }

    @Test
    public void nabtoSignupTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoSignup(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoSignup(null, null);
    }

    @Test
    public void nabtoResetAccountPasswordTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        NabtoStatus status = NabtoCApiWrapper.nabtoResetAccountPassword(DUMMY_EMAIL);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoResetAccountPassword(null);
    }

    @Test
    public void nabtoOpenSessionTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSession(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.OK, session.getStatus());
        assertThat(session.getHandle(), instanceOf(ByteBuffer.class));

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals(DUMMY_EMAIL, paramVals.get("id"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        session = NabtoCApiWrapper.nabtoOpenSession(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.FAILED, session.getStatus());
        assertNull(session.getHandle());

        // test NULL resilience
        NabtoCApiWrapper.nabtoOpenSession(null, null);
    }

    @Test
    public void nabtoOpenSessionBareTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();

        assertEquals(NabtoStatus.OK, session.getStatus());
        assertThat(session.getHandle(), instanceOf(ByteBuffer.class));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        session = NabtoCApiWrapper.nabtoOpenSessionBare();

        assertEquals(NabtoStatus.FAILED, session.getStatus());
        assertNull(session.getHandle());
    }

    @Test
    public void nabtoCloseSessionTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        NabtoStatus status = NabtoCApiWrapper.nabtoCloseSession(session);

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCloseSession(null);
    }

    @Test
    public void nabtoSetBasestationAuthJsonTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSession(DUMMY_EMAIL, DUMMY_PASSWORD);
        final String json = "some json stuff";
        NabtoStatus status = NabtoCApiWrapper.nabtoSetBasestationAuthJson(json, session);

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals(json, paramVals.get("jsonKeyValuePairs"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoSetBasestationAuthJson("", null);
    }

    @Test
    public void nabtoRpcSetDefaultInterfaceTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface(
                "interface", session);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertNull(result.getJson());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("interface", paramVals.get("interfaceDefinition"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface("interface", session);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getJson());

        // test error with json message
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED_WITH_JSON_MESSAGE.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface("interface", session);

        assertEquals(NabtoStatus.FAILED_WITH_JSON_MESSAGE, result.getStatus());
        assertEquals("error", result.getJson());

        // test NULL resilience
        NabtoCApiWrapper.nabtoRpcSetDefaultInterface(null, null);
    }

    @Test
    public void nabtoRpcSetInterfaceTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcSetInterface(
                DUMMY_HOST, "interface", session);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertNull(result.getJson());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("interface", paramVals.get("interfaceDefinition"));
        assertEquals(DUMMY_HOST, paramVals.get("host"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoRpcSetInterface("host", "interface", session);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getJson());

        // test error with json message
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED_WITH_JSON_MESSAGE.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoRpcSetInterface("host", "interface", session);

        assertEquals(NabtoStatus.FAILED_WITH_JSON_MESSAGE, result.getStatus());
        assertEquals("error", result.getJson());

        // test NULL resilience
        NabtoCApiWrapper.nabtoRpcSetInterface(null, null, null);
    }

    @Test
    public void nabtoRpcInvokeTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcInvoke("url", session);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("json", result.getJson());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoRpcInvoke("url", session);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertEquals("json", result.getJson());

        // test NULL resilience
        NabtoCApiWrapper.nabtoRpcInvoke(null, null);
    }

    @Test
    public void nabtoFetchUrlTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        UrlResult result = NabtoCApiWrapper.nabtoFetchUrl("url", session);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("content", new String(result.getResult()));
        assertEquals("mime type", result.getMimeType());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoFetchUrl("url", session);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getResult());
        assertNull(result.getMimeType());

        // test NULL resilience
        NabtoCApiWrapper.nabtoFetchUrl(null, null);
    }

    @Test
    public void nabtoSubmitPostDataTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        UrlResult result = NabtoCApiWrapper.nabtoSubmitPostData(
                "url", new String("post").getBytes(), "post mime type", session);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("content", new String(result.getResult()));
        assertEquals("mime type", result.getMimeType());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));
        assertEquals("post", paramVals.get("postBuffer"));
        assertEquals("4", paramVals.get("postLen"));
        assertEquals("post mime type", paramVals.get("postMimeType"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoSubmitPostData(
                "url", new String("post").getBytes(), "post mime type", session);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getResult());
        assertNull(result.getMimeType());

        // test NULL resilience
        NabtoCApiWrapper.nabtoSubmitPostData(null, null, null, null);
    }

    @Test
    public void nabtoGetSessionTokenTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        String token = NabtoCApiWrapper.nabtoGetSessionToken(session);

        assertEquals(64, token.length());
        assertEquals("xxx", token.substring(0, 3));

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("65", paramVals.get("bufLen"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoGetSessionToken(null);
    }

    @Test
    public void nabtoStreamOpenTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        assertEquals(NabtoStatus.OK, stream.getStatus());
        assertThat(stream.getHandle(), instanceOf(ByteBuffer.class));

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals(DUMMY_HOST, paramVals.get("serverId"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        assertEquals(NabtoStatus.FAILED, stream.getStatus());
        assertNull(stream.getHandle());

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamOpen(null, null);
    }

    @Test
    public void nabtoStreamCloseTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        NabtoStatus status = NabtoCApiWrapper.nabtoStreamClose(stream);

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("43", paramVals.get("streamHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamClose(null);
    }

    @Test
    public void nabtoStreamReadTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        StreamReadResult result = NabtoCApiWrapper.nabtoStreamRead(stream);

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("data", new String(result.getData()));

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("43", paramVals.get("streamHandle"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        result = NabtoCApiWrapper.nabtoStreamRead(stream);

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getData());

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamRead(null);
    }

    @Test
    public void nabtoStreamWriteTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        NabtoStatus status = NabtoCApiWrapper.nabtoStreamWrite(
                new String("data").getBytes(), stream);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("43", paramVals.get("streamHandle"));
        assertEquals("data", paramVals.get("buf"));
        assertEquals("4", paramVals.get("len"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamWrite(null, null);
    }

    @Test
    public void nabtoStreamConnectionTypeTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        NabtoConnectionType type = NabtoCApiWrapper.nabtoStreamConnectionType(stream);

        assertEquals(NabtoConnectionType.P2P, type);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("43", paramVals.get("streamHandle"));

        // test fail
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        type = NabtoCApiWrapper.nabtoStreamConnectionType(stream);

        assertEquals(NabtoConnectionType.UNKNOWN, type);

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamConnectionType(null);
    }

    @Test
    public void nabtoStreamSetOptionTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session);

        NabtoStatus status = NabtoCApiWrapper.nabtoStreamSetOption(
                NabtoStreamOption.RECEIVE_TIMEOUT.toInteger(),
                new String("option").getBytes(),
                stream);

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("43", paramVals.get("streamHandle"));
        assertEquals(Integer.toString(NabtoStreamOption.RECEIVE_TIMEOUT.toInteger()),
                paramVals.get("optionName"));
        assertEquals("option", paramVals.get("optionValue"));
        assertEquals("6", paramVals.get("optionLength"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamSetOption(-1, null, null);
    }

    @Test
    public void nabtoTunnelOpenTcpTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session);

        assertEquals(NabtoStatus.OK, tunnel.getStatus());
        assertThat(tunnel.getHandle(), instanceOf(ByteBuffer.class));

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("11", paramVals.get("localPort"));
        assertEquals("nabto host", paramVals.get("nabtoHost"));
        assertEquals("remote host", paramVals.get("remoteHost"));
        assertEquals("22", paramVals.get("remotePort"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session);

        assertEquals(NabtoStatus.FAILED, tunnel.getStatus());
        assertNull(tunnel.getHandle());

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelOpenTcp(-1, null, null, -1, null);
    }

    @Test
    public void nabtoTunnelCloseTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session);

        NabtoStatus status = NabtoCApiWrapper.nabtoTunnelClose(tunnel);

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("44", paramVals.get("tunnelHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelClose(null);
    }

    @Test
    public void nabtoTunnelInfoTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValueMap(retVals);

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session);

        TunnelInfoResult info = NabtoCApiWrapper.nabtoTunnelInfo(tunnel);

        assertEquals(NabtoStatus.OK, info.getStatus());
        assertEquals(123, info.getVersion());
        assertEquals(NabtoTunnelState.REMOTE_RELAY, info.getTunnelState());
        assertEquals(-123, info.getLastError());
        assertEquals(234, info.getPort());

        Map paramVals = NabtoCApiWrapperStubController.getParameterValueMap();
        assertEquals("44", paramVals.get("tunnelHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelInfo(null);
    }
}
