package org.javando.android.hrwinfo.core.impl;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import org.javando.android.hrwinfo.core.api.Device;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Domenico on 28/09/2017.
 */

public class DeviceImpl implements Device {

    private final Screen screen;
    private final ActivityManager activityManager;
    private Context context;

    private String commercialName;
    private String hardware;
    private String model;
    private String brand;
    private String board;

    private float availRamPercent;
    private float availStoragePercent;

    private int totalRam;
    private int availRam = 0;

    private int totalStorage;
    private int availableStorage = 0;

    private OnValueChangeListener availRamListener;
    private OnValueChangeListener availStorageListener;


    private ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();

    private static final Timer timer = new Timer();

    private TimerTask timerTask;

    private boolean isTimerEnabled = false;

    public DeviceImpl(Context context) {
        this.context = context;
        screen = new Screen(context);

        activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);


        model = Build.MODEL;
        brand = Utils.capitalize(Build.BRAND.toLowerCase());//+"™";
        board = Build.BOARD;
        hardware = Build.HARDWARE;

        calculateRam();
        calculateStorage();
    }

    private void enableListener() {
        if (isTimerEnabled)
            return;

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (availStorageListener == null && availRamListener == null) {
                    disableTimerTask();
                    return;
                }

                if (availRamListener != null) {
                    calculateRam();
                    availRamListener.onChange(DeviceImpl.this);
                }

                if (availStorageListener != null) {
                    calculateStorage();
                    availStorageListener.onChange(DeviceImpl.this);
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);

        isTimerEnabled = true;
    }

    @SuppressLint("DefaultLocale")
    private void calculateRam() {
        activityManager.getMemoryInfo(mi);

        double availableMegs = (double) mi.availMem / 0x100000L;
        double totalMegs = (double) mi.totalMem / 0x100000L;

        availRam = (int) availableMegs;
        totalRam = (int) totalMegs;

        //Percentage can be calculated for API 16+
        availRamPercent = (float) (availableMegs / totalMegs);

    }

    @SuppressLint("DefaultLocale")
    private void calculateStorage() {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        double bytesTotal = stat.getBlockSizeLong() * stat.getBlockCountLong();
        double megTotal = bytesTotal / 1048576;

        double bytesAvailable = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        double megAvailable = bytesAvailable / 1048576;

        totalStorage = (int) megTotal;
        availableStorage = (int) megAvailable; // MB value

        availStoragePercent = ((float) (megAvailable / megTotal) * 100.0f);
    }

    @Override
    public void disableAvailableRamListener() {
        this.availRamListener = null;
    }

    @Override
    public void disableAvailableStorageListener() {
        this.availStorageListener = null;
    }

    @Override
    public void setOnAvailableRamChangeListener(OnValueChangeListener listener) {
        availRamListener = listener;
        enableListener();
    }

    @Override
    public void setOnAvailableStorageChangeListener(OnValueChangeListener listener) {
        availStorageListener = listener;
        enableListener();
    }

    public void disableTimerTask() {
        timerTask.cancel();
        timer.purge();
        isTimerEnabled = false;
        System.out.println("Disabling Ram and Storage Timertask ...");
    }

    public String getCommercialName() {
        if (commercialName != null)
            return commercialName;

        try {
            InputStream is = context.getAssets().open("supported_devices.txt");
            // InputStream is = getClass().getClassLoader().getResourceAsStream("supported_devices.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;

            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(",");

                if (splitted.length == 4) {
                    String retailBranding = splitted[0];
                    String marketingName = splitted[1];
                    String model = splitted[3];

                    if (model.equalsIgnoreCase(this.model) && retailBranding.equalsIgnoreCase(Build.BRAND)) {
                        if (!marketingName.toLowerCase().contains(getBrand().toLowerCase()))
                            return commercialName = retailBranding + " " + marketingName;
                        else
                            return commercialName = marketingName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getBrand() {
        return brand;
    }

    @Override
    public String getHardwareCode() {
        return board;
    }

    @Override
    public int getTotalRam() {
        return totalRam;
    }

    @Override
    public int getAvailableRam() {
        return availRam;
    }

    public int getTotalStorage() {
        return totalStorage;
    }

    public int getAvailableStorage() {
        return availableStorage;
    }

    @Override
    public float getAvailableRamPercent() {
        return availRamPercent;
    }

    @Override
    public float getAvailableStoragePercent() {
        return availStoragePercent;
    }

    public String getBoardCode() {
        return board;
    }

    @Override
    public String getScreenDensityDpi() {
        return screen.getDensityDpi();
    }

    @Override
    public String getScreenSize() {
        return screen.getSizeInInches();
    }

    @Override
    public String getScreenResolution() {
        return screen.getResolution();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeviceImpl{");
        sb.append("screen=").append(screen).append("\n");
        sb.append(", model='").append(model).append('\'').append("\n");
        sb.append(", brand='").append(brand).append('\'').append("\n");
        sb.append(", board='").append(board).append('\'').append("\n");
        sb.append(", totalRam=").append(totalRam).append("\n");
        sb.append(", availRamPercent='").append(availRamPercent).append('\'').append("\n");
        sb.append(", totalStorage=").append(totalStorage).append("\n");
        sb.append(", availRam=").append(availRam).append("\n");
        sb.append(", availableStorage=").append(availableStorage).append("\n");
        sb.append(", availStoragePercent='").append(availStoragePercent).append('\'').append("\n");
        sb.append(", commercialName='").append(commercialName).append('\'').append("\n");
        sb.append(", hardware='").append(hardware).append('\'').append("\n");
        sb.append(", context=").append(context).append("\n");
        sb.append("Screen resolution: ").append(getScreenResolution()).append("\n");
        sb.append("Screen density: ").append(getScreenDensityDpi()).append("\n");
        sb.append("Screen size: ").append(getScreenSize()).append("\n");
        sb.append('}');
        return sb.toString();
    }
}
