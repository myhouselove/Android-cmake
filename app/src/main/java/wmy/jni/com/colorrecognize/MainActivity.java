package wmy.jni.com.colorrecognize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.




    public static String TAG="MainActivity";
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView txt;

    public static MainActivity mInstance;
    private CameraBridgeViewBase mOpenCvCameraView;
    public OpenCVBase ob;
    public OpenCVContext occ;
    public OpenCVContext ocs;
    public OpenCVAlgorithmRecognizeColor oarc;

    public OpenCVAlgorithmRecognizeShape oars;


    private SeekBar mSeekBarSelf;
    int i = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



//        mInstance = this;
//        ob = new OpenCVBase(this,getResources());
//
//        oarc = new OpenCVAlgorithmRecognizeColor();
//        occ = new OpenCVContext(oarc);
//
//        oars = new OpenCVAlgorithmRecognizeShape();
//        ocs = new OpenCVContext(oars);
//
//       //a ocs.doAction();
//
//
//        txt = (TextView)findViewById(R.id.tips);
//        img1 = (ImageView)findViewById(R.id.img1);
//        img2 = (ImageView)findViewById(R.id.img2);
//        img3 = (ImageView)findViewById(R.id.img3);
//       // img.setImageBitmap(ob.mat2Bitmap(oars.out));
//
//        mSeekBarSelf = (SeekBar) findViewById(R.id.seekBar);
//        mSeekBarSelf.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                oars.g_threshold = i;
//                Log.d(TAG,"VALUE="+i);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//
//
//        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
//
//        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
//
//        mOpenCvCameraView.setCvCameraViewListener(this);
//     //   mOpenCvCameraView.set
//        mOpenCvCameraView.enableView();

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!OpenCVLoader.initDebug()){// 默认加载opencv_java.so库
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//if(i==10) {
        Log.d(TAG,"FRAME");
    occ.doFrame(inputFrame.rgba());
        ocs.doFrame(inputFrame.rgba());

//}

        MainActivity.mInstance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                img1.setImageBitmap(ob.mat2Bitmap(oars.out));
//                img2.setImageBitmap(ob.mat2Bitmap(oars.out1));
//                img3.setImageBitmap(ob.mat2Bitmap(oars.out2));
                MainActivity.mInstance.txt.setText(oarc.getColor(oarc.color_type)+"色的"+oars.getShape(oars.shape_type));
            }
        });
        return null;
        //return inputFrame.rgba();
    }
}
