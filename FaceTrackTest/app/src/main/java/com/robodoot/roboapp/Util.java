package com.robodoot.roboapp;

import org.opencv.core.Rect;

/**
 * Created by alex on 3/14/16.
 */
public class Util {
    static boolean checkSimilarRect(Rect r1, Rect r2)
    {
        double maxRange = (r1.width + r2.width) / 20.0;

        int xCenter1 = (r1.x + r1.width + r1.x) / 2;
        int yCenter1 = (r1.y + r1.y + r1.height) / 2;

        int xCenter2 = (r2.x + r2.width + r2.x) / 2;
        int yCenter2 = (r2.y + r2.y + r2.height) / 2;

        double avgArea = (r1.area()+r2.area())/2;
        double maxSizeDiff = 0.15*avgArea;

        double range = Math.sqrt(Math.pow(xCenter1-xCenter2,2)+Math.pow(yCenter1-yCenter2,2));
        double sizeDiff = Math.abs(r1.area()-r2.area());

        return range < maxRange && sizeDiff < maxSizeDiff;
    }
}
