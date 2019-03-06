package org.javando.android.hrwinfo.core.api;

import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class SystemInfoTest {

    private static final String TAG = "Sysinfo tag";
    private static SystemInfo sysInfo;

    @Rule
    public ActivityTestRule<TestActivity> activityRule = new ActivityTestRule<>(TestActivity.class);

    @Before
    public void beforeClass() {
        sysInfo = AndroidHrwInfo.getInstance().systemInfo(activityRule.getActivity());
    }

    @Test
    public void numProcessesTest() {
        final int[] num = {0};

        sysInfo.getProcessRunning(value -> {
            num[0] = value;
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertNotEquals(0, num[0]);
        assertThat(num[0], is(greaterThan(1)));

        Log.d(TAG, "Num processes: " + num[0]);
    }

    @Test
    public void getNumThreadsTest() {
        int num = sysInfo.getNumThreads();
        System.out.println(num);
    }

    @Test
    public void securityPatchLevelTest() {
        String patch = sysInfo.getSecurityPatchLevel();
        System.out.println(patch);
    }

    @Test
    public void testSystemInfo() {
        Log.d(TAG, "testSystemInfo: " + sysInfo);

        ExecutorService exSer = Executors.newFixedThreadPool(1);

        exSer.submit(() -> {
            sysInfo.setSystemUptimeMonitor((days, hours, minutes, seconds) -> {
                Log.d(TAG, "testSystemInfo: " + sysInfo.formatSystemUptime(days, hours, minutes, seconds));
            });

        });

        try {
            exSer.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "testSystemInfo: timeout!");
    }

}