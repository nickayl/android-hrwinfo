package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import org.javando.android.hrwinfo.core.api.SensorListener;

import java.util.Locale;

/**
 * Created by Domenico on 13/10/2017.
 */

public class AmbientTemperatureSensorEventListener implements SensorListener {

    private long lastUpdate = System.nanoTime();

    private SensorListener.OnValueChangeListener mAmbientTemperatureListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mAmbientTemperatureListener == null)
            return;

        long curtime = System.nanoTime() / 1000000000;
        long lasttime = lastUpdate / 1000000000;

        if (curtime - lasttime < 1)
            return;
        else
            lastUpdate = System.nanoTime();

        Sensor sensor = event.sensor;

        String values = String.format(Locale.getDefault(), "%.2f °C", event.values[0]);
        //    Log.d("SENSOR-INFO", String.format("ambient temperature change: %s", values));

        if (mAmbientTemperatureListener != null)
            mAmbientTemperatureListener.onValueChange(values, sensor);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d("Sensor-DEBUG", String.format(Locale.getDefault(), "ambient temperature change: %s: %d", arg0.getName(), arg1));
    }


    @Override
    public int getSensorType() {
        return Sensor.TYPE_AMBIENT_TEMPERATURE;
    }

    @Override
    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mAmbientTemperatureListener = listener;
    }
}
