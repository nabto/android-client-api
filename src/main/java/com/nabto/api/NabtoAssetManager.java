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

class NabtoAssetManager {
    private Context context;
    private File nabtoHomeDirectory;
    private File nabtoResourceDirectory;
    private File nabtoNativeLibraryDirectory;

    public NabtoAssetManager(Context context) {
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

    public String getNabtoResourceDirectory() {
        return nabtoResourceDirectory.getAbsolutePath();
    }

    public String getNabtoHomeDirectory() {
        return nabtoHomeDirectory.getAbsolutePath();
    }

    public String getNabtoNativeLibraryDirectory() {
        return nabtoNativeLibraryDirectory.getAbsolutePath();
    }

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
        // Only write to file if it does not exist already
        if (fileLocation.exists() && !overwrite)
            return;

        // Create necessary directory structure
        fileLocation.getParentFile().mkdirs();
        Log.d(this.getClass().getSimpleName(), "Writing asset file: " + asset + " to "
                + fileLocation.getAbsolutePath());
        try {
            InputStream inStream = new BufferedInputStream(manager.open(asset,
                    AssetManager.ACCESS_STREAMING));
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
