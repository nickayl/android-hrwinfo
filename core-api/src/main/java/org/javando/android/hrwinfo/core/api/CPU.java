package org.javando.android.hrwinfo.core.api;

import java.util.List;

public interface CPU {

    String SCALING_GOVERNOR_UNKNOWN = "Uknown";

    List<Core> getCores();

    List<Cluster> getClusters();

    ProcessorInfo getProcessorInfo();

    int getNumCores();

    String getScalingGovernor();


    // ====== Public API ======= //
    void startCpuFrequencyMonitor(int precision);
    void startCpuFrequencyMonitor();

    void stopCpuFrequencyMonitor();

    void startTemperatureMonitor();
    void stopTemperatureMonitor();

    CPU setOnFrequencyChangeListener(CoreFrequencyChangeListener listener);
    CPU setOnTemperatureChangeListener(TemperatureChangeListener listener);

    interface CoreFrequencyChangeListener {
        void frequencyChanged(List<? extends Core> cores);
    }

    interface TemperatureChangeListener {
        void temperatureChanged(double newTemp);
    }

    interface Core {
        int getMinFrequency();
        int getMaxFrequency();
        int getCurFrequency();
        int getCoreNumber();
    }

    interface Cluster {
        int getNumberCores();
        double getMinFrequency();
        double getMaxFrequency();
    }

    interface ProcessorInfo {
        String getArchitecture();
        String getModelName();
        String getVendor();
    }

    class Constants {

        // ======= Unexpected conditions constants ====== //
        public static final String UNKNOWN_PROCESSOR_TYPE = "Unknown processor";
        public static final String ERROR_TAG = "CPUINFO_ERROR";

        // ====== Company names ======= //
        public static final String ARM_VENDOR = "ARM Holdings";
        public static final String INTEL_VENDOR = "Intel Corporation";

        // ====== ARM Model names ==== //
        public static final String CORTEX_A_53_PROCESSOR = "Cortex A-53 Processor";
        public static final String CORTEX_M_7_PROCESSOR = "Cortex-M7 Processor";
        public static final String CORTEX_M_4_PROCESSOR = "Cortex-M4 Processor";
        public static final String CORTEX_M_3_PROCESSOR =  "Cortex-M3 Processor";
        public static final String CORTEX_M_33_PROCESSOR =  "Cortex-M33 Processor";
        public static final String CORTEX_M_23_PROOCESSOR = "Cortex-M23 Proocessor";
        public static final String CORTEX_M_1_PROCESSOR =  "Cortex-M1 Processor";
        public static final String CORTEX_M_0_PROCESSOR =  "Cortex-M0 Processor";
        public static final String CORTEX_A_76_PROCESSOR = "Cortex-A76 Processor";
        public static final String CORTEX_A_75_PROCESSOR = "Cortex-A5 Processor";
        public static final String CORTEX_A_73_PROCESSOR = "Cortex-A73 Processor";
        public static final String CORTEX_A_72_PROCESSOR = "Cortex-A72 Processor";
        public static final String CORTEX_A_57_PROCESSOR = "Cortex-A57 Processor";
        public static final String CORTEX_A_55_PROCESSOR = "Cortex-A55 Processor";
        public static final String CORTEX_A_35_PROCESSOR = "Cortex-A35 Processor";
        public static final String CORTEX_A_32_PROCESSOR = "Cortex-A32 Processor";
        public static final String CORTEX_A_15_PROCESSOR = "Cortex-A15 Processor";
        public static final String CORTEX_A_9_PROCESSOR =  "Cortex-A9 Processor";
        public static final String CORTEX_A_8_PROCESSOR =  "Cortex-A8 Processor";
        public static final String CORTEX_A_7_MPCORE =     "Cortex-A7 MPCore";
        public static final String CORTEX_A_5_PROCESSOR =  "Cortex-A5 Processor";

        // ==== Architecture types ======== //
        public static final String ARCH_X86_64 = "X86_64";
        public static final String ARM8_64 = "ARMv8-A 64-bit";
        public static final String ARMV_7_A_32_BIT = "Armv7-A 32-bit";
        public static final String ARMV_7_A_WITH_NEON_AND_TRUST_ZONE = "Armv7-A 32-bit with NEON and TrustZone";
        public static final String ARMV_7_A_32_BIT_WITH_NEON_VFP = "Armv7-A 32-bit with NEON-VFP";
        public static final String ARMV_6_M = "ARMv6-M 32-bit";
        public static final String ARMV_8_M = "ARMv8-M 32-bit";
        public static final String ARMV_7_M = "ARMv7-M 32-bit";
        public static final String ARMV_8_2_A = "ARMv8.2-A 64-bit";
    }
}