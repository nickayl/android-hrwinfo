package org.javando.android.hrwinfo.core.api;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

/**
 * Created by Domenico on 13/10/2017.
 */

public interface SensorListener extends SensorEventListener {

    /**
     *
     * @return returns the sensor type. One of the Sensor.TYPE_* types.
     */
    int getSensorType();
    void setOnValueChangeListener(OnValueChangeListener listener);

    interface OnValueChangeListener {
        void onValueChange(String values, Sensor sensor);
    }
}
