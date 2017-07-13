package wmy.jni.com.colorrecognize;

import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_NONE;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGR;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGR555;
import static org.opencv.imgproc.Imgproc.COLOR_RGBA2BGR;
import static org.opencv.imgproc.Imgproc.COLOR_RGBA2GRAY;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.RETR_EXTERNAL;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.approxPolyDP;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.drawContours;
import static org.opencv.imgproc.Imgproc.isContourConvex;
import static org.opencv.imgproc.Imgproc.threshold;

/**
 * Created by win7 on 2017/7/10 0010.
 */

public class OpenCVAlgorithmRecognizeShape extends OpenCVAlgorithm {

    private Mat src;
    private Mat mid;
    private Mat dst;
    public Mat out;
    public Mat out1;
    public Mat out2;
    public Mat tmp ;
    public int shape_type = 0;

    public int g_threshold = 150;
    private static String TAG = "OpenCVRecognizeShape";

    //Algorithm parameter
    private static double epsilon = 15.0f;

    OpenCVAlgorithmRecognizeShape(){
        src = new Mat();
       // src = insrc;
        dst = new Mat();
        out = new Mat();
        out1 = new Mat();
        out2 = new Mat();
        mid = new Mat();
        tmp = new Mat();
    }

    OpenCVAlgorithmRecognizeShape(Mat insrc){
        src = new Mat();
        src = insrc;
        dst = new Mat();
        out = new Mat();
        out1 = new Mat();
        out2 = new Mat();
        mid = new Mat();
        tmp = new Mat();

    }

    @Override
    void AlgrithmInterface() {
        doAction();
    }



    @Override
    void AlgrithmInterface(Mat frame) {
        src = frame;
        doAction();
    }

public static double threshold = 1;


    private int doAction() {
       if(src == null){
           Log.d(TAG,"DO SRC NULL");
           return -1;
       }
       // cvtColor(src,mid,COLOR_GRAY2BGR);
        cvtColor(src,mid,COLOR_RGBA2GRAY);
        //Mat tmp = new Mat();

       // Canny(mid,tmp,threshold,255);
        mid.convertTo(out,CvType.CV_8UC1);
       // tmp =mid;
        threshold(mid,tmp,150,255,THRESH_BINARY|THRESH_OTSU);
        //Imgproc.Canny(mid,tmp,g_threshold,g_threshold*3);
        tmp.convertTo(out1,CvType.CV_8UC1);


       Mat white = new Mat(new Size(tmp.width(),tmp.height()),tmp.depth(),new Scalar(255));
       Core.subtract(white,tmp,tmp);
        Log.d(TAG,"tmp2="+tmp.get(33,33)[0]);
        //ut1=tmp;
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint> ();
        MatOfInt4 hierarchy = new MatOfInt4();


        Imgproc.findContours(tmp, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE);
Log.d(TAG,"SIZE = "+contours.size());
        for (int contourIdx = 0; contourIdx < contours.size(); contourIdx++) {
            Imgproc.drawContours(tmp, contours, contourIdx, new Scalar(0, 0, 255), -1);
        }


        Log.d(TAG,"SIZE =2");
        tmp.convertTo(out2,CvType.CV_8UC1);
        //ArrayList<MatOfPoint> approxPoint = new ArrayList<MatOfPoint> (contours.size());
        ArrayList<MatOfPoint> approxPoint = new ArrayList<MatOfPoint> (contours.size());
        double maxCosine = 0;
       // int idx = 0;

      //  Scalar color = new Scalar(100f, 100f, 100f);

       // drawContours(out,contours,1,color,-1);
        Log.d(TAG,"SIZE = 3");
        for (int index = 0; index < contours.size(); index++)
        {
            MatOfPoint2f dst1 = new MatOfPoint2f();
            contours.get(index).convertTo(dst1, CvType.CV_32F);

            Log.d(TAG,"dst1 size = "+dst1.toArray().length);

            MatOfPoint2f dst2 = new MatOfPoint2f();

            approxPolyDP(dst1,dst2,epsilon,true);
            Log.d(TAG,"dst2 size = "+dst2.toArray().length);

            MatOfPoint ds = new MatOfPoint();
            dst2.convertTo(ds,CvType.CV_8UC1);

            Log.d(TAG,"index = "+index+"list = "+approxPoint.size());
            approxPoint.add(index,ds);
           // Log.d(TAG,"approxPoint list = "+approxPoint.get(index).);

            MatOfPoint approxs = approxPoint.get(index);
            approxs.convertTo(approxs,CvType.CV_32S);


Log.d(TAG,"length="+approxs.toArray().length+"cols="+approxs.cols()+"arear = "+Math.abs(contourArea(approxs))+"bool = "+isContourConvex(approxs));

            if (Math.abs(contourArea(approxs)) > 10){// && isContourConvex(approxs)) {//当正方形面积在此范围内……，如果有因面积过大或过小漏检正方形问题，调整此范围
                switch (approxs.toArray().length) {
                    case 3:
                        maxCosine = 0;
                        for (int j = 2; j < 4; j++) {
                            double cosine = Math.abs(angle(approxs.toArray()[j%3], approxs.toArray()[j - 2], approxs.toArray()[j - 1]));
                            maxCosine = Math.max(maxCosine, cosine);
                            //printf("maxCosin=%f \n", maxCosine);
                            //LOG("maxCosin=%f \n", maxCosine);
                        }
                       Log.d(TAG,"triangle");
                        shape_type = 3;
                        break;
                    case 4:
                        maxCosine = 0;
                        for (int j = 2; j < 5; j++) {
                            double cosine = Math.abs(angle(approxs.toArray()[j % 4], approxs.toArray()[j - 2], approxs.toArray()[j - 1]));
                            maxCosine = Math.max(maxCosine, cosine);
                            Log.d(TAG,"");

                        }
                        if (maxCosine >= 1) {
                            Log.d(TAG,"rectangle");
                            shape_type = 4;

                        }
                        else {
                            Log.d(TAG,"lingxing");
                            shape_type = 4;
                        }
                        break;
                    case 5:
                        Log.d(TAG,"five");
                        shape_type = 5;
                        break;
                    case 6:
                        Log.d(TAG,"six");
                        shape_type = 6;
                        break;
                    default:
                        Log.d(TAG,"side  ");
                        if (approxs.toArray().length > 7) {
                            shape_type = 7;
                            Log.d(TAG,"circle");
                            //	vector<Vec3f> circles;
                            //	HoughCircles(cannyimg, circles, CV_HOUGH_GRADIENT, 1, cannyimg.rows / 8, 200, 100, 0, 0);
                            //	LOG("circles.size = %d\n ", circles.size());
                            break;
                        }
                }
            }

        }


        return 0;
    }



    double angle(Point pt1, Point pt2, Point pt0)
    {
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;

        double dxy1 = dx1*dx1 + dy1*dy1;
        double dxy2 = dx2*dx2 + dy2*dy2;

	/*		if (((dx1*dx1 + dy1*dy1) < 100) || ((dx2*dx2 + dy2*dy2) < 100)) {
	return 0;
	}*/

        //add
        double ratio;//边长平方的比
        ratio = dxy1 / dxy2;
        //ratio=(dx1*dx1+dy1*dy1)/(dx2*dx2+dy2*dy2);
        if (ratio<0.8 || 1.2<ratio) {//根据边长平方的比过小或过大提前淘汰这个四边形，如果淘汰过多，调整此比例数字
            //Log("ratio\n");
            return 1.0;//根据边长平方的比过小或过大提前淘汰这个四边形
        }//end

        return (dx1*dx2 + dy1*dy2) / Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }

    public  String getShape(int index){
        String str =  new String();
        switch(index){
            case 2:
                str = "菱形";
                break;
            case 3:
                str = "三角形";
                break;
            case 4:
                str = "矩形";
                break;
            case 5:
                str = "五边形";
                break;
            case 6:
                str = "六边形";
                break;
            case 7:
                str = "圆";
                break;
            default:
                break;
        }
        return str;
    }
}
