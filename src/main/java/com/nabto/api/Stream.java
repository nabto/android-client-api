package com.nabto.api;

/**
 * Result object of the function {@link NabtoApi#streamOpen(String, Session)}.
 * <p>
 *     If {@link #getStatus()} is {@link NabtoStatus#OK}, this object contains a valid stream handle
 *     that can be used in subsequent client API invocations.
 * </p>
 */
public class Stream {
    private Object handle;
    private NabtoStatus status;

    Stream(Object handle, int nabtoStatus) {
        this.handle = handle;
        this.status = NabtoStatus.fromInteger(nabtoStatus);
    }

    /**
     * Stream handle.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return Stream handle.
     */
    Object getHandle() {
        return handle;
    }

    /**
     * The success of the function call which returned this {@link Stream} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}
