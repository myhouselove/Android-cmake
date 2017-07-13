package wmy.jni.com.colorrecognize;

import org.opencv.core.Mat;

import java.security.AlgorithmConstraints;

/**
 * Created by win7 on 2017/7/6 0006.
 */

abstract class OpenCVAlgorithm {


    public OpenCVAlgorithm(){}

    abstract void AlgrithmInterface();
    abstract void AlgrithmInterface(Mat frame);
    public OpenCVAlgorithm(int i){

    }

}


class OpenCVContext{
    public OpenCVContext(OpenCVAlgorithm opencv){
        if(opencv != null)
            _opencv = opencv;
    }
    public void doAction(){
        _opencv.AlgrithmInterface();
    }
    public void doFrame(Mat frame){
        _opencv.AlgrithmInterface(frame);
    }
    private OpenCVAlgorithm _opencv;
}

