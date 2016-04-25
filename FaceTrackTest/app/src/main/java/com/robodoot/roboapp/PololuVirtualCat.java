package com.robodoot.roboapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.robodoot.dr.RoboApp.PololuHandler;

import org.opencv.core.Point;

/**
 * Created by alex on 4/10/16.
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
    public void stopMovingHead() {
        //stopLookingLeftRight();
        //stopLookingUpDown();
        //NOTHING
    }

    @Override
    public void stopLookingLeftRight() {
        p.setYaw(0);
    }

    @Override
    public void stopLookingUpDown() {
        p.setPitch(0);
    }

    @Override
    public void lookToward(Point relPos) {
        double len = Math.sqrt(Math.pow(relPos.x, 2) + Math.pow(relPos.y, 2));
        //Point norm = new Point(relPos.x / len, relPos.y / len);
        Point norm = relPos;
        if (Math.abs(norm.x) > 0.08) {
            /*if (norm.x < 0.0)
                p.addToYaw(-TURN_SPEED);
            else
                p.addToYaw(TURN_SPEED);*/
            //Log.i(TAG, "setting yaw: " + norm.x);

            p.addToYaw((int)(relPos.x * p.NECK_YAW_SERVO_MAX));
        }
        else
            stopLookingLeftRight();

        if (Math.abs(norm.y) > 0.08) {
            /*if (norm.y < 0.0)
                p.addToPitch(-TURN_SPEED);
            else
                p.addToPitch(TURN_SPEED);*/
            //Log.i(TAG, "setting pitch: " + norm.y);

            p.addToPitch((int) (relPos.y * p.NECK_PITCH_SERVO_MAX));
        }
        else {
            stopLookingUpDown();
        }
    }

    @Override
    public void lookAwayFrom(Point relPos) {
        lookToward(new Point(-relPos.x, -relPos.y));
    }

    public void resetHead() {
        p.home();
        Log.i(TAG, "RESETTING HEAD");
    }
}
