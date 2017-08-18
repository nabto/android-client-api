package com.nabto.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Handles the Nabto assets used by the {@link NabtoApi} on Android.
 * @deprecated Resources are now embedded in the Nabto Client SDK and installed into the homedir at
 * startup and there is no need to manage resources, this class will be removed in next major
 * release.
 */
@Deprecated
public class NabtoAndroidAssetManager implements NabtoAssetManager {
    private Context context;
    private File nabtoHomeDirectory;
    private File nabtoResourceDirectory;
    private File nabtoNativeLibraryDirectory;

    /**
     * Creates a new Nabto asset manager for Android.
     *
     * @param context The Android app context.
     */
    public NabtoAndroidAssetManager(Context context) {
        this.context = context;
        ApplicationInfo info = context.getApplicationInfo();

        nabtoHomeDirectory = new File(context.getFilesDir(), "nabto");
        nabtoResourceDirectory = new File(context.getFilesDir(), "share/nabto");
        nabtoNativeLibraryDirectory = new File(info.nativeLibraryDir);

        Log.d(this.getClass().getSimpleName(), "Native lib dir: " + getNabtoNativeLibraryDirectory());
        Log.d(this.getClass().getSimpleName(), "App resource dir: " + getNabtoResourceDirectory());
        Log.d(this.getClass().getSimpleName(), "App dir: " + getNabtoHomeDirectory());
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
     * Returns the absolute path of the Nabto resource directory (see deprecation notice above; the
     * resource directory is no longer needed)
     * @return The absolute path of the Nabto resource directory.
     */
    public String getNabtoResourceDirectory() {
        return getNabtoHomeDirectory();
    }

    String getNabtoNativeLibraryDirectory() { return nabtoNativeLibraryDirectory.getAbsolutePath(); }

}
