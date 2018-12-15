package org.javando.android.hrwinfo.core.impl;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import com.javando.collections.api.ObservableCollections;
import com.javando.collections.api.ObservableUnit;
import org.javando.android.hrwinfo.core.api.Device;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Domenico on 28/09/2017.
 */

public class DeviceImpl implements Device {

    private final Screen screen;
    private String model;
    private String brand;
    private String board;
    private int totalRam;
    private String availRamPercent;
    private int totalStorage;
    private ObservableUnit<Integer> availRam;
    private ObservableUnit<Double> availableStorage;

    private String availStoragePercent;
    private String commercialName;
    private String hardware;

    private Context context;

    public DeviceImpl(Context context) {
        this.context = context;
        availableStorage = ObservableCollections.getObservableUnit();
        availRam = ObservableCollections.getObservableUnit();
        screen = new Screen(context);
        calculate();
    }

    private void calculate() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)  context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        long availableMegs = mi.availMem / 0x100000L;
        long totalMegs = mi.totalMem / 0x100000L;

        //Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double)mi.totalMem * 100.0;
        availRamPercent = String.format("%.2f",percentAvail);

        availRam.setValue((int) availableMegs);
        totalRam = (int) totalMegs;

        model = Build.MODEL;
        brand = Utils.capitalize(Build.BRAND.toLowerCase())+"™";
        board = Build.BOARD;
        hardware = Build.HARDWARE;

        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        double bytesTotal = stat.getBlockSizeLong() * stat.getBlockCountLong();
        double megTotal = bytesTotal / 1048576;

        double bytesAvailable = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
        double megAvailable = bytesAvailable / 1048576;

        totalStorage = (int) megTotal;
        availableStorage.setValue(megAvailable); // MB value

        availStoragePercent = String.format("%.2f", (megAvailable/ megTotal) * 100.0);
    }

    public String getCommercialName() {
        if(commercialName != null)
            return commercialName;

        try {
            InputStream is = context.getAssets().open("" +
                    "supported_devices.txt");
           // InputStream is = getClass().getClassLoader().getResourceAsStream("supported_devices.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;

            while((line = br.readLine()) != null) {
                String[] splitted = line.split(",");

                if(splitted.length == 4) {
                    String retailBranding = splitted[0];
                    String marketingName = splitted[1];
                    String model = splitted[3];

                    if (model.equalsIgnoreCase(this.model) && retailBranding.equalsIgnoreCase(Build.BRAND))
                        return commercialName = retailBranding + "™ " + marketingName;
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
    public ObservableUnit<Integer> getAvailableRam() {
        return availRam;
    }

    public String getAvailRamPercent() {
        return availRamPercent;
    }

    public int getTotalStorage() {
        return totalStorage;
    }

    public ObservableUnit<Double> getAvailableStorage() {
        return availableStorage;
    }

    public String getAvailStoragePercent() {
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
        sb.append(", availRam=").append(availRam.getValue()).append("\n");
        sb.append(", availableStorage=").append(availableStorage.getValue()).append("\n");
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
