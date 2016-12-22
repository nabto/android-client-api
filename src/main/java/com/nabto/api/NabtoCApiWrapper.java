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
    static native NabtoStatus nabtoGetFingerprint(String certId, byte[] fingerprint);
    static native NabtoStatus nabtoSignup(String email, String password);
    static native NabtoStatus nabtoResetAccountPassword(String email);

    // The session API
    static native Session nabtoOpenSession(String id, String password);
    static native Session nabtoOpenSessionBare();
    static native NabtoStatus nabtoCloseSession(Session session);
    static native RpcResult nabtoRpcSetDefaultInterface(String interfaceDefinition, Session session);
    static native RpcResult nabtoRpcSetInterface(String host, String interfaceDefinition, Session session);
    static native RpcResult nabtoRpcInvoke(String nabtoUrl, Session session);
    static native UrlResult nabtoFetchUrl(String url, Session session);
    static native UrlResult nabtoSubmitPostData(String nabtoUrl, byte[] postData, String postMimeType, Session session);
    static native String nabtoGetSessionToken(Session session);

    // The streaming API
    static native Stream nabtoStreamOpen(String nabtoHost, Session session);
    static native NabtoStatus nabtoStreamClose(Stream stream);
    static native StreamReadResult nabtoStreamRead(Stream stream);
    static native NabtoStatus nabtoStreamWrite(byte[] data, Stream stream);
    static native NabtoConnectionType nabtoStreamConnectionType(Stream stream);
    static native NabtoStatus nabtoStreamSetOption(int optionName, byte[] option, Stream stream);

    // The tunnel API
    static native Tunnel nabtoTunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Session session);
    static native NabtoStatus nabtoTunnelClose(Tunnel tunnel);
    static native TunnelInfoResult nabtoTunnelInfo(Tunnel tunnel);
}
