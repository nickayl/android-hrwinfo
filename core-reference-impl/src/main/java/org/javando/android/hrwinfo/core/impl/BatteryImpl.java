package org.javando.android.hrwinfo.core.impl;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;
import org.javando.android.hrwinfo.core.api.Battery;

/**
 * Created by Domenico on 10/10/2017.
 */

public class BatteryImpl extends BroadcastReceiver implements Battery {

    private IntentFilter intentFilter;
    private Activity activity;

    private String health;
    private String percentage;
    private String plugged;
    private String charging_status;
    private String technology;
    private String temperature;
    private String voltage;
    private String capacity;

    private OnChangeEventListener listener;


//    String[] codes = new String[] { "health", "charge_percentage", "plugged_state", "charging_status", "technology", "temperature", "voltage", "capacity" };
//    String[] titles = new String[] { "Health", "Remaining Charge", "Plugged State", "Charging Status", "Technology", "Temperature", "Voltage", "Capacity" };
//    String[] values = new String[] { health, percentage, plugged, charging_status, technology, temperature, voltage, capacity };


    public BatteryImpl(Activity activity) throws IllegalArgumentException {
        if(activity == null)
            throw new IllegalArgumentException("Activity cannot be null on BatteryImpl class.");

        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        updateBatteryData(intent);
    }

    public IntentFilter getIntentFilter() {
        return intentFilter;
    }

    private void updateBatteryData(Intent intent) {
        Log.d("BatteryImpl-INFO", "Receiving BatteryImpl info");

        boolean present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);

        if (present) {
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int healthLbl = -1;

            switch (health) {
                case BatteryManager.BATTERY_HEALTH_COLD:
                    healthLbl = R.string.battery_health_cold;
                    break;

                case BatteryManager.BATTERY_HEALTH_DEAD:
                    healthLbl = R.string.battery_health_dead;
                    break;

                case BatteryManager.BATTERY_HEALTH_GOOD:
                    healthLbl = R.string.battery_health_good;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    healthLbl = R.string.battery_health_over_voltage;
                    break;

                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    healthLbl = R.string.battery_health_overheat;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    healthLbl = R.string.battery_health_unspecified_failure;
                    break;

                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    healthLbl = R.string.battery_health_unknown;
                    break;

                default:
                    break;
            }

            if (healthLbl != -1) {
                // display battery health ...
                this.health = activity.getString(healthLbl);
            }

            // Calculate BatteryImpl Percentage
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            if (level != -1 && scale != -1) {
                int batteryPct = (int) ((level / (float) scale) * 100f);
                this.percentage = batteryPct + "%";
            }

            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            int pluggedLbl;

            switch (plugged) {
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    pluggedLbl = R.string.battery_plugged_wireless;
                    break;

                case BatteryManager.BATTERY_PLUGGED_USB:
                    pluggedLbl = R.string.battery_plugged_usb;
                    break;

                case BatteryManager.BATTERY_PLUGGED_AC:
                    pluggedLbl = R.string.battery_plugged_ac;
                    break;

                default:
                    pluggedLbl = R.string.battery_plugged_none;
                    break;
            }

            // display plugged status ...
            this.plugged = activity.getString(pluggedLbl);

            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int statusLbl;

            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    statusLbl = R.string.battery_status_charging;
                    break;

                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    statusLbl = R.string.battery_status_discharging;
                    break;

                case BatteryManager.BATTERY_STATUS_FULL:
                    statusLbl = R.string.battery_status_full;
                    break;

                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    statusLbl = R.string.battery_status_unknown;
                    break;

                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    statusLbl = R.string.battery_status_not_charging;
                    break;
                default:
                    statusLbl = R.string.battery_status_discharging;
                    break;
            }

            if (statusLbl != -1) {
                this.charging_status = activity.getString(statusLbl);
            }

            if (intent.getExtras() != null) {
                String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);

                if (!"".equals(technology)) {
                    this.technology = technology;
                }
            }

            int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

            if (temperature > 0) {
                float temp = ((float) temperature) / 10f;
                this.temperature = temp + " °C";
            }

            int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);

            if (voltage > 0) {
                this.voltage = voltage +" mV";
            }

            double capacity = getBatteryCapacity(activity);

            if (capacity > 0) {
                this.capacity = capacity + " mAh";
            } else
                this.capacity = Utils.UNKNOWN;

        //    this.values = new String[] { this.health, percentage, this.plugged, charging_status, technology, this.temperature, this.voltage, this.capacity };

//            for(int i=0; i < titles.length; i++) {
//                Log.d("BatteryImpl-info", "Updating "+codes[i]+" : "+values[i]);
////                if(this.onValueChangeListener.get(i) != null) {
////                    this.onValueChangeListener.get(i).onValueChange(values[i], Utils.LISTENER_BATTERY_INFO + "_" + codes[i]);
////                    pairs.get(i).setValue(values[i]);
////                }
//            }

            if(listener != null)
                listener.onChange(this);

        } else {
            Toast.makeText(activity, "No BatteryImpl present", Toast.LENGTH_SHORT).show();
        }

    }


    /*public long getBatteryCapacity_old(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Integer capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if(chargeCounter == Integer.MIN_VALUE || capacity == Integer.MIN_VALUE)
                return 0;

            //long value = (long) (((float) chargeCounter / (float) capacity) * 100f);
            return (chargeCounter/capacity) *100;
        }
        return 0;
    } */
    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    @Override
    public String toString() {
        return "BatteryImpl{" +
                "intentFilter=" + intentFilter +
                ", activity=" + activity +
                ", health='" + health + '\'' +
                ", percentage='" + percentage + '\'' +
                ", plugged='" + plugged + '\'' +
                ", charging_status='" + charging_status + '\'' +
                ", technology='" + technology + '\'' +
                ", temperature='" + temperature + '\'' +
                ", voltage='" + voltage + '\'' +
                ", capacity='" + capacity + '\'' +
//                ", codes=" + Arrays.toString(codes) +
//                ", titles=" + Arrays.toString(titles) +
//                ", values=" + Arrays.toString(values) +
                '}';
    }

    public String getHealth() {
        return health;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getPlugged() {
        return plugged;
    }

    public String getChargingStatus() {
        return charging_status;
    }

    public String getTechnology() {
        return technology;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getVoltage() {
        return voltage;
    }

    public String getCapacity() {
        return capacity;
    }

    @Override
    public void setOnChangeEventListener(OnChangeEventListener listener) {
        this.listener = listener;

        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        this.activity.registerReceiver(this, intentFilter);
        listener.onChange(this);
    }

    @Override
    public void removeOnChangeEventListener() {
        this.activity.unregisterReceiver(this);
        this.listener = null;
    }
}
