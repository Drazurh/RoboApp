package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import org.pololu.maestro.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Joel on 12/7/2015.
 */

class Servo{

    public int minPosition;
    public int maxPosition;
    public int position;
    public int id;
    MaestroSSC maestro;

    public Servo(){
        this.minPosition = 0;
        this.maxPosition = 0;
        this.position = -1;
        this.id = -1;
    }

    public Servo(MaestroSSC _maestro, int _id, int _min, int _max){
        this.id = _id;
        this.minPosition = _min;
        this.maxPosition = _max;
        this.position = -1;
        this.maestro = _maestro;

    }

    public void setPosition(int _position){
        this.maestro.setTarget(this.id,_position);
    }

}


public class PololuHandler implements Serializable {

    MaestroSSC maestro;

    public static final int NECK_YAW_SERVO = 10;
    public static final int NECK_PITCH_SERVO = 9;

    int num_servos = 16;

    ArrayList<Servo> ServoList = new ArrayList<Servo>();





    public float speedConst = 90f;

    public int yaw=1600;
    public int pitch=2200;

    public PololuHandler()
    {
        maestro = new MaestroSSC();
        ServoList.add(new Servo(maestro, NECK_YAW_SERVO, 100, 200));
        ServoList.add(new Servo(maestro, NECK_PITCH_SERVO, 100, 200));
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
