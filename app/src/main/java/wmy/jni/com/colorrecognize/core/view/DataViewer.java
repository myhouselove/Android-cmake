package wmy.jni.com.colorrecognize.core.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.SurfaceView;

/**
 * Created by zlh on 2015/6/20.
 */
public class DataViewer extends SurfaceView {

    private String TAG = DataViewer.class.getName();

    protected Canvas mCanvas;
    protected Paint mRectPaint;
    protected Paint  mCleanPaint;

    public DataViewer(Context context){
        super(context);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        //surfceview放置在顶层，即始终位于最上层
        //
        setZOrderMediaOverlay(true);
        mRectPaint = new Paint();

        mCleanPaint = new Paint();
        mCleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public void beginDraw(){
        mCanvas = getHolder().lockCanvas();
        if(mCanvas == null){
            Log.v(TAG, " mCanvas is null");
            return;
        }
        mCanvas.drawPaint(mCleanPaint);
    }

    public void endDraw(){
        if(mCanvas != null){
            getHolder().unlockCanvasAndPost(mCanvas);
        }
    }

}
