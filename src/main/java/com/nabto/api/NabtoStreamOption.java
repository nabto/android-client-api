package com.nabto.api;

/**
 * The NabtoStreamOption enumeration is listing the valid options that
 * can be applied to a nabto stream.
 */
public enum NabtoStreamOption {
    /**
     * Set timeout for receive operations in ms.
     * The argument is an int. -1 means wait indefinitly, 0 means non-blocking.
     * Default value is -1
     */
    RECEIVE_TIMEOUT,

    /**
     * Set timeout for send operations in ms.
     * The argument is an int. -1 means infinite, 0 means non-blocking.
     */
    SEND_TIMEOUT,

    /**
     * An invalid option.
     */
    INVALID;


    static NabtoStreamOption fromInteger(int val) {
        val--;
        if (val < NabtoStreamOption.values().length && val >= 0) {
            return NabtoStreamOption.values()[val];
        } else {
            return INVALID;
        }
    }

    int toInteger() {
        return this.ordinal() + 1;
    }
}
