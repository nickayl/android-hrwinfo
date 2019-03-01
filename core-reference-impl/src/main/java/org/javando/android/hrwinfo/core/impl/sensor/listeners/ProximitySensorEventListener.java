package org.javando.android.hrwinfo.core.impl.sensor.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import org.javando.android.hrwinfo.core.api.SensorListener;

import java.util.Locale;

/**
 * Created by Domenico on 13/10/2017.
 */

public class ProximitySensorEventListener implements SensorListener {

    private long lastUpdate = System.nanoTime();

    private SensorListener.OnValueChangeListener mProximityListener;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mProximityListener == null)
            return ;

        long curtime = System.nanoTime() / 1000000000;
        long lasttime = lastUpdate / 1000000000;

        if(curtime - lasttime < 1)
            return ;
        else
            lastUpdate = System.nanoTime();

        Sensor sensor = event.sensor;

        String values = String.format(Locale.getDefault(), "%.2f cm", event.values[0]);
      //  Log.d("SENSOR-INFO", String.format("proxumity sensor change: %s", values));

        if(mProximityListener != null)
        mProximityListener.onValueChange(values, sensor);

    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        Log.d("Sensor-DEBUG", String.format(Locale.getDefault(), "Light accuracy change: %s: %d", arg0.getName(), arg1));
    }


    @Override
    public int getSensorType() {
        return Sensor.TYPE_PROXIMITY;
    }

    @Override
    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mProximityListener = listener;
    }
}
