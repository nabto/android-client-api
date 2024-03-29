package com.nabto.api;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NabtoApiTest {

    private NabtoApi api;

    @Before
    public void setup() {
        NabtoAndroidAssetManager assetManager = new NabtoAndroidAssetManager(InstrumentationRegistry.getInstrumentation().getContext());
        api = new NabtoApi(assetManager);
    }

    @Test
    public void nabtoVersionString() {
        String v = api.versionString();
        assertTrue(v, v.matches("^\\d+\\.\\d+\\.\\d+.*$"));
    }

    @Test
    public void startup() {
        NabtoStatus s = api.startup();
        assertEquals(s, NabtoStatus.OK);
    }

    @Test
    public void shutdown() {
        NabtoStatus s = api.startup();
        assertEquals(s, NabtoStatus.OK);
        s = api.shutdown();
        assertEquals(s, NabtoStatus.OK);
    }
}
