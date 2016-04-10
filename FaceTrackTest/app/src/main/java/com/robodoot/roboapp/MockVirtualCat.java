package com.robodoot.roboapp;

import android.os.Handler;
import android.util.Log;

import com.robodoot.dr.RoboApp.PololuHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/10/16.
 */
public class MockVirtualCat implements VirtualCat {
    private static final String TAG = "MockVirtualCat";
    private static PololuHandler p;
    // DEFAULT CONSTRUCTOR
    public MockVirtualCat() {
        p = new PololuHandler();
        batteryUpdateHandler.postDelayed(batteryUpdateRunnable, 100);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BATTERY LEVEL STUFF
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // battery event listeners
    private List<CatBatteryListener> batteryListeners = new ArrayList<CatBatteryListener>();

    // periodically raise battery update event
    private float batteryLevel = 0.0f;
    private Handler batteryUpdateHandler = new Handler();
    private Runnable batteryUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            batteryLevel = (batteryLevel + 0.01f) % 1.0f;

            for (CatBatteryListener listener : batteryListeners) {
                listener.UpdateBatteryLevel(batteryLevel);
            }

            // call me again in 100 ms
            batteryUpdateHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void AddBatteryListener(CatBatteryListener listener) {
        batteryListeners.add(listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SENDING DATA TO CAT
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void UpdateObjectPosition(int relX, int relY) {
        Log.i(TAG, "Sending relative coordinates to cat: x = " + relX + ", y = " + relY);
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


}
