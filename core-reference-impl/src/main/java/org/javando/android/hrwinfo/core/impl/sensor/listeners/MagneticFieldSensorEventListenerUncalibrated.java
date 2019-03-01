package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;

/**
 * Created by Domenico on 13/10/2017.
 */

public class MagneticFieldSensorEventListenerUncalibrated extends MagneticFieldSensorEventListener {

    @Override
    public int getSensorType() {
        return Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED;
    }
}
