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
}
