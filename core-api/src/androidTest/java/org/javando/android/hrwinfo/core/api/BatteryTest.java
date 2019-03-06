package org.javando.android.hrwinfo.core.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BatteryTest {

    private static Battery battery;

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void before() {
        battery = AndroidHrwInfo.getInstance().battery(activityRule.getActivity());
    }

    @Test
    public void testBatteryActivity() {

        AtomicBoolean activity = new AtomicBoolean(false);

        Executors.newSingleThreadExecutor().submit(() -> {

            battery.setOnChangeEventListener(b -> {
                System.out.println(b);
                activity.set(true);
            });

        });


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(activity.get());
        battery.removeOnChangeEventListener();
    }


}