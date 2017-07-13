package wmy.jni.com.colorrecognize.core;

import android.graphics.Bitmap;

/**
 * Created by Lin_JX on 2015/7/27.
 */
public class NativeMethod {

    static{
        System.loadLibrary("native-lib");
    }

    public static native int   Init(boolean isUVC, int depthWidth, int depthHeight, int imageWidth, int imageHeight);
    public native static int   GetDepthBitmap(Bitmap bitmap);
    public native static int   GetRGBBitmap(Bitmap bitmap);
    public native static void  ReleaseSensor();
    public native static int   Update();

    public native String stringFromJNI();




}
