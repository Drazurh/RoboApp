package com.robodoot.roboapp;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/10/16.
 */
public class MockCatCommunicator implements CatCommunicator {
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

            /* and here comes the "trick" */
            batteryUpdateHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void AddListener(CatBatteryListener listener) {
        batteryListeners.add(listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // TODO: MORE STUFF
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
