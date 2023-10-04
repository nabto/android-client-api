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
 * Handles the few remaining Nabto assets used by the {@link NabtoApi} on Android that have not been embedded in the
 * SDK directly. This includes skin bundles.
 */
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

        copyDirContentsToLocation(context.getAssets(), "share",
                new File(context.getFilesDir() + "/share"), false);
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

    String getNabtoNativeLibraryDirectory() { return nabtoNativeLibraryDirectory.getAbsolutePath(); }

    private void copyDirContentsToLocation(AssetManager manager,
                                           String fileToCopy, File fileLocation, boolean overwrite) {
        try {
            String[] filesInDir = manager.list(fileToCopy);
            if (filesInDir.length == 0) {
                // this is a file
                copyFromAssets(manager, fileToCopy, fileLocation, overwrite);
            } else {
                for (String fileInDir : filesInDir) {
                    copyDirContentsToLocation(manager, fileToCopy + "/"
                            + fileInDir, new File(fileLocation + "/"
                            + fileInDir), overwrite);
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Could not get assets from directory " + fileToCopy, e);
        }
    }

    /**
     * Copy the specified asset from assets to the specified fileLocation
     */
    private void copyFromAssets(AssetManager manager, String asset,
                                File fileLocation, boolean overwrite) {
        // Create necessary directory structure
        if (!fileLocation.getParentFile().exists() && !fileLocation.getParentFile().mkdirs()) {
            throw new IllegalArgumentException("Could not create directory: " + fileLocation.getParentFile().getPath());
        }
        Log.d(this.getClass().getSimpleName(), "Writing asset file: " + asset + " to "
                + fileLocation.getAbsolutePath());
        InputStream inStream;
        try {
            inStream = new BufferedInputStream(manager.open(asset,
                                                            AssetManager.ACCESS_STREAMING));
        } catch (IOException e) {
            Log.w(this.getClass().getSimpleName(), "Could not read asset file: " + asset);
            // don't throw - it is ok to miss a file in the app bundle (asset manager used with
            // different file configurations), just don't install in that case
            return;
        }
        try {
            OutputStream outStream = new BufferedOutputStream(new FileOutputStream(fileLocation));
            byte[] buffer = new byte[10240]; // 10KB
            int length = 0;
            while ((length = inStream.read(buffer)) >= 0) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not write file to " + fileLocation, e);
        }
    }
}
