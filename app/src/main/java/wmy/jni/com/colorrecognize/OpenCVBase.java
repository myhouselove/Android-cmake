package wmy.jni.com.colorrecognize;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

/**
 * Created by win7 on 2017/7/6 0006.
 */

public class OpenCVBase {
    public static String TAG = "OpenCVBase";
    private Context mCtx;
    private Resources mRes;
    private Mat mat_output;
    private Bitmap bitmap_output;

    public OpenCVBase(){


    }



    public OpenCVBase(Context context, Resources res){
        initOpenCVContext();
        if(context != null)
         mCtx = context;
        if(res != null)
         mRes = res;
    }
    private void initOpenCVContext(){
        if (!OpenCVLoader.initDebug()){// 默认加载opencv_java.so库
            Log.d(TAG,"Loading OpenCV so!");
        }
        mat_output = new Mat();

    }


    public Mat picture2mat(int pic_id){
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
      //  Bitmap srcBitmap = BitmapFactory.decodeResource(mRes, pic_id);
        Bitmap srcBitmap = BitmapFactory.decodeResource(mRes,pic_id,opts);
        if(srcBitmap == null){
            Log.d(TAG,"BMP IS NULL");
        }
        Utils.bitmapToMat(srcBitmap, mat_output);//convert original bitmap to Mat, R G B.

        return mat_output;
    }

    public Bitmap mat2Bitmap( Mat src){

        bitmap_output = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(src, bitmap_output);
        return bitmap_output;
    }

}
