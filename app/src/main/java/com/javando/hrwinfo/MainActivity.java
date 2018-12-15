package com.javando.hrwinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import org.javando.android.hrwinfo.core.api.AndroidHrwInfo;
import org.javando.android.hrwinfo.core.api.CPU;
import org.javando.android.hrwinfo.core.api.Device;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Device device = AndroidHrwInfo.getInstance().device(this);
        Log.d(TAG, "onCreate: "+device.toString());


        AndroidHrwInfo.getInstance()
                .cpu()
                .setOnFrequencyChangeListener(cores -> {
                    String str = "[";
                    for(CPU.Core core: cores) {
                        str += String.format("#%d %d ",core.getCoreNumber(), core.getCurFrequency());
                    }
                    str += "] Mhz\n";
                    Log.d(TAG, str);
                })
                .startCpuFrequencyMonitor(1000);

        Log.d(TAG, "onCreate: ciaooooo");
    }
}
