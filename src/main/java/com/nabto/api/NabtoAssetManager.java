package com.nabto.api;

interface NabtoAssetManager {
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
