package org.javando.android.hrwinfo.core.impl;

import android.app.Activity;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import org.javando.android.hrwinfo.core.api.GPU;
import org.javando.android.hrwinfo.core.api.SystemInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Domenico on 03/10/2017.
 */

public class SystemInfoImpl implements SystemInfo {

    private static final String TAG = "SystemInfoError";
    private Activity activity;

    private String androidVersion;
    private String apiLevel;
    private String securityPatchLevel;
    private String bootLoader;
    private String buildId;
    private String javaVM;
    private String openGL;
    private String kernelArchitecture;
    private String kernelVersion;
    private String rootAccess;

    private SystemUptimeMonitor monitor;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private boolean systemUptimeMonitorActive = false;

    private GPU gpu;

    public SystemInfoImpl(Activity activity) {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null");

        this.activity = activity;
        this.gpu = AndroidHrwInfoImpl.getInstance().gpu();
        initialize();
    }


    private void initialize() {

        androidVersion = Build.VERSION.RELEASE;
      //  apiLevel = String.valueOf(Build.VERSION.SDK_INT);
        apiLevel = Build.VERSION.SDK;
        securityPatchLevel = retriveSecurityPatchLevel();
        bootLoader = Build.BOOTLOADER;
        buildId = Build.ID;
        javaVM = System.getProperty("java.vm.name") + " " +System.getProperty("java.vm.version");
        openGL = gpu.getOpenGLVersion().replace("OpenGL ES","");
        kernelArchitecture = System.getProperty("os.arch");
        kernelVersion = System.getProperty("os.name")+ " "+ System.getProperty("os.version");

        String rootCheck = activity.getString(R.string.no);
        try {
            String root = Utils.readSystemFile("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_cur_freq");
            Integer.valueOf(root);
            rootCheck = activity.getString(R.string.yes);
        } catch(Exception e) {
            e.printStackTrace();
        }

        rootAccess = rootCheck;
    }

    private String retriveSecurityPatchLevel() {
        String str = "";

        try {
            Process process = new ProcessBuilder()
                    .command("/system/bin/getprop")
                    .redirectErrorStream(true)
                    .start();

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                str += line + "\n";
                if(str.contains("security_patch")) {
                    String[] splitted = line.split(":");
                    if(splitted.length == 2) {
                        return splitted[1];
                    }
                    break;
                }
            }

            br.close();
            process.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public String getBootLoader() {
        return bootLoader;
    }

    public String getBuildId() {
        return buildId;
    }

    public String getJavaVM() {
        return javaVM;
    }

    public String getOpenGL() {
        return openGL;
    }

    public String getKernelArchitecture() {
        return kernelArchitecture;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getRootAccess() {
        return rootAccess;
    }

    public String getSecurityPatchLevel() {
        return securityPatchLevel;
    }

    @Override
    public void setSystemUptimeMonitor(SystemUptimeMonitor monitor) {
        enableSystemUptimeMonitor(monitor);
    }

    @Override
    public void removeSystemUptimeMonitor() {
        disableSystemUptimeMonitor();
    }

    @Override
    public String formatSystemUptime(int days, int hours, int minutes, int seconds) {

        String daysString = String.valueOf(days);
        String hoursString = String.valueOf(hours);
        String minutesString = String.valueOf(minutes);
        String secondsString = String.valueOf(Math.round(seconds));

        if (hoursString.length() == 1)
            hoursString = "0" + hoursString;

        if (minutesString.length() == 1)
            minutesString = "0" + minutesString;

        if (secondsString.length() == 1)
            secondsString = "0" + secondsString;

        String formattedUptime = String.format("%s:%s:%s", hoursString, minutesString, secondsString);

        if(!daysString.equals("0"))
            formattedUptime = daysString + " daysString " + formattedUptime;

        return formattedUptime;
    }

    // ===== private methods ====== //

    private void enableSystemUptimeMonitor(SystemUptimeMonitor monitor) {
        this.monitor = monitor;
        systemUptimeMonitorActive = true;

        executorService.submit(() -> {
            try {
                while(systemUptimeMonitorActive) {
                    long millis = SystemClock.elapsedRealtime();

                    double currentSeconds = millis / 1000f;
                    double divideByDays = currentSeconds / 86400;
                    double divideByHours = (divideByDays % 1) * 24;
                    double divideByMinutes = (divideByHours % 1) * 60;
                    double divideBySeconds = (divideByMinutes % 1) * 60;

                    this.monitor.onChange((int) divideByDays, (int) divideByHours, (int) divideByMinutes, (int) divideBySeconds);
                    Thread.sleep(1000);
                }
            } catch(InterruptedException e) {
                Log.e(TAG, "enableSystemUptimeMonitor: interrupted exception!" );
                e.printStackTrace();
            } catch(Exception e) {
                Log.d(TAG, "enableSystemUptimeMonitor: unknown error");
                e.printStackTrace();
            }
        });

    }

    private void disableSystemUptimeMonitor() {
        systemUptimeMonitorActive = false;
        this.monitor = null;
        executorService.shutdown();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SystemInfoImpl{");
        sb.append("\n activity=").append(activity);
        sb.append("\n,  androidVersion='").append(androidVersion).append('\'');
        sb.append("\n,  apiLevel='").append(apiLevel).append('\'');
        sb.append("\n,  securityPatchLevel='").append(securityPatchLevel).append('\'');
        sb.append("\n,  bootLoader='").append(bootLoader).append('\'');
        sb.append("\n,  buildId='").append(buildId).append('\'');
        sb.append("\n,  javaVM='").append(javaVM).append('\'');
        sb.append("\n,  openGL='").append(openGL).append('\'');
        sb.append("\n,  kernelArchitecture='").append(kernelArchitecture).append('\'');
        sb.append("\n,  kernelVersion='").append(kernelVersion).append('\'');
        sb.append("\n,  rootAccess='").append(rootAccess).append('\'');
        sb.append("\n,  monitor=").append(monitor);
        sb.append("\n,  executorService=").append(executorService);
        sb.append("\n,  systemUptimeMonitorActive=").append(systemUptimeMonitorActive);
        sb.append('}');
        return sb.toString();
    }
}

