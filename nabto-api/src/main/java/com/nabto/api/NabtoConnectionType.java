package com.nabto.api;

/**
 * The NabtoConnectionType enumeration is telling the underlying
 * connection type for a stream.
 */
public enum NabtoConnectionType {
    /**
     * The stream is running on a local connection (no Internet).
     */
    LOCAL,

    /**
     * The stream is running on a direct connection (peer-to-peer).
     */
    P2P,

    /**
     * The stream is running on a fallback connection through the
     * base-station.
     */
    RELAY,

    /**
     * The stream is running on an unrecognized connection type. This
     * value indicates that the connection is lost, since we always
     * know the underlying connection type if it exists.
     */
    UNKNOWN,

    /**
     * The stream is running on a connection that runs through a relay
     * node on the Internet. The device is capable of using TCP/IP and
     * the connection runs directly from the device to the relay node
     * to the client.
     */
    RELAY_MICRO;

    static NabtoConnectionType fromInteger(int connectionType) {
        if (connectionType < NabtoConnectionType.values().length && connectionType >= 0) {
            return NabtoConnectionType.values()[connectionType];
        } else {
            return UNKNOWN;
        }
    }

    int toInteger() {
        return this.ordinal();
    }
}