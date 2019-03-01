package org.javando.android.hrwinfo.core.impl;

import android.opengl.*;
import android.util.Log;
import org.javando.android.hrwinfo.core.api.GPU;
import org.javando.android.hrwinfo.core.api.NotImplementedException;

/**
 * Created by Domenico on 08/10/2017.
 */

public class GPUImpl implements GPU {

 //   protected Activity activity;

    private String renderer;
    private String vendor;
    private String openGLVersion;

    public GPUImpl() {
        initialize();
    }

    protected void initialize() {

        try {
            EGLDisplay dpy = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            int[] vers = new int[2];
            EGL14.eglInitialize(dpy, vers, 0, vers, 1);

            int[] configAttr = {
                    EGL14.EGL_COLOR_BUFFER_TYPE, EGL14.EGL_RGB_BUFFER,
                    EGL14.EGL_LEVEL, 0,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfig = new int[1];
            EGL14.eglChooseConfig(dpy, configAttr, 0,
                    configs, 0, 1, numConfig, 0);
            if (numConfig[0] == 0) {
                // TROUBLE! No config found.
            }

            int[] surfAttr = {
                    EGL14.EGL_WIDTH, 64,
                    EGL14.EGL_HEIGHT, 64,
                    EGL14.EGL_NONE
            };
            EGLSurface surf = EGL14.eglCreatePbufferSurface(dpy, configs[0], surfAttr, 0);

            int[] ctxAttrib = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };
            EGLContext ctx = EGL14.eglCreateContext(dpy, configs[0], EGL14.EGL_NO_CONTEXT, ctxAttrib, 0);
            EGL14.eglMakeCurrent(dpy, surf, surf, ctx);

            Log.d("GL", "GL_RENDERER = " + GLES20.glGetString(GLES20.GL_RENDERER));
            Log.d("GL", "GL_VENDOR = " + GLES20.glGetString(GLES20.GL_VENDOR));
            Log.d("GL", "GL_VERSION = " + GLES20.glGetString(GLES20.GL_VERSION));
            Log.i("GL", "GL_EXTENSIONS = " + GLES20.glGetString(GLES20.GL_EXTENSIONS));


            this.renderer = GLES20.glGetString(GLES20.GL_RENDERER);
            this.vendor = GLES20.glGetString(GLES20.GL_VENDOR);
            this.openGLVersion = GLES20.glGetString(GLES20.GL_VERSION);

        } catch(NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getOpenGLVersion() {
        return openGLVersion;
    }

    @Override
    public String getVendor() {
        return vendor;
    }

    @Override
    public String getRenderer() {
        return renderer;
    }


    // TODO: GPU interface methods to be implemented. ( getModel and getFrequency )
    @Override
    public String getModel() {
        throw new NotImplementedException("getModel() method of GPU interface has not been implemented yet.");
    }

}
