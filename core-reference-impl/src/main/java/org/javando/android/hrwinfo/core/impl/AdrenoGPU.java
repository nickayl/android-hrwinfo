package org.javando.android.hrwinfo.core.impl;

import android.app.Activity;
import org.javando.android.hrwinfo.core.api.GPU;

import java.util.Locale;

/**
 * Created by Domenico on 08/10/2017.
 */

class AdrenoGPU extends GPUImpl implements GPU {

    private String currentLoadPercent;
    private String maxClock;

    public AdrenoGPU(Activity activity)  {
        super();
    }

    public String getCurrentLoadPercent() {
        String values = Utils.readSystemFile("cat sys/class/kgsl/kgsl-3d0/gpubusy");

        if(values.length() == 0)
            return Utils.UNKNOWN;

        try {
            String[] splitted = values.split("[ ]+");

            float num1 = Float.valueOf(splitted[1]);
            float num2 = Float.valueOf(splitted[2]);
            float percent = (num1 / num2) * 100;

            if (num2 == 0)
                percent = 0;

            currentLoadPercent = String.format(Locale.getDefault(), "%.0f", percent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentLoadPercent;
    }


    public String getMaxClock() {
        if(maxClock != null)
            return maxClock;

        try {
            String maxGpuClock = Utils.readSystemFile("cat sys/class/kgsl/kgsl-3d0/max_gpuclk");

            if (maxGpuClock.length() == 0)
                return Utils.UNKNOWN;

            Integer gpuClk = Integer.valueOf(maxGpuClock);
            float num = gpuClk / 1000000;

            maxClock = String.format(Locale.getDefault(), "%.0f MHz", num);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxClock;
    }

    /*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        ArrayList<TextTitleValuePair> list = super.getTitleValuePair();

        if(!getMaxClock().equals(Utils.UNKNOWN))
            list.add(new StaticTextTitleValuePair("Max GPU Clock", getMaxClock()));

        if(!getCurrentLoadPercent().equals(Utils.UNKNOWN)) {
            VariableTextTitleValuePair vtp = new VariableTextTitleValuePair("GPU Load", getCurrentLoadPercent() + "%", Utils.LISTENER_GPU_LOAD);
            list.add(vtp);
            setOnLoadChangeListener(vtp);
        }

        return list;
    } */
}
