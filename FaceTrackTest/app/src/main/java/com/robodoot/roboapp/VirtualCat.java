package com.robodoot.roboapp;

/**
 * Created by alex on 2/10/16.
 */
public interface VirtualCat {
    // interface for anyone who wants battery level updates
    interface CatBatteryListener {
        void UpdateBatteryLevel(float level);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERFACE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void AddBatteryListener(CatBatteryListener listener);

    public void UpdateObjectPosition(int relX, int relY);

    public void stepForward();

    public void stepBackward();

    public void stepLeft();

    public void stepRight();

    public void turnHeadLeft();

    public void turnHeadRight();

    public void turnHeadUp();

    public void turnHeadDown();
}
