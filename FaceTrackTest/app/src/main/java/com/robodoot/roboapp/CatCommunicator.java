package com.robodoot.roboapp;

/**
 * Created by alex on 2/10/16.
 */
public interface CatCommunicator {
    // interface for anyone who wants battery level updates
    interface CatBatteryListener {
        void updateBatteryLevel(float level);
    }


    public void addListener(CatBatteryListener listener);
}
