package com.nabto.api;

/**
 * Result object of the function {@link NabtoApi#openSession(String, String)} or
 * {@link NabtoApi#openSessionBare()}.
 * <p>
 *     If {@link #getStatus()} is {@link NabtoStatus#OK}, this object contains a valid session
 *     handle that can be used in subsequent client API invocations.
 * </p>
 */
public class Session {
    private Object handle;
    private NabtoStatus status;

    Session(Object handle, int nabtoStatus) {
        this.handle = handle;
        this.status = NabtoStatus.fromInteger(nabtoStatus);
    }

    /**
     * Session handle.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return Session handle.
     */
    Object getHandle() {
        return handle;
    }

    /**
     * The success of the function call which returned this {@link Session} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}
