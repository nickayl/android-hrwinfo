package org.javando.android.hrwinfo.core.api;

public interface Device {

    String getCommercialName();

    String getModel();

    String getBrand();

    String getHardwareCode();

    int getTotalRam();

    float getAvailableRamPercent();
    float getAvailableStoragePercent();

    int getAvailableRam();

    int getTotalStorage();

    int getAvailableStorage();

    String getScreenResolution();
    String getScreenDensityDpi();
    String getScreenSize();

    void setOnAvailableRamChangeListener(OnValueChangeListener listener);
    void setOnAvailableStorageChangeListener(OnValueChangeListener listener);

    void disableAvailableRamListener();
    void disableAvailableStorageListener();

    interface OnValueChangeListener {
        void onChange(Device device);
    }
}
