package org.javando.android.hrwinfo.core.impl;

import android.os.Build;
import android.util.Log;

import org.javando.android.hrwinfo.core.api.CPU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARCH_X86_64;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARM8_64;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_6_M;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_7_A_32_BIT;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_7_A_32_BIT_WITH_NEON_VFP;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_7_A_WITH_NEON_AND_TRUST_ZONE;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_7_M;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_8_2_A;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARMV_8_M;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ARM_VENDOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_15_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_32_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_35_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_53_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_55_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_57_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_5_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_72_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_73_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_75_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_76_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_7_MPCORE;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_8_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_A_9_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_0_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_1_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_23_PROOCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_33_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_3_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_4_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.CORTEX_M_7_PROCESSOR;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.ERROR_TAG;
import static org.javando.android.hrwinfo.core.api.CPU.Constants.INTEL_VENDOR;


public class CPUImpl implements CPU {

    public static final String DEBUG_TAG = "DEBUG_TAG";
    private static final String INFO_TAG = "cpu_INFO";

    // ========= private fields ========= //
    private static CPU instance;
    private int numCores;

    private List<CoreImpl> cores = new ArrayList<>();
    private List<Cluster> clusters;
    private String scalingGovernor = SCALING_GOVERNOR_UNKNOWN;

    private File[] cpufreqFileArray;

    private ExecutorService cpuFreqExecutorService;
    private ExecutorService cpuTempExecutorService;

    private boolean cpuFreqMonitorActive = false;
    private boolean cpuTempMonitorActive = false;

    private CoreFrequencyChangeListener freqChangeListener;
    private TemperatureChangeListener tempChangeListener;
    private int defaultPrecision = 500;
    private String catCommand;

    private int minFrequency = Integer.MAX_VALUE;
    private int maxFrequency = Integer.MIN_VALUE;

    private float averageMaxFreq = 0;
    private float averageMinFreq = 0;

    static CPU getInstance() {
        if (instance == null)
            instance = new CPUImpl();

        return instance;
    }

    private CPUImpl() {
        initialize();
    }

    // ====== Public API ======= //
    @Override
    public void startCpuFrequencyMonitor(int precision) {
        enableFrequencyMonitor(precision);
    }

    @Override
    public void startCpuFrequencyMonitor() {
        if (freqChangeListener == null) {
            throw new IllegalStateException("You must add a frequencyChangeListener before starting the cpuFrequencyMonitor");
        }
        enableFrequencyMonitor(defaultPrecision);
    }

    @Override
    public void stopCpuFrequencyMonitor() {
        disableFrequencyMonitor();
    }

    @Override
    public void startTemperatureMonitor() {
        cpuTempMonitorActive = true;
        calculateTemperature();
    }

    @Override
    public void stopTemperatureMonitor() {
        cpuTempMonitorActive = false;
        cpuTempExecutorService.shutdown();
    }

    @Override
    public List<Core> getCores() {
        return Collections.unmodifiableList(cores);
    }

    @Override
    public int getMinFrequency() {
        if (minFrequency != Integer.MAX_VALUE)
            return minFrequency;

        for (Core c : cores) {
            if (c.getMinFrequency() == 0)
                continue;
            minFrequency = Math.min(c.getMinFrequency(), minFrequency);
        }

        return minFrequency;
    }

    @Override
    public int getMaxFrequency() {
        if (maxFrequency != Integer.MIN_VALUE)
            return maxFrequency;

        for (Core c : cores) {
            maxFrequency = Math.max(c.getMaxFrequency(), maxFrequency);
        }

        return maxFrequency;
    }

    @Override
    public float getAverageMinimumFrequency() {
        if (averageMinFreq == 0) {
            for (Core c : cores) {
                averageMinFreq += c.getMinFrequency();
            }
            averageMinFreq /= numCores;
        }

        return averageMinFreq;
    }

    @Override
    public float getAverageMaximumFrequency() {
        if (averageMaxFreq == 0) {
            for (Core c : cores) {
                averageMaxFreq += c.getMaxFrequency();
            }
            averageMaxFreq /= numCores;
        }

        return averageMaxFreq;
    }

    @Override
    public List<Cluster> getClusters() {
        return Collections.unmodifiableList(clusters);
    }

    @Override
    public CPU setOnFrequencyChangeListener(CoreFrequencyChangeListener listener) {
        this.freqChangeListener = listener;
        return this;
    }

    @Override
    public CPU setOnTemperatureChangeListener(TemperatureChangeListener listener) {
        tempChangeListener = listener;
        return this;
    }

    @Override
    public int getNumCores() {
        return numCores;
    }


    @Override
    public String getScalingGovernor() {
        File f = new File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor");

        if (!f.exists())
            this.scalingGovernor = SCALING_GOVERNOR_UNKNOWN;

        try (FileInputStream fis = new FileInputStream(f);
             InputStreamReader is = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(is)) {

            String line = br.readLine();

            return line != null ? line : SCALING_GOVERNOR_UNKNOWN;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.scalingGovernor;
    }

    @Override
    public CPU.ProcessorInfo getProcessorInfo() {

        ProcessorInfo procInfo = new ProcessorInfo();

        File file = new File("/proc/cpuinfo");
        if (file.exists()) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

                char[] buffer = new char[500];
                StringBuilder finalString = new StringBuilder();
                int charsRead;

                while ((charsRead = br.read(buffer)) >= 0) {
                    finalString.append(buffer, 0, charsRead);
                }

                String[] processorsInfo = finalString.toString().split("\n\n");
                List<String[]> processorsInfoLines = new ArrayList<>();

                for (String s : processorsInfo) {
                    processorsInfoLines.add(s.split("\n"));
                }

                for (String[] processorInfo : processorsInfoLines) {
                    for (String line : processorInfo) {

                        // ========  Check if it is an Intel processor ======= //
                        if (line.contains("GenuineIntel")) {
                            procInfo.setVendor(INTEL_VENDOR);
                            procInfo.setArchitecture(ARCH_X86_64);
                        } else if (line.contains("model name")) {
                            String model_name = line.split(":")[1];
                            procInfo.setModelName(model_name);
                        }
                        // ======= End check ============================== //


                        if (line.toLowerCase().contains("cpu part")) {
                            String part = line.split(":")[1];
                            resolveARMProcessorType(part, procInfo);
                        }
                        // ========== End check =====================//
                    }
                }

// ====== Check if it is an ARM Processor ====== //
//                        if(line.contains("CPU implementer\t: 0x41")) {
//                            processorBrand = "ARM Holdings";
//                            processorArch = SystemInfo.getProperty("os.arch");
//                        }
            } catch (FileNotFoundException e) {
                Log.w(ERROR_TAG, "getType: /proc/cpuinfo file not found");
            } catch (IOException e) {
                Log.e(ERROR_TAG, "getType: error while reading /proc/cpuinfo");
                e.printStackTrace();
            }
        }

        return procInfo;

    }

    // ======== private dirty methods =========== //

    private void initialize() {

        // Let's discover how much cores do it have this device
        this.numCores = calculateNumCores();

        try {
            cpufreqFileArray = new File[numCores];
            StringBuilder sb = new StringBuilder("cat ");

            for (int i = 0; i < this.numCores; i++) {
                cpufreqFileArray[i] = new File("/sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_cur_freq");
                sb.append(cpufreqFileArray[i].getAbsolutePath()).append(" ");
                CoreImpl core = new CoreImpl(i);
                cores.add(core);
            }

            this.clusters = calculateClusters();
            catCommand = sb.toString().trim();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Cluster> calculateClusters() {
        this.clusters = new ArrayList<>();

        int num = 0, prevMin = 0, prevMax = 0;

        for (Core core : cores) {
            if (core.getMinFrequency() == 0) {
                num++;
                continue;
            }
            num++;
            if (core.getMinFrequency() > prevMin && core.getMaxFrequency() > prevMax) {
                prevMin = core.getMinFrequency();
                prevMax = core.getMaxFrequency();
                clusters.add(new ClusterImpl(num, prevMin, prevMax));
                num=0;
            } else {
                int index = clusters.size() - 1;
                if (index >= 0)
                    clusters.get(index).setNumberCores(num+1);
            }

        }

        return clusters;
    }

    private void enableFrequencyMonitor(int precision) {
        cpuFreqMonitorActive = true;
        cpuFreqExecutorService = Executors.newFixedThreadPool(1);

        cpuFreqExecutorService.submit(() -> {
            int i;
            long startTime = System.currentTimeMillis();
            while (cpuFreqMonitorActive) {

                for (i = 0; i < numCores; i++) {
                    if (cpufreqFileArray[i].exists()) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cpufreqFileArray[i])))) {
                            cores.get(i).setCurFrequency(br.readLine().trim());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                freqChangeListener.frequencyChanged(cores);
                try {
                    Thread.sleep(precision);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (System.currentTimeMillis() - startTime >= 8000) { // Free up memory
                    System.gc();
                    startTime = System.currentTimeMillis();
                    Log.i(INFO_TAG, "Suggesting gc invocation");
                }
            }

            Log.d(DEBUG_TAG, "enableFrequencyMonitor: Stopping CPU Frequency Monitor");
        });
    }

    private void disableFrequencyMonitor() {
        cpuFreqMonitorActive = false;
        cpuFreqExecutorService.shutdown();
    }

    private void calculateTemperature() {
        File f = new File("/sys/class/thermal/thermal_zone0/temp");

        cpuTempExecutorService = Executors.newSingleThreadExecutor();

        if (f.exists()) {
            cpuTempExecutorService.submit(() -> {
                //    String line;
                Double value;
                while (cpuTempMonitorActive) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)))) {
                        value = Double.valueOf(br.readLine());

                        if (value > 1000)
                            value /= 1000;

                        tempChangeListener.temperatureChanged(value);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(ERROR_TAG, "calculateTemperature: error while calculating temperature inside custom thread");
                        stopTemperatureMonitor();
                    }
                }
            });
        }

        Log.d(DEBUG_TAG, "calculateTemperature: END MONITORING");
    }

    private int calculateNumCores() {
        int numCores = 0;

        while (true) {
            File f = new File("/sys/devices/system/cpu/cpu" + numCores++);
            if (!f.exists() || numCores > 24) // if something goes wrong, stop it at the 24th iteration (after all, what type of mobile cpu has more than 24 cores? )
                break;
        }
        return --numCores;
    }

    private void resolveARMProcessorType(String part, ProcessorInfo processorInfo) {

        String modelName;
        String arch;

        switch (part.toLowerCase().trim()) {

            case "0xc0f":
                modelName = CORTEX_A_15_PROCESSOR;
                arch = ARMV_7_A_32_BIT_WITH_NEON_VFP;
                break;

            case "0xc09":
                modelName = CORTEX_A_9_PROCESSOR;
                arch = ARMV_7_A_32_BIT;
                break;

            case "0xc08":
                modelName = CORTEX_A_8_PROCESSOR;
                arch = ARMV_7_A_WITH_NEON_AND_TRUST_ZONE;
                break;

            case "0xc07":
                modelName = CORTEX_A_7_MPCORE;
                arch = ARMV_7_A_32_BIT;
                break;

            case "0xc05":
                modelName = CORTEX_A_5_PROCESSOR;
                arch = ARMV_7_A_32_BIT;
                break;

            case "0xc20":
                modelName = CORTEX_M_0_PROCESSOR;
                arch = ARMV_6_M;
                break;

            case "0xd20":
                modelName = CORTEX_M_23_PROOCESSOR;
                arch = ARMV_8_M;
                break;

            case "0xc21":
                modelName = CORTEX_M_1_PROCESSOR;
                arch = ARMV_6_M;
                break;

            case "0xc23":
                modelName = CORTEX_M_3_PROCESSOR;
                arch = ARMV_7_M;
                break;

            case "0xc24":
                modelName = CORTEX_M_4_PROCESSOR;
                arch = ARMV_7_M;
                break;

            case "0xc27":
                modelName = CORTEX_M_7_PROCESSOR;
                arch = ARMV_7_M;
                break;

            case "0xd01":
                modelName = CORTEX_A_32_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd03":
                modelName = CORTEX_A_53_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd04":
                modelName = CORTEX_A_35_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd05":
                modelName = CORTEX_A_55_PROCESSOR;
                arch = ARMV_8_2_A;
                break;

            case "0xd07":
                modelName = CORTEX_A_57_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd08":
                modelName = CORTEX_A_72_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd09":
                modelName = CORTEX_A_73_PROCESSOR;
                arch = ARM8_64;
                break;

            case "0xd0a":
                modelName = CORTEX_A_75_PROCESSOR;
                arch = ARMV_8_2_A;
                break;

            case "0xd0b":
                modelName = CORTEX_A_76_PROCESSOR;
                arch = ARMV_8_2_A;
                break;

            case "0xd21":
                modelName = CORTEX_M_33_PROCESSOR;
                arch = ARMV_8_M;
                break;

            default:
                String armVersion;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    armVersion = Build.SUPPORTED_ABIS[0];
                } else {
                    armVersion = Build.CPU_ABI;
                }
                modelName = armVersion.replace("armeabi-", "");
                arch = "Unknown";
        }

        modelName = "ARM " + modelName;

        processorInfo.setModelName(modelName);
        processorInfo.setVendor(ARM_VENDOR);
        processorInfo.setArchitecture(arch);
    }

    private class ProcessorInfo implements CPU.ProcessorInfo {

        private String architecture;
        private String modelName;
        private String vendor;

        private ProcessorInfo() {
        }

        private ProcessorInfo(String architecture, String modelName, String vendor) {
            this.architecture = architecture;
            this.modelName = modelName;
            this.vendor = vendor;
        }

        @Override
        public String getArchitecture() {
            return architecture;
        }

        public void setArchitecture(String architecture) {
            this.architecture = architecture;
        }

        @Override
        public String getModelName() {
            return modelName;
        }

        public void setModelName(String modelName) {
            this.modelName = modelName;
        }

        @Override
        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }
    }

    public String getCatCommandForCpuFreq() {
        return catCommand;
    }
}
