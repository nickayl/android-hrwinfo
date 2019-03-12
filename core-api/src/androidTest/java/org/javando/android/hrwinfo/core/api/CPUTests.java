package org.javando.android.hrwinfo.core.api;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CPUTests {


    private static CountDownLatch cdl;
    private static ExecutorService exService;
    private boolean called = false;

    @BeforeClass
    public static void beforeClass() {
        cdl = new CountDownLatch(1);
        exService = Executors.newFixedThreadPool(3);
    }

    @Before
    public void before() {
        called = false;
    }

//    @Test
//    public void useAppContext() {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
//
//        assertEquals("com.javando.hrwinfo", appContext.getPackageName());
//    }

    @Test
    public void checkCpuCores() {

        CPU cpu = AndroidHrwInfo
                .getInstance()
                .cpu();

        AtomicInteger numCores = new AtomicInteger();
        exService.submit(() -> {
            cpu.setOnFrequencyChangeListener(cores -> {
                called = true;
                System.out.print("[");
                for (CPU.Core core : cores) {
                    System.out.printf("%d ", core.getCurFrequency());
                }
                System.out.print("]\n");
            }).startCpuFrequencyMonitor();

            numCores.set(cpu.getNumCores());
            // assertEquals(8, numCores);
        });


        try {
            cdl.await(5, TimeUnit.SECONDS);
            //  Thread.sleep(1500);
            cpu.stopCpuFrequencyMonitor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(called);
        assertThat(numCores.get(), is(greaterThan(0)));

    }


    @Test  // With the android emulator, this test will fail.
    public void testMinMaxFreq() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        int min = androidHrwInfo.cpu().getMinFrequency();
        int max = androidHrwInfo.cpu().getMaxFrequency();

        assertNotEquals(min, 0);
        assertNotEquals(max, 0);
    }

    @Test  // With the android emulator, this test will fail.
    public void testMeanMinMaxFreq() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        float meanmax = androidHrwInfo.cpu().getAverageMaximumFrequency();
        float meanmin = androidHrwInfo.cpu().getAverageMinimumFrequency();

        assertNotEquals(meanmax, 0);
        assertNotEquals(meanmin, 0);

        System.out.println("mean "+meanmax+" " +meanmin);
    }

    @Test
    public void getTypeTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();
        CPU.ProcessorInfo procInfo = cpu.getProcessorInfo();

        assertNotNull(procInfo);
        assertNotNull(procInfo.getArchitecture());
    }

    @Test
    public void temperatureMonitorTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();

        AtomicReference<Double> temp = new AtomicReference<>(0.0);
        cpu.setOnTemperatureChangeListener(newTemp -> {
            called = true;
            temp.set(newTemp);
            System.out.format("Temp: [%.1f°]\n", newTemp);
        });

        cpu.startTemperatureMonitor();

        try {
            cdl.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cpu.stopTemperatureMonitor();
        assertTrue(called);
        assertNotEquals(0.0, temp.get());
    }

//    @Test
//    public void testCatCommand() {
//        String command = ((CPUImpl) AndroidHrwInfo.getInstance().cpu()).getCatCommandForCpuFreq();
//        assertNotNull(command);
//        assertThat(command.length(), is(greaterThan(0)));
//    }

    @Test
    public void scalingGovernorTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();

        String scalingGov = cpu.getScalingGovernor();

        assertNotNull(scalingGov);
        assertNotEquals("", scalingGov);
    }

    @Test
    public void clustersTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();
        List<CPU.Cluster> clusters = cpu.getClusters();

        assertNotNull(clusters);
        assertNotEquals(0, clusters.size());
    }

}
