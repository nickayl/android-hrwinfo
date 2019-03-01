package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by Domenico on 13/10/2017.
 */

public class GameRotationVectorSensorEventListener extends RotationVectorSensorEventListener {

    @Override
    public void onSensorChanged(SensorEvent event) {
        super.onSensorChanged(event);

     //   Log.d("SENSOR-INFO", "GAME ROTATION VECTOR");
    }

    @Override
    public int getSensorType() {
        return Sensor.TYPE_GAME_ROTATION_VECTOR;
    }
}
