package com.robodoot.roboapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;

/**
 * Created by alex on 3/14/16.
 */
public class ImageUtil {
    public static opencv_core.Mat convert(Mat m)
    {
        Bitmap bmp = null;
        Mat tmp = new Mat (m.cols(), m.rows(), CvType.CV_8UC1, new Scalar(4));
        try {

            Imgproc.cvtColor(m, tmp, Imgproc.COLOR_GRAY2RGBA);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);
        }
        catch (CvException e){
            Log.d("Exception", e.getMessage());}

        opencv_core.IplImage img = BitmapToIplImage(bmp, m.rows(), m.cols());

        if (img == null) return null;

        //arrows[2].setVisibility(View.VISIBLE);
        opencv_core.Mat temp = opencv_core.cvarrToMat(img);

        if(!temp.isContinuous())
            temp.clone().copyTo(temp);

        opencv_imgproc.cvtColor(temp, temp, opencv_imgproc.COLOR_RGB2GRAY);
        //temp.convertTo(temp, opencv_imgproc.COLOR_RGB2GRAY);

        return temp;
    }

    public static opencv_core.IplImage BitmapToIplImage(Bitmap source, int cols, int rows) {
        opencv_core.IplImage container = opencv_core.IplImage.create(cols, rows, opencv_core.IPL_DEPTH_8U, 4);
        if (container == null) return null;
        source.copyPixelsToBuffer(container.getByteBuffer());
        return container;
    }

    public static Bitmap GetBitmapFromContextAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            InputStream istr = assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(istr);
        }
        catch(Exception e){}

        return bitmap;
    }
}
