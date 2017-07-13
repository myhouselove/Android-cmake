package wmy.jni.com.colorrecognize;


import android.util.Log;
import android.widget.ImageView;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.COLOR_BGR5652BGR;
import static org.opencv.imgproc.Imgproc.COLOR_RGBA2BGR;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.putText;

/**
 * Created by win7 on 2017/7/6 0006.
 */

public class OpenCVAlgorithmRecognizeColor extends OpenCVAlgorithm {
    private static String TAG = "OpenCVARC";

    static Scalar hsvRedLo =  new Scalar(0, 40, 40);
    static Scalar hsvRedHi =  new Scalar(10, 255, 255);

    static Scalar hsvOrangeLo =  new Scalar(11, 40, 40);
    static Scalar hsvOrangeHi =  new Scalar(25, 255, 255);

    static Scalar hsvYellowLo =  new Scalar(26, 40, 40);
    static Scalar hsvYellowHi =  new Scalar(34, 255, 255);

    static Scalar hsvGreenLo =  new Scalar(35, 40, 40);
    static Scalar hsvGreenHi =  new Scalar(77, 255, 255);

    static Scalar hsvcCyanLo =  new Scalar(78, 40, 40);
    static Scalar hsvCyanHi =  new Scalar(99, 255, 255);

    static Scalar hsvBlueLo =  new Scalar(100, 40, 40);
    static Scalar hsvBlueHi =  new Scalar(123, 255, 255);

    static Scalar hsvPurpleLo =  new Scalar(124, 40, 40);
    static Scalar hsvPurpleHi =  new Scalar(155, 255, 255);

    static Scalar hsvDeepRedLo =  new Scalar(156, 40, 40);
    static Scalar hsvDeepRedHi =  new Scalar(180, 255, 255);

    static ArrayList<Scalar> hsvLo = new ArrayList<Scalar>(){{add(hsvRedLo);add(hsvOrangeLo);add(hsvYellowLo); add(hsvGreenLo); add(hsvcCyanLo);add(hsvBlueLo);add(hsvPurpleLo);add(hsvDeepRedLo);}};

    static ArrayList<Scalar> hsvHi = new ArrayList<Scalar>(){{add(hsvRedHi);add(hsvOrangeHi);add(hsvYellowHi); add(hsvGreenHi); add(hsvCyanHi); add(hsvBlueHi);add(hsvPurpleHi);add(hsvDeepRedHi);}};


    private Mat src;
    private Mat hsv;
    private Mat dst;
    private Mat mid ;

    public Mat out;
    public int color_type=0;
    public OpenCVAlgorithmRecognizeColor(Mat insrc){
        src = new Mat();
        src = insrc;
        hsv = new Mat();
        dst = new Mat();
        out = new Mat();
        mid = new Mat();
    }
    public OpenCVAlgorithmRecognizeColor(String url){

    }
    public OpenCVAlgorithmRecognizeColor(){
        hsv = new Mat();
        dst = new Mat();
        out = new Mat();
        mid = new Mat();
    }
    @Override
    void AlgrithmInterface() {
        Log.d(TAG,"OpenCVAlgorithmRecognizeColor AlgrithmInterface");
        doAction();
    }

    @Override
    void AlgrithmInterface(Mat frame) {
        Log.d(TAG,"OpenCVAlgorithmRecognizeColor AlgrithmInterface mat frame");
        src = frame;
        doAction();
    }

    private void doAction() {
        Log.d(TAG,"OpenCVAlgorithmRecognizeColor doAction hsvLo.size()="+hsvLo.size());

        cvtColor(src,mid,COLOR_RGBA2BGR);
        cvtColor(mid,hsv,COLOR_BGR2HSV);


        for (int colorIdx = 0; colorIdx < hsvLo.size(); colorIdx++) {
            Core.inRange(hsv, hsvLo.get(colorIdx),hsvHi.get(colorIdx),dst);

            Mat imgThresholded1 = new Mat();
            // 转换成二值图
            Imgproc.threshold(dst,imgThresholded1,1,255,THRESH_BINARY);


            ArrayList<MatOfPoint> contours0 = new ArrayList<MatOfPoint> ();
            MatOfInt4 hierarchy = new MatOfInt4();
            Imgproc.findContours(imgThresholded1, contours0, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);



            for (int idx = 0; idx < contours0.size(); idx++)
            {
                double area = Imgproc.contourArea(contours0.get(idx));
             //   Log.d(TAG,"AREA IS"+area);
                if(area < 1000)
                    continue;
                Rect bound = Imgproc.boundingRect(contours0.get(idx));
                Point bc = new Point(bound.x + bound.width / 2,
                        bound.y + bound.height / 2);
                double []x = imgThresholded1.get((int)bc.y,(int)bc.x);
              //  Log.d(TAG,"size ="+x.length);
             //  Log.d(TAG,"1="+x[0]);
                if (x[0] > 0)
                {
                  //  Log.d (TAG,"THE COLOR IS"+ colorIdx+"size = "+contours0.size());
                    putText(src,getColor(colorIdx),bc,1,1f,new Scalar(0,0,0),1);
                    //color_type = colorIdx;
                    color_type = colorIdx;
                   // return 0;
                }
            }
        }
        out = src;
    }
    public  String getColor(int index){
        String str =  new String();
        switch(index){
            case 0:
                str = "红";
                break;
            case 1:
                str = "橙";
                break;
            case 2:
                str = "黄";
                break;
            case 3:
                str = "绿";
                break;
            case 4:
                str = "青";
                break;
            case 5:
                str = "蓝";
                break;
            case 6:
                str = "紫";
                break;
            case 7:
                str = "深红";
                break;
            default:
                break;
        }
        return str;
    }


}

class OpenCVRecgnizeColorAShape extends OpenCVAlgorithmRecognizeColor{

    public static String TAG = "OpenCVColorAShape";
    private Mat src;
    private Mat dst;
    private Mat mid;
    private Mat hsv;

   public OpenCVRecgnizeColorAShape(){
       src = new Mat();
       dst = new Mat();
       mid = new Mat();
       hsv = new Mat();
    }
    public OpenCVRecgnizeColorAShape(Mat insrc){

    }

    @Override
    void AlgrithmInterface() {
        super.AlgrithmInterface();
    }

    @Override
    void AlgrithmInterface(Mat frame) {
        super.AlgrithmInterface(frame);

        src = frame;
        doAction();

    }
    void doAction(){
        Log.d(TAG,"OpenCVAlgorithmRecognizeColorshape doAction hsvLo.size()="+hsvLo.size());

        cvtColor(src,mid,COLOR_RGBA2BGR);
        cvtColor(mid,hsv,COLOR_BGR2HSV);


        for (int colorIdx = 0; colorIdx < hsvLo.size(); colorIdx++) {
            Core.inRange(hsv, hsvLo.get(colorIdx),hsvHi.get(colorIdx),dst);

            Mat imgThresholded1 = new Mat();
            // 转换成二值图
            Imgproc.threshold(dst,imgThresholded1,1,255,THRESH_BINARY);


            ArrayList<MatOfPoint> contours0 = new ArrayList<MatOfPoint> ();
            MatOfInt4 hierarchy = new MatOfInt4();
            Imgproc.findContours(imgThresholded1, contours0, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);



            for (int idx = 0; idx < contours0.size(); idx++)
            {
                double area = Imgproc.contourArea(contours0.get(idx));
                Log.d(TAG,"AREA IS"+area);
                if(area < 1000)
                    continue;
                Rect bound = Imgproc.boundingRect(contours0.get(idx));
                Point bc = new Point(bound.x + bound.width / 2,
                        bound.y + bound.height / 2);
                double []x = imgThresholded1.get((int)bc.y,(int)bc.x);
                Log.d(TAG,"size ="+x.length);
                Log.d(TAG,"1="+x[0]);
                if (x[0] > 0)
                {
                    Log.d (TAG,"THE COLOR IS"+ colorIdx+"size = "+contours0.size());
                    putText(src,getColor(colorIdx),bc,1,1f,new Scalar(0,0,0),1);
                    //color_type = colorIdx;
                    color_type = colorIdx;
                    // return 0;
                }
            }
        }
        out = src;
    }
}
