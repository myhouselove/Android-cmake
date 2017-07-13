package wmy.jni.com.colorrecognize.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;


/**
 * Created by zlh on 2015/6/19.
 */
public class DepthViewer extends DataViewer {

    protected Bitmap mUserLabelsBitmap;
    protected Paint  mUserLabelsPaint;
    protected Paint  mSkeletonJointPaint;
    protected Paint  mJointLinePaint;

    private static final String TAG = DepthViewer.class.getSimpleName();


    public DepthViewer(Context context){
        super(context);
        setZOrderOnTop(true);
        setPaint();
    }

    private void setPaint(){

        mRectPaint.setColor(Color.GREEN);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(3);

        mUserLabelsPaint = new Paint();
        mUserLabelsPaint.setColor(Color.BLUE);
        mUserLabelsPaint.setStrokeWidth(10);

        mSkeletonJointPaint = new Paint();
        mSkeletonJointPaint.setColor(Color.GREEN);
        mSkeletonJointPaint.setStyle(Paint.Style.FILL);
        mSkeletonJointPaint.setStrokeWidth(10);

        mJointLinePaint = new Paint();
        mJointLinePaint.setColor(Color.RED);
        mJointLinePaint.setStrokeWidth(5);
    }


    public void drawDepthBitmap(Bitmap bitmap)
    {
        if(mCanvas != null){
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            mCanvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0f, 0f, getWidth(), getHeight()), null);
        }
    }
}
