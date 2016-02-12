package com.robodoot.roboapp;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/10/16.
 */
public class MockCatCommunicator implements CatCommunicator {
    private static final String TAG = "MockCatCommunicator";

    // DEFAULT CONSTRUCTOR
    public MockCatCommunicator() {
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
    public void AddListener(CatBatteryListener listener) {
        batteryListeners.add(listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SENDING DATA TO CAT
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void SendObjectRelativePositionToCat(int relX, int relY) {
        Log.i(TAG, "Sending relative coordinates to cat: x = " + relX + ", y = " + relY);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO: MORE STUFF
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
