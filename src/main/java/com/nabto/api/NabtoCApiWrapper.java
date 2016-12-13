package com.nabto.api;

class NabtoCApiWrapper {
    static {
        System.loadLibrary("nabto_client_api_jni");
    }

    // Configuration and initialization API
    static native String nabtoVersion();
    static native NabtoStatus nabtoStartup(String nabtoHomeDir);
    static native NabtoStatus nabtoShutdown();
    static native NabtoStatus nabtoSetStaticResourceDir(String nabtoResDir);
    static native String[] nabtoGetProtocolPrefixes();
    static native String[] nabtoGetLocalDevices();
    static native NabtoStatus nabtoProbeNetwork(int timeoutMillis, String hostname);

    // The portal API
    static native String[] nabtoGetCertificates();
    static native NabtoStatus nabtoCreateProfile(String email, String password);
    static native NabtoStatus nabtoCreateSelfSignedProfile(String commonName, String password);
    static native NabtoStatus nabtoSignup(String email, String password);
    static native NabtoStatus nabtoResetAccountPassword(String email);

    // The session API
    static native Session nabtoOpenSession(String id, String password);
    static native Session nabtoOpenSessionBare();
    static native NabtoStatus nabtoCloseSession(Object session);
    static native RpcResult nabtoRpcSetDefaultInterface(String interfaceDefinition, Object session);
    static native RpcResult nabtoRpcSetInterface(String host, String interfaceDefinition, Object session);
    static native RpcResult nabtoRpcInvoke(String nabtoUrl, Object session);
    static native UrlResult nabtoFetchUrl(String url, Object session);
    static native UrlResult nabtoSubmitPostData(String nabtoUrl, byte[] postData, String postMimeType, Object session);
    static native String nabtoGetSessionToken(Object session);

    // The streaming API
    static native Stream nabtoStreamOpen(String nabtoHost, Object session);
    static native NabtoStatus nabtoStreamClose(Object Stream);
    static native StreamReadResult nabtoStreamRead(Object Stream);
    static native NabtoStatus nabtoStreamWrite(byte[] data, Object stream);
    static native NabtoConnectionType nabtoStreamConnectionType(Object stream);
    static native NabtoStatus nabtoStreamSetOption(int optionName, byte[] option, Object stream);

    // The tunnel API
    static native Tunnel nabtoTunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Object session);
    static native NabtoStatus nabtoTunnelClose(Object tunnel);
    static native TunnelInfoResult nabtoTunnelInfo(Object tunnel);
}