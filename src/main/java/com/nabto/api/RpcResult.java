package com.nabto.api;

/**
 * Result object of the functions {@link NabtoApi#rpcSetDefaultInterface(String, Session)},
 * {@link NabtoApi#rpcSetInterface(String, String, Session)}, and
 * {@link NabtoApi#rpcInvoke(String, Session)}.
 * <p>
 *     Depending on the returning function, {@link #getJson()} can be undefined depending on the
 *     value returned by {@link #getStatus()}. See documentation of the returning function for
 *     further details.
 * </p>
 */
public class RpcResult {
    private String json;
    private NabtoStatus status;

    RpcResult(String json, int nabtoStatus) {
        this.json = json;
        this.status = NabtoStatus.fromInteger(nabtoStatus);
    }

    /**
     * The JSON formatted string returned from the device.
     * <p>
     *     Depending on the returning function, the value can be undefined depending on the
     *     value returned by {@link #getStatus()}. See documentation of the returning function for
     *     further details ({@link NabtoApi#rpcSetDefaultInterface(String, Session)},
     *     {@link NabtoApi#rpcSetInterface(String, String, Session)}, or
     *     {@link NabtoApi#rpcInvoke(String, Session)}.
     * </p>
     *
     * @return The JSON formatted string returned from the device.
     */
    public String getJson() {
        return json;
    }

    /**
     * The success of the function call which returned this {@link RpcResult} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}