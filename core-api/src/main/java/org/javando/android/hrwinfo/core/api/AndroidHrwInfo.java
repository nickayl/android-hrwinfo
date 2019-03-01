package org.javando.android.hrwinfo.core.api;


import android.app.Activity;
import android.content.Context;

public abstract class AndroidHrwInfo {

    private static AndroidHrwInfo instance;

    public static AndroidHrwInfo getInstance() {
        if(instance == null) {

            try {
                Class klass = Class.forName("org.javando.android.hrwinfo.core.impl.AndroidHrwInfoImpl");
                instance = (AndroidHrwInfo) klass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Class AndroidHrwInfoImpl not found. Maybe you've forgotten to add an implementation dependency for this api?");
            }
        }
        return instance;
    }

    public abstract CPU cpu();

    public abstract Device device(Context context);

    public abstract GPU gpu();

    public abstract Battery battery(Activity activity);

    public abstract SystemInfo systemInfo(Activity activity);

    public abstract Sensors sensors(Activity activity);
}
