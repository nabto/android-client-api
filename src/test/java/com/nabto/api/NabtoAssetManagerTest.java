package com.nabto.api;

import android.util.Log;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
    
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, AssetManager.class, ApplicationInfo.class, Context.class})
public class NabtoAssetManagerTest {

    private static final int TEMP_DIR_ATTEMPTS = 100;
    private File rootDir;
    private File filesDir;
    private File bundleDir;
    private Context context;
    private AssetManager assets;

    private File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = System.currentTimeMillis() + "-";
        
        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                tempDir.deleteOnExit();
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within "
                                        + TEMP_DIR_ATTEMPTS + " attempts (tried "
                                        + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
    }

    private File createFileInDir(File dir, String name, String contents) {
        try {
            File file = new File(dir, name);
            if (!file.createNewFile()) {
                throw new IOException("Could not create file " + file);
            }
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.println(contents);
            writer.close();
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private String readFileIntoString(File file) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
            return new String(encoded, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private File createSubDir(File rootDir, String name) {
        File dir = new File(rootDir, name);
        assertTrue(dir.mkdir());
        return dir;
    }
    
    @Before
    public void setUp() {
        rootDir = createTempDir();
        bundleDir = createSubDir(rootDir, "bundle");
        filesDir = createSubDir(rootDir, "files");
        
        assets = mock(AssetManager.class);
        
        ApplicationInfo info = mock(ApplicationInfo.class);
        info.nativeLibraryDir = "/tmp";
        
        context = mock(Context.class);
        when(context.getFilesDir()).thenReturn(filesDir);
        when(context.getAssets()).thenReturn(assets);
        when(context.getApplicationInfo()).thenReturn(info);
        
        mockStatic(Log.class);
    }

    @After
    public void tearDown() {
    }

    
    @Test
    public void copyFileInExistingShareDirTest() {
        String subdirName = "share";
        File dir = createSubDir(bundleDir, subdirName);
        String filename = "file1";
        String body = "line1\nline2\nline3\n";
        try {
            when(assets.list(subdirName)).thenReturn(new String[]{filename}); // ("share") => ["file1"] 
            when(assets.list(subdirName + "/" + filename)).thenReturn(new String[]{}); // ("share/file1") => [] (leaf)
            when(assets.open(anyString(), anyInt())).thenReturn(new ByteArrayInputStream(body.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        NabtoAssetManager sut = new NabtoAndroidAssetManager(context);

        File copiedFile = new File(filesDir + "/" + subdirName, filename);
        assertEquals(readFileIntoString(copiedFile), body);
    }
     

}
