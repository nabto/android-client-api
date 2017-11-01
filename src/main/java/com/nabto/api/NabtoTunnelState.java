package com.nabto.api;

/**
 * The NabtoTunnelState enumeration is telling the state of the tunnel
 * and the underlying connection type for a stream carrying the tunnel.
 */
public enum NabtoTunnelState {
    /**
     * The tunnel is closed.
     */
    CLOSED,

    /**
     * The tunnel is connecting.
     */
    CONNECTING,

    /**
     * The other end of the tunnel (the device) has disappeared. The client
     * must connect again.
     */
    READY_FOR_RECONNECT,

    /**
     * The tunnel is connected and the tunnel is running on an unrecognized
     * connection type. This value indicates an internal error, since we
     * always know the underlying connection type if it exists.
     */
    UNKNOWN,

    /**
     * The tunnel is connected and the tunnel is running on a local
     * connection (no Internet).
     */
    LOCAL,

    /**
     * The tunnel is connected and the tunnel is running on a direct
     * connection (peer-to-peer).
     */
    REMOTE_P2P,

    /**
     * The tunnel is connected and the tunnel is running on a fallback
     * connection through the base-station.
     */
    REMOTE_RELAY,

    /**
     * The tunnel is connected and the tunnel is running on a connection
     * that runs through a relay node on the Internet. The device is
     * capable of using TCP/IP and the connection runs directly from the
     * device to the relay node to the client.
     */
    REMOTE_RELAY_MICRO;

    static NabtoTunnelState fromInteger(int val) {
        val++;
        if (val < NabtoTunnelState.values().length && val >= 0) {
            return NabtoTunnelState.values()[val];
        } else {
            return UNKNOWN;
        }
    }

    int toInteger() {
        return this.ordinal() - 1;
    }

    public String toString() {
        switch (this) {
            case CLOSED:
                return "Closed";
            case CONNECTING:
                return "Connecting...";
            case READY_FOR_RECONNECT:
                return "Ready for TCP client retry";
            case UNKNOWN:
                return "Unknown connection";
            case LOCAL:
                return "Local";
            case REMOTE_P2P:
                return "Remote P2P";
            case REMOTE_RELAY:
                return "Remote Relay (UDP)";
            case REMOTE_RELAY_MICRO:
                return "Remote Relay (TCP)";
            default:
                return "?";
        }
    }
}
