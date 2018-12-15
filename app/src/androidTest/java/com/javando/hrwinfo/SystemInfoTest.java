package com.javando.hrwinfo;

import android.util.Log;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;
import org.javando.android.hrwinfo.core.api.AndroidHrwInfo;
import org.javando.android.hrwinfo.core.api.SystemInfo;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class SystemInfoTest {

    private static final String TAG = "Sysinfo tag";
    @Rule
    public ActivityTestRule<MainActivity> activityRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSystemInfo() {
        SystemInfo sysInfo = AndroidHrwInfo.getInstance().systemInfo(activityRule.getActivity());
        Log.d(TAG, "testSystemInfo: "+sysInfo);

        ExecutorService exSer = Executors.newFixedThreadPool(1);

        exSer.submit(() -> {
            sysInfo.setSystemUptimeMonitor((days, hours, minutes, seconds) -> {
                Log.d(TAG, "testSystemInfo: "+sysInfo.formatSystemUptime(days, hours, minutes, seconds));
            });

        });

        try {
            exSer.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "testSystemInfo: timeout!");
    }

}