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

    public static PololuHandler p;

    // DEFAULT CONSTRUCTOR
    public PololuVirtualCat() {
        p = new PololuHandler();
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
        p.cameraPitchSpeed(-0.3f);
    }

    @Override
    public void turnHeadLeft() {
        p.cameraYawSpeed(0.3f);
    }

    @Override
    public void turnHeadRight() {
        p.cameraYawSpeed(-0.3f);
    }

    @Override
    public void turnHeadUp() {
        p.cameraPitchSpeed(-0.3f);
    }

    static final int TURN_SPEED = 5;
    @Override
    public void lookToward(Point relPos) {
        double len = Math.sqrt(Math.pow(relPos.x, 2) + Math.pow(relPos.y, 2));
        //Point norm = new Point(relPos.x / len, relPos.y / len);
        Point norm = relPos;
        if (Math.abs(norm.x) > 0.08) {
            //p.cameraYawSpeed((float) norm.x);
            if (norm.x < 0.0)
                p.addToYaw(TURN_SPEED);
            else
                p.addToYaw(-TURN_SPEED);
            //Log.i(TAG, "setting yaw: " + norm.x);
        }
        else
            p.cameraYawSpeed(0.0f);

        if (Math.abs(norm.y) > 0.08) {
            //p.cameraPitchSpeed((float) norm.y);
            if (norm.y < 0.0)
                p.addToPitch(TURN_SPEED);
            else
                p.addToPitch(-TURN_SPEED);
            //Log.i(TAG, "setting pitch: " + norm.y);
        }
        else {
            p.cameraPitchSpeed(0.0f);
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
