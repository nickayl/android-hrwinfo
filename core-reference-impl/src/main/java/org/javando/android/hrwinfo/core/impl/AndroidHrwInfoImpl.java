package org.javando.android.hrwinfo.core.impl;


import android.app.Activity;
import android.content.Context;
import org.javando.android.hrwinfo.core.api.*;
import org.javando.android.hrwinfo.core.impl.sensor.SensorsImpl;

public class AndroidHrwInfoImpl extends AndroidHrwInfo {

    private SystemInfo systemInfo;
    private GPU gpu;
    private Device device;
    private CPU cpu;
    private Battery battery;
    private Sensors sensors;

    @Override
    public CPU cpu() {
        if(cpu == null)
            cpu = CPUImpl.getInstance();

        return cpu;
    }

    @Override
    public Device device(Context context) {
        if(device == null)
            device = new DeviceImpl(context);

        return device;
    }

    @Override
    public GPU gpu() {
        if(gpu == null)
            gpu = new GPUImpl();

        return gpu;
    }

    @Override
    public Battery battery(Activity activity) {
        if(battery == null)
            battery = new BatteryImpl(activity);

        return battery;
    }

    @Override
    public SystemInfo systemInfo(Activity activity) {
        if(systemInfo == null)
            systemInfo = new SystemInfoImpl(activity);

        return systemInfo;
    }

    @Override
    public Sensors sensors(Activity activity) {
        if(sensors == null)
            sensors = new SensorsImpl(activity);

        return sensors;
    }

}
