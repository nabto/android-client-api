package com.nabto.api;

/**
 * Result object of the function {@link NabtoApi#tunnelInfo(Tunnel)}.
 * <p>
 *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return values of
 *     all {@link #getVersion()}, {@link #getTunnelState()}, {@link #getLastError()}, and
 *     {@link #getPort()} are undefined.
 * </p>
 */
public class TunnelInfoResult {
    private NabtoStatus status;
    private int version;
    private NabtoTunnelState tunnelState;
    private int lastError;
    private int port;

    TunnelInfoResult(int version, int nabtoTunnelState, int lastError, int port, int nabtoStatus) {
        this.status = NabtoStatus.fromInteger(nabtoStatus);
        this.version = version;
        this.tunnelState = NabtoTunnelState.fromInteger(nabtoTunnelState);
        this.lastError = lastError;
        this.port = port;
    }

    /**
     * The protocol version of the tunnel.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The protocol version of the tunnel.
     */
    public int getVersion() {
        return version;
    }

    /**
     * The state of the tunnel.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The state of the tunnel.
     */
    public NabtoTunnelState getTunnelState() {
        return tunnelState;
    }

    /**
     * The error code of the last error.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The error code of the last error.
     */
    public int getLastError() {
        return lastError;
    }

    /**
     * The local listening port of the tunnel.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The local listening port of the tunnel.
     */
    public int getPort() {
        return port;
    }

    /**
     * The success of the function call which returned this {@link TunnelInfoResult} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}