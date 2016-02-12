package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.robodoot.dr.facetracktest.R;
import com.robodoot.roboapp.CatCommunicator;
import com.robodoot.roboapp.MockCatCommunicator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by alex on 2/10/16.
 */
public class ColorTrackingActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, CatCommunicator.CatBatteryListener {
    private static final String TAG = "ColorTrackingActivity";
    private JavaCameraView mOpenCvCameraView;
    private Mat mRgba;
    private Mat mGray;

    CatCommunicator catCommunicator = new MockCatCommunicator();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_color_tracking);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.color_tracking_camera_view);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setAlpha(1.0f);
        mOpenCvCameraView.bringToFront();

        catCommunicator.AddListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                        mOpenCvCameraView.setCameraIndex(1);
                        //mOpenCvCameraView.enableFpsMeter();
                        mOpenCvCameraView.enableView();
                    break;
                    default:
                        super.onManagerConnected(status);
                    break;
                }
            }
        };
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC4);
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        try {
            inputFrame.rgba().copyTo(mRgba);

            // do stuff
        } catch (Exception e) {
            Log.i(TAG, "Exception " + e.getMessage());
            return null;
        }

        // transpose and flip
        Core.flip(mRgba.t(), mRgba, 0);

        // return the mat to be displayed
        return mRgba;
    }

    public void onCameraViewStopped() {
        // this doesn't seem like it should be necessary but better safe than sorry.
        // the mat objects themselves only contain a pointer to the pixel data, so
        // when you copy a mat it still points to the same data as the original.
        // i believe the opencv library handles releasing each frame
        mGray.release();
        mRgba.release();
    }

    @Override
    public void UpdateBatteryLevel(float level) {
        // cool
    }
}
