package com.nabto.api;

public class NabtoCApiWrapper {
    static {
        System.loadLibrary("nabto_client_api_jni");
    }

    // Configuration and initialization API
    public static native String nabtoVersion();
    public static native String nabtoVersionString();
    public static native NabtoStatus nabtoStartup(String nabtoHomeDir);
    public static native NabtoStatus nabtoShutdown();
    public static native NabtoStatus nabtoSetOption(String name, String value);
    public static native NabtoStatus nabtoSetStaticResourceDir(String nabtoResDir);
    public static native NabtoStatus nabtoInstallDefaultStaticResources(String nabtoResDir);
    public static native String[] nabtoGetProtocolPrefixes();
    public static native String[] nabtoGetLocalDevices();
    public static native NabtoStatus nabtoProbeNetwork(int timeoutMillis, String hostname);

    // The portal API
    public static native String[] nabtoGetCertificates();
    public static native NabtoStatus nabtoCreateProfile(String email, String password);
    public static native NabtoStatus nabtoCreateSelfSignedProfile(String commonName, String password);
    public static native NabtoStatus nabtoRemoveProfile(String certId);
    public static native NabtoStatus nabtoGetFingerprint(String certId, byte[] fingerprint);
    public static native NabtoStatus nabtoSignup(String email, String password);
    public static native NabtoStatus nabtoResetAccountPassword(String email);

    // The session API
    public static native Session nabtoOpenSession(String id, String password);
    public static native Session nabtoOpenSessionBare();
    public static native NabtoStatus nabtoCloseSession(Session session);
    public static native NabtoStatus nabtoSetBasestationAuthJson(String jsonKeyValuePairs, Session session);
    public static native NabtoStatus nabtoSetLocalConnectionPsk(String host, byte[] pskId, byte[] psk, Session session);
    public static native RpcResult nabtoRpcSetDefaultInterface(String interfaceDefinition, Session session);
    public static native RpcResult nabtoRpcSetInterface(String host, String interfaceDefinition, Session session);
    public static native RpcResult nabtoRpcInvoke(String nabtoUrl, Session session);
    public static native UrlResult nabtoFetchUrl(String url, Session session);
    public static native UrlResult nabtoSubmitPostData(String nabtoUrl, byte[] postData, String postMimeType, Session session);
    public static native String nabtoGetSessionToken(Session session);

    // The streaming API
    public static native Stream nabtoStreamOpen(String nabtoHost, Session session);
    public static native NabtoStatus nabtoStreamClose(Stream stream);
    public static native StreamReadResult nabtoStreamRead(Stream stream);
    public static native NabtoStatus nabtoStreamWrite(byte[] data, Stream stream);
    public static native NabtoConnectionType nabtoStreamConnectionType(Stream stream);
    public static native NabtoStatus nabtoStreamSetOption(int optionName, byte[] option, Stream stream);

    // The tunnel API
    public static native Tunnel nabtoTunnelOpenTcp(int localPort, String nabtoHost, String remoteHost, int remotePort, Session session);
    public static native NabtoStatus nabtoTunnelClose(Tunnel tunnel);
    public static native TunnelInfoResult nabtoTunnelInfo(Tunnel tunnel);
    public static native NabtoStatus nabtoTunnelSetRecvWindowSize(int recvWindowSize, Tunnel tunnel);
    public static native NabtoStatus nabtoTunnelSetSendWindowSize(int sendWindowSize, Tunnel tunnel);
}
