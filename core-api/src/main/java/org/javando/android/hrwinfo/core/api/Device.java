package org.javando.android.hrwinfo.core.api;

import com.javando.collections.api.ObservableUnit;

public interface Device {

    String getCommercialName();

    String getModel();

    String getBrand();

    String getHardwareCode();

    int getTotalRam();

    ObservableUnit<Integer> getAvailableRam();

    int getTotalStorage();

    ObservableUnit<Double> getAvailableStorage();


    String getScreenResolution();
    String getScreenDensityDpi();
    String getScreenSize();
}
