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


        StringBuilder sb = new StringBuilder();

        AndroidHrwInfo.getInstance()
                .cpu()
                .setOnFrequencyChangeListener(cores -> {
                    sb.replace(0,sb.length(), "[");
                    for(CPU.Core core: cores) {
                       sb.append(String.format("#%d %d ",core.getCoreNumber(), core.getCurFrequency()));
                    }
                    sb.append("] Mhz\n");

                    Log.d(TAG, sb.toString());
                })
                .startCpuFrequencyMonitor(1000);

    }
}
