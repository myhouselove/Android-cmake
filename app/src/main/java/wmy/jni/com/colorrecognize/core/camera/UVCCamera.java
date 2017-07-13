package wmy.jni.com.colorrecognize.core.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;


/**
 * Created by root on 3/7/17.
 */

public class UVCCamera{

    private String TAG = "UVCCamera";
    private Context mContext;

    private Camera            mCamera;
    public  CameraPreview     mCameraPreview;
    private Camera.Parameters mParams;


    public UVCCamera(Context context){

        mContext = context;
        mCamera = getCameraInstance();
        if(mCamera == null){
            Log.e(TAG, " mCamera is null");
            return;
        }

        mCameraPreview = new CameraPreview(mContext, mCamera);

        mCameraPreview.setPreviewCallbackWithBuffer();
    }

    private static Camera getCameraInstance(){
        Camera camera = null;
        try{
           Camera.getNumberOfCameras();
            camera = Camera.open(0);

        }catch (Exception e){
            Log.e("UVCCamera", "Camera is in use or does not exist");
        }
        return camera;
    }

    public void attachTo(FrameLayout layout){

        layout.addView(mCameraPreview);
    }



    public byte[] getNV21Data(){
        return mCameraPreview.mNV21Data;
    }

    public void release(){
        if(mCamera != null){
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
