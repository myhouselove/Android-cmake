package wmy.jni.com.colorrecognize.core.permission;

/**
 * Created by Administrator on 2015/3/24.
 */
public abstract interface PermissionCallbacks
{
    public abstract void onDevicePermissionGranted(boolean isUVC);

    public abstract void onDevicePermissionDenied();

    public abstract void onDeviceNotFound();
}

