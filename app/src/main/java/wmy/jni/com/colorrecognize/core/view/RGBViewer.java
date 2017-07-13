package wmy.jni.com.colorrecognize.core.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by zlh on 2015/6/19.
 */
public class RGBViewer extends DataViewer {

    private static final String TAG = RGBViewer.class.getSimpleName();

    protected Paint  mJointLinePaint;
    protected Paint mSkeletonPointPaint;
    protected Paint mUserCOMPaint;

    public RGBViewer(Context context){
        super(context);

        setPaint();
    }

    private void setPaint(){

        mRectPaint.setColor(Color.GREEN);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setStrokeWidth(3);

        mJointLinePaint = new Paint();
        mJointLinePaint.setColor(Color.RED);
        mJointLinePaint.setStrokeWidth(5);

        mUserCOMPaint = new Paint();
        mUserCOMPaint.setColor(Color.BLUE);
        mUserCOMPaint.setStrokeWidth(15);

        mSkeletonPointPaint = new Paint();
        mSkeletonPointPaint.setColor(Color.GREEN);
        mSkeletonPointPaint.setStyle(Paint.Style.FILL);
        mSkeletonPointPaint.setStrokeWidth(10);

    }

    public void drawRGBBitmap(Bitmap bitmap){
        if(mCanvas != null){
            mCanvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0f, 0f, getWidth(), getHeight()), null);
        }
    }



}
