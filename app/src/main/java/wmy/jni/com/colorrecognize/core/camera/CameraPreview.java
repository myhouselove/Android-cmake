package wmy.jni.com.colorrecognize.core.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import wmy.jni.com.colorrecognize.core.utils.GlobalDef;


/**
 * Created by root on 3/7/17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{

    private String TAG = CameraPreview.class.getName();
    private Camera        mCamera;
    private SurfaceHolder mSfHolder;
    public byte[] mNV21Data = new byte[GlobalDef.RGB_WIDTH * GlobalDef.RGB_HEIGHT * 3 / 2];
    private boolean mIsDateGenerated = false;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        mSfHolder = getHolder();
        mSfHolder.addCallback(this);
        mSfHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        try{
//            mCamera.setPreviewDisplay(holder);
//            mCamera.startPreview();
//        }catch (Exception e){
//            Log.e(TAG, " Error setting camera preview : " + e.getMessage());
//        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        // preview surface does not exist
        if(mSfHolder.getSurface() == null){
            return;
        }

        // stop preview before making changes
        try{
            mCamera.stopPreview();
        }catch (Exception e){
            Log.e(TAG,  "try to stop a non-existent preview");
        }

        // set preview size and make any resize, rotate or reformatting changes here
        // start preview with new settings
        try{
            mCamera.setPreviewDisplay(mSfHolder);
            setPreviewCallbackWithBuffer();
        }catch (Exception e){
            Log.e(TAG, " Error starting camera preview : " + e.getMessage());
        }
    }

    public void setPreviewCallbackWithBuffer(){
        mCamera.addCallbackBuffer(mNV21Data);
        mCamera.setPreviewCallbackWithBuffer(this);
        mCamera.startPreview();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.v("xcy","onPreviewFrame");
        if(!mIsDateGenerated){
            mIsDateGenerated = true;
        }
        mNV21Data = data.clone();

        camera.addCallbackBuffer(mNV21Data);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean isDataGenerated(){
        return mIsDateGenerated;
    }
}
