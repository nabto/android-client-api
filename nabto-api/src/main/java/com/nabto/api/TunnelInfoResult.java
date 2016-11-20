package com.nabto.api;

/**
 * Result object of the function {@link NabtoApi#tunnelInfo(Tunnel)}.
 * <p>
 *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return values of
 *     both {@link #getTunnelState()} and {@link #getPort()} are undefined.
 * </p>
 */
public class TunnelInfoResult {
    private NabtoStatus status;
    private NabtoTunnelState tunnelState;
    private int port;

    TunnelInfoResult(int nabtoTunnelState, int port, int nabtoStatus) {
        this.status = NabtoStatus.fromInteger(nabtoStatus);
        this.tunnelState = NabtoTunnelState.fromInteger(nabtoTunnelState);
        this.port = port;
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