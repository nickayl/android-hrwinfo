package org.javando.android.hrwinfo.core.api;

import com.javando.collections.api.ObservableUnit;

public interface GPU {

    String getVendor();

    String getModel();

    String getRenderer();

    ObservableUnit<Double> getFrequency();

    String getOpenGLVersion();

}
