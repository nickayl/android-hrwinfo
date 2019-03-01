package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by Domenico on 13/10/2017.
 */

public class GyroscopeUncalibratedSensorEventListener extends GyroscopeSensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        super.onSensorChanged(event);

     //  Log.d("SENSOR-INFO", "Gyroscope uncalibrated");
    }

    @Override
    public int getSensorType() {
        return Sensor.TYPE_GYROSCOPE_UNCALIBRATED;
    }


}
