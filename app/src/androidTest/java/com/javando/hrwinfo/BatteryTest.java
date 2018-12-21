package com.javando.hrwinfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import org.javando.android.hrwinfo.core.api.AndroidHrwInfo;
import org.javando.android.hrwinfo.core.api.Battery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(AndroidJUnit4.class)
public class BatteryTest {

    private static Battery battery;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void before() {
        battery = AndroidHrwInfo.getInstance().battery(activityRule.getActivity());
    }

    @Test
    public void testBatteryActivity() {

        AtomicBoolean activity = new AtomicBoolean(false);

        new Thread(() -> {

            battery.setOnChangeEventListener(b -> {
                System.out.println(b);
                activity.set(true);
            });

        }).start();


        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}