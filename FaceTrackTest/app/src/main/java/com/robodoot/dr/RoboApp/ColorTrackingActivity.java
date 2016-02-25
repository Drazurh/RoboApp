package com.robodoot.dr.RoboApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.robodoot.dr.facetracktest.R;
import com.robodoot.roboapp.BatteryView;
import com.robodoot.roboapp.MockVirtualCat;
import com.robodoot.roboapp.VirtualCat;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

/**
 * Created by alex on 2/10/16.
 */
public class ColorTrackingActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, VirtualCat.CatBatteryListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ColorTrackingActivity";

    private JavaCameraView mOpenCvCameraView;
    private BatteryView mBatteryView;
    private SeekBar mSeekBarLowH, mSeekBarHighH, mSeekBarLowS, mSeekBarHighS, mSeekBarLowV, mSeekBarHighV;

    private Mat mRgba;
    private Mat mGray;

    private boolean mShowThreshold;

    VirtualCat mVirtualCat = new MockVirtualCat();

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

        ((Switch)findViewById(R.id.switch_threshold)).setOnCheckedChangeListener(this);
        ((Switch)findViewById(R.id.switch_camera)).setOnCheckedChangeListener(this);
        ((Switch)findViewById(R.id.switch_battery)).setOnCheckedChangeListener(this);;

        mBatteryView = (BatteryView) findViewById(R.id.battery_view);
        mBatteryView.setConnected(true);
        mBatteryView.setAlpha(0.0f); // hide initially

        mSeekBarLowH = (SeekBar) findViewById(R.id.seek_bar_low_h);
        mSeekBarHighH = (SeekBar) findViewById(R.id.seek_bar_high_h);
        mSeekBarLowS = (SeekBar) findViewById(R.id.seek_bar_low_s);
        mSeekBarHighS = (SeekBar) findViewById(R.id.seek_bar_high_s);
        mSeekBarLowV = (SeekBar) findViewById(R.id.seek_bar_low_v);
        mSeekBarHighV = (SeekBar) findViewById(R.id.seek_bar_high_v);

        mVirtualCat.AddBatteryListener(this);
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
                        mOpenCvCameraView.setCameraIndex(0);
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
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switch_camera:
                mOpenCvCameraView.disableView();
                mOpenCvCameraView.setCameraIndex(isChecked ? 1 : 0);
                mOpenCvCameraView.enableView();
                break;
            case R.id.switch_threshold:
                mShowThreshold = isChecked;
                break;
            case R.id.switch_battery:
                mBatteryView.setAlpha(isChecked ? 1.0f : 0.0f);
                break;
        }
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC4);
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat imgHSV = null;
        Mat imgThresholded = null;
        try {
            inputFrame.rgba().copyTo(mRgba);

            // red 1
//            int iLowH = 170;
//            int iHighH = 179;
//
//            int iLowS = 150;
//            int iHighS = 255;
//
//            int iLowV = 60;
//            int iHighV = 255;

            // red 2
//            int iLowH = 0;
//            int iHighH = 179;
//
//            int iLowS = 210;
//            int iHighS = 255;
//
//            int iLowV = 108;
//            int iHighV = 179;


            // white
//            int iLowH = 0;
//            int iHighH = 179;
//
//            int iLowS = 0;
//            int iHighS = 70;
//
//            int iLowV = 150;
//            int iHighV = 255;

            int iLowH = mSeekBarLowH.getProgress();
            int iHighH = mSeekBarHighH.getProgress();

            int iLowS = mSeekBarLowS.getProgress();
            int iHighS = mSeekBarHighS.getProgress();

            int iLowV = mSeekBarLowV.getProgress();
            int iHighV = mSeekBarHighV.getProgress();

            //imgHSV = new Mat();

            //Imgproc.cvtColor(mRgba, imgHSV, Imgproc.COLOR_RGB2HSV); //Convert the captured frame from BGR to HSV

            imgThresholded = new Mat();
            Imgproc.cvtColor(mRgba, imgThresholded, Imgproc.COLOR_RGB2HSV); //Convert the captured frame from BGR to HSV

            //Core.inRange(imgHSV, new Scalar(iLowH, iLowS, iLowV), new Scalar(iHighH, iHighS, iHighV), imgThresholded); //Threshold the image
            Core.inRange(imgThresholded, new Scalar(iLowH, iLowS, iLowV), new Scalar(iHighH, iHighS, iHighV), imgThresholded); //Threshold the image

            //morphological opening (removes small objects from the foreground)
            Imgproc.erode(imgThresholded, imgThresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
            Imgproc.dilate(imgThresholded, imgThresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));

            //morphological closing (removes small holes from the foreground)
            Imgproc.dilate(imgThresholded, imgThresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
            Imgproc.erode(imgThresholded, imgThresholded, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));

            //Calculate the moments of the thresholded image
            Moments oMoments = Imgproc.moments(imgThresholded);

            double dM01 = oMoments.m01;
            double dM10 = oMoments.m10;
            double dArea = oMoments.m00;

            // if the area <= 10000, I consider that the there are no object in the image and it's because of the noise, the area is not zero
            if (dArea > 10000) {
                //calculate the position of the object
                double posX = dM10 / dArea;
                double posY = dM01 / dArea;

                if (posX >= 0 && posY >= 0) {
                    //Draw a red line from the previous point to the current point
                    //Imgproc.line(mRgba, new Point(posX, posY), new Point(posX, posY), new Scalar(0, 0, 255), 2);
                    Imgproc.rectangle(mRgba, new Point(posX, posY), new Point(posX + 20, posY + 20), new Scalar(255, 255, 255), 5);

                    Log.i(TAG, "I SEE AN OBJECT");
                }
            }

//                float centerX = imgLines.size().width / 2.0f;
//                float centerY = imgLines.size().height / 2.0f;
//                float relativeX = posX - centerX;
//                float relativeY = posY - centerY;

                //cout << "x = " << relativeX << ", y = " << relativeY << endl;
        } catch (Exception e) {
            Log.i(TAG, "Exception " + e.getMessage());
        }

        // transpose and flip
        //Core.flip(mRgba.t(), mRgba, 0);

        //if (imgHSV != null)
        //    imgHSV.release();

        if (imgThresholded != null) {
            if (mShowThreshold) {
                imgThresholded.copyTo(mRgba);
            }
            imgThresholded.release();
        }

        // return the mat to be displayed
        return mRgba;
        //return imgThresholded;
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
        mBatteryView.setCharge(level);
    }
}
