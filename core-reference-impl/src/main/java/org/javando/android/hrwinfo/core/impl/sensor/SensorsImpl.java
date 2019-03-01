package org.javando.android.hrwinfo.core.impl.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;

import org.javando.android.hrwinfo.core.api.SensorListener;
import org.javando.android.hrwinfo.core.api.Sensors;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.AccelerometerSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.AmbientTemperatureSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.GameRotationVectorSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.GyroscopeSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.GyroscopeUncalibratedSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.LightSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.LinearAccelerationSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.MagneticFieldSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.MagneticFieldSensorEventListenerUncalibrated;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.OrientationSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.PressureSensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.ProximitySensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.RelativeHumiditySensorEventListener;
import org.javando.android.hrwinfo.core.impl.sensor.listeners.RotationVectorSensorEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Domenico on 10/10/2017.
 */

public class SensorsImpl implements Sensors {

    private final SensorManager mSensorManager;
    private Activity activity;

    private List<SensorListener> sensorEventListeners;

    public SensorsImpl(Activity activity) throws IllegalArgumentException {
        if (activity == null)
            throw new IllegalArgumentException("Activity cannot be null on Sensor class");

        this.activity = activity;
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        sensorEventListeners = new ArrayList<>(4);
        sensorEventListeners.addAll(Arrays.asList(
                new AccelerometerSensorEventListener(),
                new AmbientTemperatureSensorEventListener(),
                new GyroscopeSensorEventListener(),
                new MagneticFieldSensorEventListener(),
                new MagneticFieldSensorEventListenerUncalibrated(),
                new LightSensorEventListener(),
                new PressureSensorEventListener(),
                new ProximitySensorEventListener(),
                new OrientationSensorEventListener(),
                new RelativeHumiditySensorEventListener(),
                new LinearAccelerationSensorEventListener(),
                new RotationVectorSensorEventListener(),
                new GameRotationVectorSensorEventListener(),
                new GyroscopeUncalibratedSensorEventListener()
        ));

        //  List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);
    }

    public void registerListeners() {
        ArrayList<SensorListener> toRemove = new ArrayList<SensorListener>();
        for (SensorListener sensorListener : sensorEventListeners) {
            boolean res = mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(sensorListener.getSensorType()), SensorManager.SENSOR_DELAY_NORMAL);
            if(!res)
                toRemove.add(sensorListener);
        }
        sensorEventListeners.removeAll(toRemove);
    }

    public void unregisterListeners() {
        for (SensorListener sensorListener : sensorEventListeners)
            mSensorManager.unregisterListener(sensorListener);
    }

    public List<SensorListener> getSensorEventListeners() {
        return sensorEventListeners;
    }

    //    @Override
//    public ArrayList<TextTitleValuePair> getTitleValuePair() {
//
//        ArrayList<TextTitleValuePair> pairs = new ArrayList<>();
//        List<Sensor> mList= mSensorManager.getSensorList(Sensor.TYPE_ALL);
//
//        for (int i = 0; i < mList.size(); i++) {
//            Sensor s = mList.get(i);
//         //   Log.d("Sensor-INFO", String.format(Locale.getDefault(), "Sensor name: %s, \nVendor: %s, \nVersion: \n%s, Resolution: %s\n\n",s.getName(),s.getVendor(), s.getVersion(), s.getResolution()));
//            pairs.add(new StaticTextTitleValuePair("Sensor #"+i, s.getName()));
//            String tm = "™";
//            if(s.getVendor().equalsIgnoreCase("aosp"))
//                tm = "";
//            pairs.add(new StaticTextTitleValuePair("   Vendor ", s.getVendor() + tm));
//            pairs.add(new StaticTextTitleValuePair("   Version ", String.valueOf(s.getVersion())));
//
//            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("    Values", "", s.getName());
//            pairs.add(vtp);
//
//            for(SensorListener sensorListener : sensorEventListeners) {
//                if(s.getType() == sensorListener.getSensorType()) {
//                    sensorListener.setOnValueChangeListener(vtp);
//                    break;
//                }
//            }
//        }
//        return pairs;
//    }
}
