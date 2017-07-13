package wmy.jni.com.colorrecognize.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;
import android.widget.FrameLayout;



import java.io.ByteArrayOutputStream;

import wmy.jni.com.colorrecognize.core.camera.UVCCamera;
import wmy.jni.com.colorrecognize.core.permission.PermissionCallbacks;
import wmy.jni.com.colorrecognize.core.permission.PermissionGrant;
import wmy.jni.com.colorrecognize.core.view.DepthViewer;
import wmy.jni.com.colorrecognize.core.view.RGBViewer;


/**
 * Created by OrbbecSystem on 2017/4/13.
 */

public class DataEngine {

    private String TAG = "DataEngine";
    private Thread mDataThread;
    private UVCCamera mUVCCamera;

    private PermissionGrant mUSBPermission = null;
    private Context mContext;
    private int mDepthWidth;
    private int mDepthHeight;
    private int mImageWidth;
    private int mImageHeight;
    private boolean mIsUVC = false;

    private Bitmap mDepthBitmap = null;
    private Bitmap mRGBBitmap   = null;

    private FrameLayout mDepthPanel;
    private FrameLayout mRGBPanel;
    private FrameLayout mUVCPanel;
    private DepthViewer mDepthViewer;
    private RGBViewer   mRGBViewer;

    public int autoFlag = 0;


    private Bitmap m_colorBitmap;
    private boolean mExit = false;


    public DataEngine(Context context, int depthWidth, int depthHeight, int imageWidth, int imageHeight){

        mContext = context;
        mDepthWidth  = depthWidth;
        mDepthHeight = depthHeight;
        mImageWidth  = imageWidth;
        mImageHeight = imageHeight;

        getUSBPermission(mContext);
        initBitmap();



    }

    public void setDisplayPanels(FrameLayout depthPanel, FrameLayout rgbPanel, FrameLayout uvcPanel){
        mDepthPanel = depthPanel;
        mRGBPanel   = rgbPanel;
        mUVCPanel   = uvcPanel;

        mDepthViewer = new DepthViewer(mContext);
        mRGBViewer   = new RGBViewer(mContext);

        mDepthPanel.addView(mDepthViewer);
        mRGBPanel.addView(mRGBViewer);
    }

    private void initBitmap(){
        mDepthBitmap = Bitmap.createBitmap(mDepthWidth, mDepthHeight, Bitmap.Config.ARGB_8888);
        mRGBBitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        m_colorBitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
    }




    public void initDataThread(){
        mDataThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(!mExit){


                    NativeMethod.Update();

                   // NativeMethod.GetDepthBitmap(mDepthBitmap);
                   // NativeMethod.GetRGBBitmap(mRGBBitmap);
                   // updatePanels(mDepthBitmap, mRGBBitmap);
                }
            }
        });
        mDataThread.start();
    }




    public void getUSBPermission(final Context context){
        mUSBPermission = null;
        mUSBPermission = new PermissionGrant(context, new PermissionCallbacks() {
            @Override
            public void onDevicePermissionGranted(boolean isUVC) {

                Log.v(TAG, "isUVC " + isUVC);
                mIsUVC   = isUVC;
                NativeMethod.Init(isUVC, mDepthWidth, mDepthHeight, mImageWidth, mImageHeight);
                if(mIsUVC){
                    mUVCCamera = new UVCCamera(mContext);
                    mUVCCamera.attachTo(mUVCPanel);
                }
                initDataThread();
            }

            @Override
            public void onDevicePermissionDenied() {

            }

            @Override
            public void onDeviceNotFound() {

            }
        });
    }

    private void updatePanels(Bitmap depthBitmap, Bitmap rgbBitmap){
        if(mDepthPanel != null && mDepthViewer != null){
            mDepthViewer.beginDraw();
            mDepthViewer.drawDepthBitmap(depthBitmap);
            mDepthViewer.endDraw();
        }

        if(mRGBPanel != null && mRGBViewer != null){
            mRGBViewer.beginDraw();
            mRGBViewer.drawRGBBitmap(rgbBitmap);
            mRGBViewer.endDraw();
        }
    }

    public void close(){
        mExit = true;
        if(mDataThread != null){
            try {
                mDataThread.join();
                mDataThread.interrupt();
                mDataThread = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(mIsUVC && mUVCCamera != null){
            mUVCCamera.release();
        }
    }
}
