package org.javando.android.hrwinfo.core.impl;

import android.util.Log;
import org.javando.android.hrwinfo.core.api.CPU;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CoreImpl implements CPU.Core {

    private static final String TAG = "ERROR_TAG";
    private int minFrequency =0;
    private int maxFrequency =0;
    private int coreNum=0;
    private int curFrequency=0;

    public CoreImpl() { }

    public CoreImpl(int coreNum) {
        this.coreNum = coreNum;

        File maxFreqFile = new File("/sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/cpuinfo_max_freq");
        File minFreqFile = new File("/sys/devices/system/cpu/cpu" + coreNum + "/cpufreq/cpuinfo_min_freq");

        try {
            BufferedReader br;

            if (maxFreqFile.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(maxFreqFile)));
                setMaxFrequency(br.readLine());
                br.close();
            }

            if(minFreqFile.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(minFreqFile)));
                setMinFrequency(br.readLine());
                br.close();
            }

        } catch(Exception e) {
            Log.e(TAG, "CoreImpl: Error while reading cpu min and max frequency");
            e.printStackTrace();
        }

    }

    @Override
    public int getMinFrequency() {
        return minFrequency;
    }

    @Override
    public int getMaxFrequency() {
        return maxFrequency;
    }

    @Override
    public int getCurFrequency() {
        return curFrequency;
    }

    protected void setCurFrequency(String curFrequency) {
        this.curFrequency = getNormalizedFrequency(curFrequency);
    }

    @Override
    public int getCoreNumber() {
        return coreNum;
    }

    protected void setMinFrequency(String minFrequency) {
        this.minFrequency = getNormalizedFrequency(minFrequency);
    }

    protected void setMaxFrequency(String maxFrequency) {
        this.maxFrequency = getNormalizedFrequency(maxFrequency);
    }

    private int getNormalizedFrequency(String frequency) {
        if (frequency != null && frequency.matches("[0-9]+"))
            return Integer.parseInt(frequency) / 1000;

        return 0;
    }

    public void setCoreNum(int coreNum) {
        this.coreNum = coreNum;
    }
}
