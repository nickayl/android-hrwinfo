package com.javando.hrwinfo;


import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.javando.android.hrwinfo.core.api.Device;
import org.javando.android.hrwinfo.core.impl.DeviceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

        assertNotNull(device.getAvailableRam());
        assertNotNull(device.getAvailableStorage());
        assertNotNull(device.getBrand());
        assertNotNull(device.getCommercialName());
        assertNotNull(device.getHardwareCode());
        assertNotNull(device.getModel());

        assertThat(0, not(equalTo(device.getAvailableRam().getValue())));
        assertThat(0, not(equalTo(device.getAvailableStorage().getValue())));

//        assertNotEquals(0, device.getScreenDensity());
//        assertNotEquals(0, device.getScreenSize());

        assertNotNull(device.getHardwareCode());

        Log.d("tEST", "globalTest: "+device);
        System.out.println(device.toString());
    }
}