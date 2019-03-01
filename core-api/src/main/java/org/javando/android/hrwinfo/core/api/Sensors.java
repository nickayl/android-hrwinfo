package org.javando.android.hrwinfo.core.api;

import java.util.List;

public interface Sensors {

    void registerListeners();

    void unregisterListeners();

    List<SensorListener> getSensorEventListeners();
}
