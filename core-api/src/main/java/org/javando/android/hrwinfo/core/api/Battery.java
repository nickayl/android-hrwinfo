package org.javando.android.hrwinfo.core.api;

public interface Battery {

    String getHealth();
    String getPercentage();
    String getPlugged();
    String getChargingStatus();
    String getTechnology();
    String getTemperature();
    String getVoltage();
    String getCapacity();

    void setOnChangeEventListener(OnChangeEventListener listener);
    void removeOnChangeEventListener();

    interface OnChangeEventListener {
        void onChange(Battery battery);
    }
}
