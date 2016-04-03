package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import org.pololu.maestro.*;

import java.io.Serializable;

/**
 * Created by Joel on 12/7/2015.
 */
public class PololuHandler implements Serializable {

    public MaestroSSC maestro;

    public static final int NECK_YAW_SERVO = 10;
    public static final int NECK_PITCH_SERVO = 9;
    private boolean isConnected=false;

    public enum Motor{
        HEAD_YAW        (9, "Head Yaw",1600,0,0),
        HEAD_PITCH      (10, "Head Pitch",2200,0,0);

        public final int number;
        public final int homePos;
        public final int min;
        public final int max;
        public final String name;

        Motor(int num, String str, int h, int mi, int ma)
        {
            number = num;
            name = str;
            homePos = h;
            min = mi;
            max = ma;

        }
    }

    public float speedConst = 90f;

    public int yaw=1600;
    public int pitch=2200;

    public void setTarget(int ID, int target)
    {
        if(isConnected)
        {
            maestro.setTarget(ID, target);
        }
    }

    public PololuHandler()
    {
        maestro = new MaestroSSC();
    }

    public boolean isOpen()
    {
        return isConnected;
    }

    public void home()
    {
        maestro.setTarget(9,2200);
        maestro.setTarget(10,1600);
        yaw = 1600;
        pitch = 2200;
    }

    public void setSpeedConst(float newConst)
    {
        speedConst=newConst;
    }

    public void onResume(Intent intent,Activity parent ){
        String action = intent.getAction();
        isConnected = false;

        if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbManager usbManager = (UsbManager) parent.getSystemService(Context.USB_SERVICE);
                maestro.setDevice(usbManager, device);
                isConnected = true;
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                maestro.setDevice(null, null);
            }/* else {
            }*/
        }
    }

    public void stopNeckMotors()
    {
//        maestro.setSpeed(NECK_YAW_SERVO,0);
//        maestro.setSpeed(NECK_PITCH_SERVO,0);
    }

    public void cameraYawSpeed(float speedPercent)
    {
        int addToYaw = (int)(speedPercent*speedConst);
        yaw+=addToYaw;
        maestro.setTarget(NECK_YAW_SERVO, yaw);
    }

    public void cameraPitchSpeed(float speedPercent)
    {
        int addToPitch = (int)(speedPercent*speedConst);
        pitch+=addToPitch;
        maestro.setTarget(NECK_PITCH_SERVO, pitch);
    }
}
