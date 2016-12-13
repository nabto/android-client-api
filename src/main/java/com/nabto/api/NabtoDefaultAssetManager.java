package com.nabto.api;

import java.io.File;

/**
 * Handles the Nabto assets used by the {@link NabtoApi}.
 * <p>
 *     For Android see {@link NabtoAndroidAssetManager}.
 * </p>
 */
public class NabtoDefaultAssetManager {
    private File nabtoHomeDirectory;
    private File nabtoResourceDirectory;

    /**
     * Creates a new Nabto asset manager.
     *
     * @param nabtoHomeDirectory The absolute path of the Nabto home directory "nabto".
     * @param nabtoResourceDirectory The absolute path of the Nabto home resource directory "share/nabto".
     */
    public NabtoDefaultAssetManager(File nabtoHomeDirectory, File nabtoResourceDirectory) {
        this.nabtoHomeDirectory = nabtoHomeDirectory;
        this.nabtoResourceDirectory = nabtoResourceDirectory;
    }

    /**
     * Returns the absolute path of the Nabto home directory "nabto".
     *
     * @return The absolute path of the Nabto home directory.
     */
    public String getNabtoHomeDirectory() {
        return nabtoHomeDirectory.getAbsolutePath();
    }

    /**
     * Returns the absolute path of the Nabto resource directory "share/nabto".
     *
     * @return The absolute path of the Nabto resource directory.
     */
    public String getNabtoResourceDirectory() {
        return nabtoResourceDirectory.getAbsolutePath();
    }
}
