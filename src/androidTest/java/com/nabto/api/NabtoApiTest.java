package com.nabto.api;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NabtoApiTest {
    @Test
    public void nabtoVersion() {
        NabtoAndroidAssetManager assetManager = new NabtoAndroidAssetManager(InstrumentationRegistry.getInstrumentation().getContext());
        NabtoApi api = new NabtoApi(assetManager);

        String v = api.version();

        assertTrue(v, v.matches("^\\d+\\.\\d+.*$"));
    }

    @Test
    public void startup() {
        NabtoAndroidAssetManager assetManager = new NabtoAndroidAssetManager(InstrumentationRegistry.getInstrumentation().getContext());
        NabtoApi api = new NabtoApi(assetManager);

        NabtoStatus s = api.startup();
        assertEquals(s, NabtoStatus.OK);
    }
}
