package com.nabto.api;

/**
 * @deprecated Resources are now embedded in the Nabto Client SDK and installed into the homedir at
 * startup and there is no need to manage resources, this class will be removed in next major
 * release.
 */
@Deprecated
public interface NabtoAssetManager {
    /**
     * Returns the absolute path of the Nabto home directory "nabto".
     *
     * @return The absolute path of the Nabto home directory.
     */
    public String getNabtoHomeDirectory();
    /**
     * Returns the absolute path of the Nabto resource directory "share/nabto".
     *
     * @return The absolute path of the Nabto resource directory.
     */
    public String getNabtoResourceDirectory();
}
