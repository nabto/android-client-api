package com.nabto.api;

/**
 * Result object of the functions {@link NabtoApi#fetchUrl(String, Session)}, and
 * {@link NabtoApi#submitPostData(String, byte[], String, Session)}.
 * <p>
 *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return values of
 *     both {@link #getResult()} and {@link #getMimeType()} are undefined.
 * </p>
 */
public class UrlResult {
    private byte[] result;
    private String mimeType;
    private NabtoStatus status;

    UrlResult(byte[] result, String mimeType, int nabtoStatus) {
        this.result = result;
        this.mimeType = mimeType;
        this.status = NabtoStatus.fromInteger(nabtoStatus);
    }

    /**
     * The content returned from the device.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The content returned from the device.
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * The MIME type of the content returned from the device.
     * <p>
     *     If {@link #getStatus()} is different from {@link NabtoStatus#OK}, the return value
     *     is undefined.
     * </p>
     *
     * @return The MIME type of content returned from the device.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * The success of the function call which returned this {@link UrlResult} object.
     *
     * @return The success of the function call.
     */
    public NabtoStatus getStatus() {
        return status;
    }
}