package wmy.jni.com.colorrecognize.core.permission;

/**
 * Created by Administrator on 2015/3/24.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.HashMap;

public class PermissionGrant
        implements OrbbecNIHelper.DevicePermissionListener
{
    private Context m_context;
    private PermissionCallbacks m_callbacks;
    public OrbbecNIHelper orbberHelper;
    public static boolean isOrbbec = false;
    private AlertDialog dialog = null;
    HashMap<String, UsbDevice> deviceList;
    private boolean mDevicePermissionPending = false;
    UsbDeviceConnection mDeviceConnection;

    public PermissionGrant(Context context, PermissionCallbacks callback)
    {
        this.m_context = context;
        this.m_callbacks = callback;
        this.orbberHelper = new OrbbecNIHelper(this.m_context);
        isOrbbec = true;

        myDialog(context);

        this.deviceList = this.orbberHelper.getDeviceList();
        if (this.deviceList.isEmpty())
        {
            //this.dialog.show();
            this.m_callbacks.onDeviceNotFound();
        }
        else
        {
            this.mDevicePermissionPending = true;
            this.orbberHelper.requestDevicePermission(
                    (UsbDevice) this.deviceList.values().toArray()[0], this);
        }
    }

    private void myDialog(Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("检测不到设备，请插入设备!");
        builder.setTitle("提示");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Field field = null;
                try
                {
                    field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                PermissionGrant.this.deviceList = PermissionGrant.this.orbberHelper.getDeviceList();
                if (PermissionGrant.this.deviceList.isEmpty()) {
                    try
                    {
                        field.set(dialog, Boolean.valueOf(false));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else {
                    try
                    {
                        field.set(dialog, Boolean.valueOf(true));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.dialog = builder.create();
    }

    public void onDevicePermissionGranted(UsbDevice device)
    {
        this.mDevicePermissionPending = false;

        this.mDeviceConnection = this.orbberHelper.openDevice(device);

        boolean isUVC = false;
        int pid = device.getProductId();

        if(pid == 0x0403 || pid == 0x0404){
            isUVC = true;
        }
        Log.d("USV FLAG = ","ISuvc = "+isUVC+"PID = "+pid);
        this.m_callbacks.onDevicePermissionGranted(isUVC);
    }

    public void Close()
    {
        if(this.mDeviceConnection != null){
            this.mDeviceConnection.close();
        }
        if(this.orbberHelper != null){
            this.orbberHelper.shutdown();
        }
    }

    public void onDevicePermissionDenied(UsbDevice device)
    {
        this.m_callbacks.onDevicePermissionDenied();
    }




}
