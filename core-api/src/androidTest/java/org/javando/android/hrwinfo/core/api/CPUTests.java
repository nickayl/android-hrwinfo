package org.javando.android.hrwinfo.core.api;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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

    @BeforeClass
    public static void before() {
        cdl = new CountDownLatch(1);
        exService = Executors.newFixedThreadPool(3);
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

        exService.submit(() -> {
            cpu.setOnFrequencyChangeListener(cores -> {
                System.out.print("[");
                for (CPU.Core core : cores) {
                    System.out.printf("%d ", core.getCurFrequency());
                }
                System.out.print("]\n");
            }).startCpuFrequencyMonitor();

            int numCores = cpu.getNumCores();
            // assertEquals(8, numCores);
        });


        try {
            cdl.await(50, TimeUnit.SECONDS);
            //  Thread.sleep(1500);
            cpu.stopCpuFrequencyMonitor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //        assertThat(cores,
//                is(both(
//                        greaterThan(1)).
//                        and(lessThan(8))
//                ));

    }


    @Test
    public void testMinMaxFreq() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        int min = androidHrwInfo.cpu().getMinFrequency();
        int max = androidHrwInfo.cpu().getMaxFrequency();

        System.out.println("");
    }

    @Test
    public void testMeanMinMaxFreq() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        float meanmax = androidHrwInfo.cpu().getAverageMaximumFrequency();
        float meanmin = androidHrwInfo.cpu().getAverageMinimumFrequency();

        System.out.println("mean "+meanmax+" " +meanmin);
    }

    @Test
    public void getTypeTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();
        cpu.getProcessorInfo();

        assertTrue(true);
    }

    @Test
    public void temperatureMonitorTest() {
        AndroidHrwInfo androidHrwInfo = AndroidHrwInfo.getInstance();

        CPU cpu = androidHrwInfo.cpu();

        cpu.setOnTemperatureChangeListener(newTemp -> {
            System.out.format("Temp: [%.1f°]\n", newTemp);
        });

        cpu.startTemperatureMonitor();

        try {
            cdl.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cpu.stopTemperatureMonitor();
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

        cpu.getClusters();
    }

}
