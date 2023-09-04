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
public class NabtoApiTestStarted {
    private NabtoApi api;

    @Before
    public void setup() {
        NabtoAndroidAssetManager assetManager = new NabtoAndroidAssetManager(InstrumentationRegistry.getInstrumentation().getContext());
        api = new NabtoApi(assetManager);
        NabtoStatus s = api.startup();
        assertEquals(s, NabtoStatus.OK);
    }

    @After
    public void teardown() {
        api.shutdown();
    }

    @Test
    public void getProtocolPrefixes() {
        api.getProtocolPrefixes();
    }

    @Test
    public void getLocalDevices() {
        api.getLocalDevices();
    }
    @Test
    public void getCertificates() {
        api.getCertificates();
    }

    @Test
    public void testGuestCert() {
        Session session = api.openSession("guest", "");
        assertEquals(session.getStatus(), NabtoStatus.OK);
    }

}
