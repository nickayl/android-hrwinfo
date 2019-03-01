package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import org.javando.android.hrwinfo.core.api.SensorListener;

import java.util.Locale;

/**
 * Created by Domenico on 13/10/2017.
 */

public class PressureSensorEventListener implements SensorListener {

    private long lastUpdate = System.nanoTime();

    private SensorListener.OnValueChangeListener mPressureListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mPressureListener == null)
            return ;

        long curtime = System.nanoTime() / 1000000000;
        long lasttime = lastUpdate / 1000000000;

        if(curtime - lasttime < 1)
            return ;
        else
            lastUpdate = System.nanoTime();

        Sensor sensor = event.sensor;

        String values = String.format(Locale.getDefault(), "%.2f millibar", event.values[0]);
     //   Log.d("SENSOR-INFO", String.format("pressure sensor change: %s", values));

        if(mPressureListener != null)
        mPressureListener.onValueChange(values, sensor);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d("Sensor-DEBUG", String.format(Locale.getDefault(), "pressure accuracy change: %s: %d", arg0.getName(), arg1));
    }



    @Override
    public int getSensorType() {
        return Sensor.TYPE_PRESSURE;
    }

    @Override
    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mPressureListener = listener;
    }
}
