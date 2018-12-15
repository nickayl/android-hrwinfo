package org.javando.android.hrwinfo.core.impl;

import org.javando.android.hrwinfo.core.api.CPU;

public class ClusterImpl implements CPU.Cluster {

    private int numberCores;
    private int minFrequency;
    private int maxFrequency;


    public ClusterImpl() {

    }

    public ClusterImpl(int numberCores, int minFrequency, int maxFrequency) {
        this.numberCores = numberCores;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
    }


    @Override
    public int getNumberCores() {
        return numberCores;
    }

    @Override
    public double getMinFrequency() {
        return minFrequency;
    }

    @Override
    public double getMaxFrequency() {
        return maxFrequency;
    }
}
