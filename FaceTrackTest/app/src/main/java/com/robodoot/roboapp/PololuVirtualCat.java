package com.robodoot.roboapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.robodoot.dr.RoboApp.PololuHandler;

import org.opencv.core.Point;

/**
 * A concrete implementation of the VirtualCat interface which controls the cat by communicating
 * only with the Pololu (no Arduino).
 */
public class PololuVirtualCat extends VirtualCat {
    private static final String TAG = "PololuVirtualCat";

    static final int TURN_SPEED = 5;

    public static PololuHandler p;

    // DEFAULT CONSTRUCTOR
    public PololuVirtualCat() {
        new Thread(new Runnable() {
            public void run() {
                p = new PololuHandler();
            }
        }).start();
    }

    public void UpdateObjectPosition(int relX, int relY) {
        Log.i(TAG, "UpdateObjectPosition: not implemented");
    }

    public void onResume(Intent intent, Activity parent) {
        p.onResume(intent, parent);
        Log.i(TAG, "IN ONRESUME");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Implementation of cat movements
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void stepForward() {
        p.stepForward();
    }

    @Override
    public void stepBackward() {
        //p.setBackward();
    }

    @Override
    public void stepLeft() {
        //p.stepLeft();
    }

    @Override
    public void stepRight() {
        //p.stepRight();
    }

    @Override
    public void turnHeadDown() {
        //p.cameraPitchSpeed(-0.3f);
        p.addToPitch(30);
    }

    @Override
    public void turnHeadLeft() {
        //p.cameraYawSpeed(0.3f);
        p.addToYaw(-50);
    }

    @Override
    public void turnHeadRight() {
        p.addToYaw(50);
        //p.cameraYawSpeed(-0.3f);
    }

    @Override
    public void turnHeadUp() {
        //p.cameraPitchSpeed(-0.3f);
        p.addToPitch(-30);
    }

    @Override
    public void lookToward(Point relPos) {
        int yaw = (int)(relPos.x * p.NECK_YAW_SERVO_MAX * 0.1f);
        if (Math.abs(yaw) >= 25) {
            p.addToYaw(yaw);
        }
        int pitch = (int) (relPos.y * p.NECK_PITCH_SERVO_MAX * 0.075f);
        if (Math.abs(pitch) >= 25) {
            p.addToPitch(pitch);
        }
    }

    @Override
    public void lookAwayFrom(Point relPos) {
        lookToward(new Point(-1.0f / relPos.x, -1.0f / relPos.y));
    }

    public void resetHead() {
        p.home();
        Log.i(TAG, "RESETTING HEAD");
    }
}
