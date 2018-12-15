package org.javando.android.hrwinfo.core.impl;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Locale;

/**
 * Created by Domenico on 28/09/2017.
 */

public class Screen {

    private String resolution;
    private String densityDpi;
    private String sizeInInches;

    private int resolutionX;
    private int resolutionY;

    private Context context;

    public Screen(Context a) {
        this.context = a;
        this.calculate();
    }

    public Screen(String resolution, String densityDpi, String sizeInInches, Context a) {
        this.resolution = resolution;
        this.densityDpi = densityDpi;
        this.sizeInInches = sizeInInches;

        this.context = a;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getDensityDpi() {
        return densityDpi;
    }

    public void setDensityDpi(String densityDpi) {
        this.densityDpi = densityDpi;
    }

    public String getSizeInInches() {
        return sizeInInches;
    }

    public void setSizeInInches(String sizeInInches) {
        this.sizeInInches = sizeInInches;
    }

    private void calculate() {
        Point size = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealSize(size);
        setResolution(size.y + "x" + size.x);
        resolutionX = size.x;
        resolutionY = size.y;

        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(size.x/dm.xdpi,2);
        double y = Math.pow(size.y/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        setSizeInInches(String.format(Locale.getDefault(),"%.2f", screenInches)+ '"');

        double screenDPI = Math.sqrt( Math.pow(size.x,2) + Math.pow(size.y,2) )/screenInches;
        String dpi = String.format(Locale.getDefault(),"%.0f dpi",(float)Math.round(screenDPI));
        setDensityDpi(dpi);
    }

    /*
    @Override
    public ArrayList<TextTitleValuePair> getTitleValuePair() {
        ArrayList<TextTitleValuePair> list = new ArrayList<>();

        list.add(new StaticTextTitleValuePair("Screen Resolution", resolution));
        list.add(new StaticTextTitleValuePair("Screen Density", densityDpi));
        list.add(new StaticTextTitleValuePair("Screen Size", sizeInInches));

        return list;
    } */

    public int getResolutionX() {
        return resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }
}
