package com.nabto.api;

class NabtoCApiWrapper {
    static {
        System.loadLibrary("nabto_client_api_jni");
    }

    // Configuration and initialization API
    static native String nabtoVersion();
    static native int nabtoStartup(String nabtoHomeDir);
    static native int nabtoShutdown();
    static native int nabtoSetStaticResourceDir(String nabtoResDir);
    static native String[] nabtoGetProtocolPrefixes();
    static native String[] nabtoGetLocalDevices();
    static native int nabtoProbeNetwork(int timeoutMillis, String hostname);

    // The portal API
    static native String[] nabtoGetCertificates();
    static native int nabtoCreateProfile(String email, String password);
    static native int nabtoCreateSelfSignedProfile(String commonName, String password);
    static native int nabtoSignup(String email, String password);
    static native int nabtoResetAccountPassword(String email);

    // The session API
    static native Session nabtoOpenSession(String id, String password);
    static native Session nabtoOpenSessionBare();
    static native int nabtoCloseSession(Object session);
    static native RpcResult nabtoRpcSetDefaultInterface(String interfaceDefinition, Object session);
    static native RpcResult nabtoRpcSetInterface(String host, String interfaceDefinition, Object session);
    static native RpcResult nabtoRpcInvoke(String nabtoUrl, Object session);
    static native UrlResult nabtoFetchUrl(String url, Object session);
    static native UrlResult nabtoSubmitPostData(String nabtoUrl, byte[] postData, String postMimeType, Object session);
    static native String nabtoGetSessionToken(Object session);

    // The streaming API
    static native Stream nabtoStreamOpen(Object session, String nabtoHost);
    static native int nabtoStreamClose(Object Stream);
    static native StreamReadResult nabtoStreamRead(Object Stream);
    static native int nabtoStreamWrite(Object stream, byte[] data);
    static native int nabtoStreamConnectionType(Object stream);
    static native int nabtoStreamSetOption(Object stream, int optionName, byte[] option);

    // The tunnel API
    static native Tunnel nabtoTunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Object session);
    static native int nabtoTunnelClose(Object tunnel);
    static native TunnelInfoResult nabtoTunnelInfo(Object tunnel);
}