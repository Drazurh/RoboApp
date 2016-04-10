package com.robodoot.roboapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 2/10/16.
 */
public abstract class VirtualCat {
    // interface for anyone who wants battery level updates
    public interface CatBatteryListener {
        void UpdateBatteryLevel(float level);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BATTERY LEVEL STUFF
    ////////////////////////////////////////////////////////////////////////////////////////////////

    // battery event listeners
    protected List<CatBatteryListener> batteryListeners = new ArrayList<CatBatteryListener>();

    public void AddBatteryListener(CatBatteryListener listener) {
        batteryListeners.add(listener);
    }

    public abstract void UpdateObjectPosition(int relX, int relY);

    public abstract void stepForward();

    public abstract void stepBackward();

    public abstract void stepLeft();

    public abstract void stepRight();

    public abstract void turnHeadLeft();

    public abstract void turnHeadRight();

    public abstract void turnHeadUp();

    public abstract void turnHeadDown();
}
