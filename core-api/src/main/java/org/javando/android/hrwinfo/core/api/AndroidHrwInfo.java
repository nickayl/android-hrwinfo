package org.javando.android.hrwinfo.core.api;


import android.app.Activity;
import android.content.Context;

public abstract class AndroidHrwInfo {

    private static AndroidHrwInfo instance;

    static {

        try {
            Class klass = Class.forName("org.javando.android.hrwinfo.core.impl.AndroidHrwInfoImpl");
            instance = (AndroidHrwInfo) klass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class AndroidHrwInfoImpl not found. Maybe you've forgotten to add an implementation dependency for this api?");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("The provided AndroidHrwInfoImpl class does not have a public constructor, therefore cannot be instantiated. " +
                    "This is a sympthom of a malformed implementation API.");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Something gone wrong instantiating the AndroidHrwInfoImpl class. The cause is likely to be a malformed implementation API.");
        }
    }

    public static AndroidHrwInfo getInstance() {
        return instance;
    }

    public abstract CPU cpu();

    public abstract Device device(Context context);

    public abstract GPU gpu();

    public abstract Battery battery(Activity activity);

    public abstract SystemInfo systemInfo(Activity activity);

    public abstract Sensors sensors(Activity activity);
}
