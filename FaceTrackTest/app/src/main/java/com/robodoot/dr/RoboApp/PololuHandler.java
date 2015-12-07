package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import org.pololu.maestro.*;

/**
 * Created by Joel on 12/7/2015.
 */
public class PololuHandler {

    MaestroSSC maestro;

    public static final int NECK_YAW_SERVO = 1;
    public static final int NECK_PITCH_SERVO = 1;

    public PololuHandler()
    {

        maestro = new MaestroSSC();





    }

    public void onResume(Intent intent,Activity parent ){
        String action = intent.getAction();

        if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
                UsbManager usbManager = (UsbManager) parent.getSystemService(Context.USB_SERVICE);
                maestro.setDevice(usbManager, device);
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                maestro.setDevice(null, null);
            } else {

            }
        }
    }



    public void stopNeckMotors()
    {
        maestro.setSpeed(NECK_YAW_SERVO,0);
        maestro.setSpeed(NECK_PITCH_SERVO,0);
    }

    public void cameraYawSpeed(float speedPercent)
    {
        maestro.setSpeed(NECK_YAW_SERVO,(int)(3200*speedPercent));

    }

    public void cameraPitchSpeed(float speedPercent)
    {
        maestro.setSpeed(NECK_PITCH_SERVO,(int)(3200*speedPercent));
    }




}
