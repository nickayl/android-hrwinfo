package org.javando.android.hrwinfo.core.impl;

import android.content.Context;
import android.graphics.Typeface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Domenico on 05/10/2017.
 */

public class Utils {
    public static final String SYSTEM_UPTIME_LISTENER = "SYSTEM_UPTIME_CURRENT";
    public static final String LISTENER_CPU_TEMP = "LISTENER_CPU_TEMP";

    public static final String LISTENER_GPU_LOAD = "LISTENER_GPU_LOAD";
    public static final String LISTENER_CPU_LOAD = "LISTENER_CPU_LOAD";

    public static final String LISTENER_BATTERY_INFO = "LISTENER_BATTERY_INFO";

    public static final String LISTENER_SENSOR = "LISTENER_SENSOR";

    public static final int SISTEMINFO_UPTIME_CHANGE_LISTENER = 1;
    public static final int CPU_LOAD_CHANGE_LISTENER = 2;
    public static final int ADRENO_GPU_LOAD_CHANGE_LISTENER = 3;

    public static final String UNKNOWN = "Unknown";


    public static boolean inArray(Object value, Object[] array) {
        for(Object o : array)
            if(o.equals(value))
                return true;

        return false;
    }

    public static String readSystemFile(String command, String addToLine, LineExecutor code) {
        StringBuilder str = new StringBuilder();
        try {

            Process process = Runtime.getRuntime().exec(command);
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null) {
                str.append(code.execute(line) + addToLine);
            }

            in.close();
            br.close();
            //  process.destroy();
            process = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str.toString();
    }

    public static String readSystemFile(String command) {
        return readSystemFile(command, "", new LineExecutor<String>() {
            @Override
            public String execute(String line) {
                return line;
            }
        });
    }

    public static String readSystemFile(String command, LineExecutor executor) {
        return readSystemFile(command, "",executor);
    }



    public static Typeface getGlobalTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Regular.ttf");
    }


    public interface LineExecutor<T> {
        T execute(String line);
    }

    public static String capitalize(String str) {
        if(str != null && str.length() > 1)
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);

        return str;
    }
}
