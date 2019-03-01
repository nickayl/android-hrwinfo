package com.javando.hrwinfo;

import org.javando.android.hrwinfo.core.api.AndroidHrwInfo;
import org.javando.android.hrwinfo.core.api.SensorListener;
import org.javando.android.hrwinfo.core.api.Sensors;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class SensorTests {

    private static Sensors sensors;
    private boolean called = false;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void before() {
        sensors = AndroidHrwInfo.getInstance().sensors(activityRule.getActivity());
        sensors.registerListeners();
    }

    @Test
    public void testSensors() {

        for(SensorListener sl : sensors.getSensorEventListeners()) {
            sl.setOnValueChangeListener((values, sensor) -> {
                called = true;
                System.out.println(sensor.getName()+" Value changed: "+values);
            });
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(called);
        sensors.unregisterListeners();
    }


}