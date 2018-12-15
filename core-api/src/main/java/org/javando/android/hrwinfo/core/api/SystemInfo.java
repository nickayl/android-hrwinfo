package org.javando.android.hrwinfo.core.api;

public interface SystemInfo {

     String getAndroidVersion();

     String getApiLevel();

     String getBootLoader();

     String getBuildId();

     String getJavaVM();

     String getOpenGL();

     String getKernelArchitecture();

     String getKernelVersion();

     String getRootAccess();

     String getSecurityPatchLevel();

     void setSystemUptimeMonitor(SystemUptimeMonitor monitor);
     void removeSystemUptimeMonitor();

     String formatSystemUptime(int days, int hours, int minutes, int seconds);

     @FunctionalInterface
     interface SystemUptimeMonitor {
         void onChange(int days, int hours, int minutes, int seconds);
     }

}
