package com.nabto.api;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class NabtoCApiWrapperTest {
    final String DUMMY_EMAIL = "dummy@nabto.com";
    final String DUMMY_PASSWORD = "dummy password";
    final String DUMMY_HOST = "dummy.nabto.com";

    String mapToString(Map<String, String> values) {
        StringBuilder valuesString = new StringBuilder();
        String delim = "";
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            valuesString.append(delim).append(key).append("=").append(value);
            delim = ",";
        }
        return valuesString.toString();
    }

    Map<String, String> stringToMap(String valuesString) {
        Map values = new HashMap<String, String>();

        Pattern pattern = Pattern.compile("[^,]+\\=[^,]+");
        Matcher matcher = pattern.matcher(valuesString);

        while (matcher.find()) {
            String pair = matcher.group();
            String key = pair.split("=")[0];
            String value = pair.split("=")[1];
            values.put(key, value);
        }
        return values;
    }

    @Test
    public void mapToStringHelperTest() {
        Map values = new HashMap<String, String>();
        values.put("a", "1");
        values.put("ab", "12");
        values.put("ab0", "12a");

        String str = mapToString(values);

        assertEquals(17, str.length());
        assertThat(str, containsString("a=1"));
        assertThat(str, containsString("ab=12"));
        assertThat(str, containsString("ab0=12a"));
    }

    @Test
    public void stringToMapHelperTest() {
        String str = "a=1,ab=12,ab0=12a";

        Map values = stringToMap(str);

        assertTrue(values.containsKey("a"));
        assertEquals("1", values.get("a"));

        assertTrue(values.containsKey("ab"));
        assertEquals("12", values.get("ab"));

        assertTrue(values.containsKey("ab0"));
        assertEquals("12a", values.get("ab0"));
    }

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoStartup("dir"));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("dir", paramVals.get("nabtoHomeDir"));

        // test ok default home dir
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoStartup(null));

        assertEquals(NabtoStatus.OK, status);

        paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("default", paramVals.get("nabtoHomeDir"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.ERROR_READING_CONFIG.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoStartup("dir"));

        assertEquals(NabtoStatus.ERROR_READING_CONFIG, status);
    }

    @Test
    public void nabtoShutdownTest() {
        NabtoStatus status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoShutdown());

        assertEquals(NabtoStatus.OK, status);
    }

    @Test
    public void nabtoSetStaticResourceDirTest() {
        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoSetStaticResourceDir("dir"));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("dir", paramVals.get("resourceDir"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoSetStaticResourceDir(null);
    }

    @Test
    public void nabtoGetProtocolPrefixesTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("prefixesLength", "3");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        String[] prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertEquals(3, prefixes.length);
        assertEquals("p0", prefixes[0]);
        assertEquals("p1", prefixes[1]);
        assertEquals("p2", prefixes[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("prefixesLength", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertEquals(0, prefixes.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("prefixesLength", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        prefixes = NabtoCApiWrapper.nabtoGetProtocolPrefixes();

        assertNull(prefixes);
    }

    @Test
    public void nabtoGetLocalDevicesTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("numberOfDevices", "3");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        String[] devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertEquals(3, devices.length);
        assertEquals("d0", devices[0]);
        assertEquals("d1", devices[1]);
        assertEquals("d2", devices[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("numberOfDevices", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertEquals(0, devices.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("numberOfDevices", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        devices = NabtoCApiWrapper.nabtoGetLocalDevices();

        assertNull(devices);
    }

    @Test
    public void nabtoProbeNetworkTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoProbeNetwork(1, "host"));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("1", paramVals.get("timeoutMillis"));
        assertEquals("host", paramVals.get("host"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.API_NOT_INITIALIZED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        status = NabtoStatus.fromInteger(NabtoCApiWrapper.nabtoProbeNetwork(1, "host"));

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        String[] certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertEquals(3, certificates.length);
        assertEquals("c0", certificates[0]);
        assertEquals("c1", certificates[1]);
        assertEquals("c2", certificates[2]);

        // test ok empty
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        retVals.put("certificatesLength", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertEquals(0, certificates.length);

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        retVals.put("certificatesLength", "0");
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        certificates = NabtoCApiWrapper.nabtoGetCertificates();

        assertNull(certificates);
    }

    @Test
    public void nabtoCreateProfileTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoCreateProfile(DUMMY_EMAIL, DUMMY_PASSWORD));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCreateProfile(null, null);
    }

    @Test
    public void nabtoCreateSelfSignedProfileTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoCreateSelfSignedProfile(DUMMY_EMAIL, DUMMY_PASSWORD));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals(DUMMY_EMAIL, paramVals.get("commonName"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCreateSelfSignedProfile(null, null);
    }

    @Test
    public void nabtoSignupTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoSignup(DUMMY_EMAIL, DUMMY_PASSWORD));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoSignup(null, null);
    }

    @Test
    public void nabtoResetAccountPasswordTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoResetAccountPassword(DUMMY_EMAIL));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals(DUMMY_EMAIL, paramVals.get("email"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoResetAccountPassword(null);
    }

    @Test
    public void nabtoOpenSessionTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSession(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.OK, session.getStatus());
        assertThat(session.getSession(), instanceOf(ByteBuffer.class));

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals(DUMMY_EMAIL, paramVals.get("id"));
        assertEquals(DUMMY_PASSWORD, paramVals.get("password"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        session = NabtoCApiWrapper.nabtoOpenSession(DUMMY_EMAIL, DUMMY_PASSWORD);

        assertEquals(NabtoStatus.FAILED, session.getStatus());
        assertNull(session.getSession());

        // test NULL resilience
        NabtoCApiWrapper.nabtoOpenSession(null, null);
    }

    @Test
    public void nabtoOpenSessionBareTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();

        assertEquals(NabtoStatus.OK, session.getStatus());
        assertThat(session.getSession(), instanceOf(ByteBuffer.class));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        session = NabtoCApiWrapper.nabtoOpenSessionBare();

        assertEquals(NabtoStatus.FAILED, session.getStatus());
        assertNull(session.getSession());
    }

    @Test
    public void nabtoCloseSessionTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoCloseSession(session.getSession()));

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoCloseSession(null);
    }

    @Test
    public void nabtoRpcSetDefaultInterfaceTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface(
                "interface", session.getSession());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertNull(result.getJson());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("interface", paramVals.get("interfaceDefinition"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface("interface", session.getSession());

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getJson());

        // test error with json message
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED_WITH_JSON_MESSAGE.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoRpcSetDefaultInterface("interface", session.getSession());

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcSetInterface(
                DUMMY_HOST, "interface", session.getSession());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertNull(result.getJson());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("interface", paramVals.get("interfaceDefinition"));
        assertEquals(DUMMY_HOST, paramVals.get("host"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoRpcSetInterface("host", "interface", session.getSession());

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getJson());

        // test error with json message
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED_WITH_JSON_MESSAGE.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoRpcSetInterface("host", "interface", session.getSession());

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        RpcResult result = NabtoCApiWrapper.nabtoRpcInvoke("url", session.getSession());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("json", result.getJson());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoRpcInvoke("url", session.getSession());

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getJson());

        // test NULL resilience
        NabtoCApiWrapper.nabtoRpcInvoke(null, null);
    }

    @Test
    public void nabtoFetchUrlTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        UrlResult result = NabtoCApiWrapper.nabtoFetchUrl("url", session.getSession());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("content", new String(result.getResult()));
        assertEquals("mime type", result.getMimeType());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoFetchUrl("url", session.getSession());

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        UrlResult result = NabtoCApiWrapper.nabtoSubmitPostData(
                "url", new String("post").getBytes(), "post mime type", session.getSession());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("content", new String(result.getResult()));
        assertEquals("mime type", result.getMimeType());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("url", paramVals.get("nabtoUrl"));
        assertEquals("post", paramVals.get("postBuffer"));
        assertEquals("4", paramVals.get("postLen"));
        assertEquals("post mime type", paramVals.get("postMimeType"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoSubmitPostData(
                "url", new String("post").getBytes(), "post mime type", session.getSession());

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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        String token = NabtoCApiWrapper.nabtoGetSessionToken(session.getSession());

        assertEquals(64, token.length());
        assertEquals("xxx", token.substring(0, 3));

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        assertEquals(NabtoStatus.OK, stream.getStatus());
        assertThat(stream.getStream(), instanceOf(ByteBuffer.class));

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals(DUMMY_HOST, paramVals.get("serverId"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        assertEquals(NabtoStatus.FAILED, stream.getStatus());
        assertNull(stream.getStream());

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamOpen(null, null);
    }

    @Test
    public void nabtoStreamCloseTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoStreamClose(stream.getStream()));

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("43", paramVals.get("streamHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamClose(null);
    }

    @Test
    public void nabtoStreamReadTest() {
        // test ok
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        StreamReadResult result = NabtoCApiWrapper.nabtoStreamRead(stream.getStream());

        assertEquals(NabtoStatus.OK, result.getStatus());
        assertEquals("data", new String(result.getData()));

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("43", paramVals.get("streamHandle"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        result = NabtoCApiWrapper.nabtoStreamRead(stream.getStream());

        assertEquals(NabtoStatus.FAILED, result.getStatus());
        assertNull(result.getData());

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamRead(null);
    }

    @Test
    public void nabtoStreamWriteTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoStreamWrite(
                        new String("data").getBytes(), stream.getStream()));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        NabtoConnectionType type = NabtoConnectionType.fromInteger(
                NabtoCApiWrapper.nabtoStreamConnectionType(stream.getStream()));

        assertEquals(NabtoConnectionType.P2P, type);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("43", paramVals.get("streamHandle"));

        // test fail
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        type = NabtoConnectionType.fromInteger(
                NabtoCApiWrapper.nabtoStreamConnectionType(stream.getStream()));

        assertEquals(NabtoConnectionType.UNKNOWN, type);

        // test NULL resilience
        NabtoCApiWrapper.nabtoStreamConnectionType(null);
    }

    @Test
    public void nabtoStreamSetOptionTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Stream stream = NabtoCApiWrapper.nabtoStreamOpen(DUMMY_HOST, session.getSession());

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoStreamSetOption(
                        NabtoStreamOption.RECEIVE_TIMEOUT.toInteger(),
                        new String("option").getBytes(),
                        stream.getStream()));

        assertEquals(NabtoStatus.OK, status);

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
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
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session.getSession());

        assertEquals(NabtoStatus.OK, tunnel.getStatus());
        assertThat(tunnel.getTunnel(), instanceOf(ByteBuffer.class));

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("42", paramVals.get("sessionHandle"));
        assertEquals("11", paramVals.get("localPort"));
        assertEquals("nabto host", paramVals.get("nabtoHost"));
        assertEquals("remote host", paramVals.get("remoteHost"));
        assertEquals("22", paramVals.get("remotePort"));

        // test error
        retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.FAILED.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session.getSession());

        assertEquals(NabtoStatus.FAILED, tunnel.getStatus());
        assertNull(tunnel.getTunnel());

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelOpenTcp(-1, null, null, -1, null);
    }

    @Test
    public void nabtoTunnelCloseTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session.getSession());

        NabtoStatus status = NabtoStatus.fromInteger(
                NabtoCApiWrapper.nabtoTunnelClose(tunnel.getTunnel()));

        assertEquals(NabtoStatus.OK, status);
        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("44", paramVals.get("tunnelHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelClose(null);
    }

    @Test
    public void nabtoTunnelInfoTest() {
        Map retVals = new HashMap<String, String>();
        retVals.put("status", Integer.toString(NabtoStatus.OK.toInteger()));
        NabtoCApiWrapperStubController.setReturnValues(mapToString(retVals));

        Session session = NabtoCApiWrapper.nabtoOpenSessionBare();
        Tunnel tunnel = NabtoCApiWrapper.nabtoTunnelOpenTcp(
                11, "nabto host", "remote host", 22, session.getSession());

        TunnelInfoResult info = NabtoCApiWrapper.nabtoTunnelInfo(tunnel.getTunnel());

        assertEquals(NabtoStatus.OK, info.getStatus());
        assertEquals(123, info.getVersion());
        assertEquals(NabtoTunnelState.REMOTE_RELAY, info.getTunnelState());
        assertEquals(-123, info.getLastError());
        assertEquals(234, info.getPort());

        Map paramVals = stringToMap(NabtoCApiWrapperStubController.getParameterValues());
        assertEquals("44", paramVals.get("tunnelHandle"));

        // test NULL resilience
        NabtoCApiWrapper.nabtoTunnelInfo(null);
    }
}