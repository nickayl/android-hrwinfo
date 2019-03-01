package com.javando.hrwinfo;


import android.util.Log;

import org.javando.android.hrwinfo.core.api.Device;
import org.javando.android.hrwinfo.core.impl.DeviceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DeviceTest {

    private static Device device;

    @BeforeClass
    public static void beforeClass() {
        device = new DeviceImpl(InstrumentationRegistry.getInstrumentation().getContext());
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void globalTest() {

        assertNotNull(device.getBrand());
        assertNotNull(device.getCommercialName());
        assertNotNull(device.getHardwareCode());
        assertNotNull(device.getModel());

        assertThat(0, not(equalTo(device.getAvailableRam())));
        assertThat(0, not(equalTo(device.getAvailableStorage())));

        assertNotEquals(0, device.getAvailableRamPercent());
        assertNotEquals(0, device.getAvailableStoragePercent());

//        assertNotEquals(0, device.getScreenDensity());
//        assertNotEquals(0, device.getScreenSize());

        assertNotNull(device.getHardwareCode());

        Log.d("tEST", "globalTest: "+device.toString());
        System.out.println(device.toString());
    }

    @Test
    public void ramAndStorageListenerTest() {

        final boolean[] called = {false};

        device.setOnAvailableRamChangeListener(device1 -> {
            System.out.println(device.getAvailableRam() + " "+device.getAvailableRamPercent() + "%");
            called[0] = true;
        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(called[0]);

    }
}