package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import org.pololu.maestro.*;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by Joel on 12/7/2015.
 */
public class PololuHandler implements Serializable {

    public MaestroSSC maestro;

    public static final int NECK_YAW_SERVO = 10;
    public static final int NECK_YAW_SERVO_HOME = 1600;
    public static final int NECK_PITCH_SERVO = 9;
    public static final int NECK_PITCH_SERVO_HOME = 2200;
    private boolean isConnected=false;

    public enum Motor{
        HEAD_YAW        (NECK_PITCH_SERVO, "Head Yaw",NECK_YAW_SERVO_HOME,0,0),
        HEAD_PITCH      (NECK_YAW_SERVO, "Head Pitch",NECK_PITCH_SERVO_HOME,0,0);

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

    public int yaw=NECK_YAW_SERVO_HOME;
    public int pitch=NECK_PITCH_SERVO_HOME;

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
        maestro.setTarget(NECK_PITCH_SERVO,NECK_PITCH_SERVO_HOME);
        maestro.setTarget(NECK_YAW_SERVO,NECK_YAW_SERVO_HOME);
        yaw = NECK_YAW_SERVO_HOME;
        pitch = NECK_PITCH_SERVO_HOME;
    }

    public void setSpeedConst(float newConst)
    {
        speedConst=newConst;
    }

    public void onResume(Intent intent,Activity parent ){
        String action = intent.getAction();
        Log.d("POLOLU HANDLER", "action: " + action);
        Log.d("POLOLU", intent.toString());
        isConnected = false;
        Log.d("POLOLUHANDLER", "IN ON RESUME");
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

    public void stepForward()
    {
        /*int[] a = {170, 12, 31,  3,  1, 104, 42, 104, 50, 104, 62};
        ByteBuffer byteBuffer = ByteBuffer.allocate(a.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(a);

        byte[] array = byteBuffer.array();

        maestro.explicitSend(array);

        maestro.goHome();*/

/*        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 12904);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 12136);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 11880);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 12392);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 10600);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 12904);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 8296);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 13672);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 6248);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 14696);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8296);

        maestro.setTarget(1, 11880);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 16232);

        maestro.setTarget(16, 10600);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 12392);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 16488);

        maestro.setTarget(16, 10088);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 13160);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 16232);

        maestro.setTarget(16, 9320);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 13672);
        maestro.setTarget(2, 4968);
        maestro.setTarget(3, 16232);

        maestro.setTarget(16, 8808);
        maestro.setTarget(17, 15208);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 13160);
        maestro.setTarget(2, 8296);
        maestro.setTarget(3, 16232);

        maestro.setTarget(16, 9320);
        maestro.setTarget(17, 13672);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 12392);
        maestro.setTarget(2, 10600);
        maestro.setTarget(3, 16488);

        maestro.setTarget(16, 10088);
        maestro.setTarget(17, 12904);
        maestro.setTarget(18, 8040);

        maestro.setTarget(1, 10856);
        maestro.setTarget(2, 12904);
        maestro.setTarget(3, 15976);

        maestro.setTarget(16, 11368);
        maestro.setTarget(17, 12136);
        maestro.setTarget(18, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 12904);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 12392);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 11880);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 13416);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 10600);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 14440);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 8296);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 16488);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8296);

        maestro.setTarget(12, 12392);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 16232);
        maestro.setTarget(15, 16232);

        maestro.setTarget(5, 8808);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 12904);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 16488);
        maestro.setTarget(15, 16488);

        maestro.setTarget(5, 8552);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 13672);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 16232);
        maestro.setTarget(15, 16232);

        maestro.setTarget(5, 7528);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 14184);
        maestro.setTarget(13, 4968);
        maestro.setTarget(14, 16232);
        maestro.setTarget(15, 16232);

        maestro.setTarget(5, 7272);
        maestro.setTarget(6, 19304);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 13672);
        maestro.setTarget(13, 8296);
        maestro.setTarget(14, 16232);
        maestro.setTarget(15, 16232);

        maestro.setTarget(5, 7528);
        maestro.setTarget(6, 16488);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 12904);
        maestro.setTarget(13, 10600);
        maestro.setTarget(14, 16488);
        maestro.setTarget(15, 16488);

        maestro.setTarget(5, 8552);
        maestro.setTarget(6, 14440);
        maestro.setTarget(7, 8040);

        maestro.setTarget(12, 11624);
        maestro.setTarget(13, 12904);
        maestro.setTarget(14, 15976);
        maestro.setTarget(15, 15976);

        maestro.setTarget(5, 9832);
        maestro.setTarget(6, 12392);
        maestro.setTarget(7, 8296);*/
    }
}
