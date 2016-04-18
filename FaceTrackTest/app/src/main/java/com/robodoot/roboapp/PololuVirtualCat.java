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

    @Override
    public void lookToward(Point relPos) {
        p.cameraYawSpeed((float) relPos.x);
        p.cameraPitchSpeed((float) relPos.y);
    }

    @Override
    public void lookAwayFrom(Point relPos) {
        lookToward(new Point(-relPos.x, -relPos.y));
    }

    public void resetHead() {
        p.home();
    }
}
