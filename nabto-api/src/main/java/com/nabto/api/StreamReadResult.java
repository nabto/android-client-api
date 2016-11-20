package com.nabto.api;

/**
 * Result object of the function {@link NabtoApi#streamRead(Stream)}.
 * <p>
 *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value of
 *     {@link #getData()} is undefined.
 * </p>
 */
public class StreamReadResult {
    private byte[] data;
    private NabtoStatus status;

    StreamReadResult(byte[] data, int nabtoStatus) {
        this.data = data;
        this.status = NabtoStatus.fromInteger(nabtoStatus);
    }

    /**
     * The data read from the stream.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The data read from the stream.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * The success of the function call which returned this {@link StreamReadResult} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}
