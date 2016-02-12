package com.robodoot.roboapp;

/**
 * Created by alex on 2/10/16.
 */
public interface CatCommunicator {
    // interface for anyone who wants battery level updates
    interface CatBatteryListener {
        void UpdateBatteryLevel(float level);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INTERFACE METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void AddListener(CatBatteryListener listener);

    public void SendObjectRelativePositionToCat(int relX, int relY);
}
