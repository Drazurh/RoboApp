package com.robodoot.dr.facetracktest;

import android.app.Activity;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Method;


public class MainActivity extends Activity implements CvCameraViewListener2 {

    private Mat tempMat;
    private Mat tempMat2;
    private Mat ROI;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Log.i(TAG, "OpenCV loaded");
                    mOpenCvCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);

                }
            }

        }
    };


    private JavaCameraView mOpenCvCameraView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (JavaCameraView)findViewById(R.id.MainActivityCameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    public void onDestroy()
    {
        super.onDestroy();
        if(mOpenCvCameraView!=null){
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        tempMat = new Mat();
        tempMat2 = new Mat();
        ROI = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        tempMat.release();
        tempMat2.release();
        ROI.release();
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        tempMat = inputFrame.rgba();
        tempMat2 = inputFrame.gray();


//        Core.flip(tempMat2.t(),tempMat2,1);


        Imgproc.GaussianBlur(tempMat2,tempMat2,new Size(9,9),2,2);

        Mat circles = new Mat();

        Imgproc.HoughCircles(tempMat2, circles, Imgproc.CV_HOUGH_GRADIENT, 1, tempMat2.rows()/8,200,100,0,0);

        int totalCirclesDetected = 0;
        if (circles.cols() > 0) {
            for (int x = 0; x < Math.min(circles.cols(), 10); x++) {
                double vCircle[] = circles.get(0, x);
                if (vCircle == null)
                    break;
                Log.i("Circle :", "Yes ");

                totalCirclesDetected++;
                Point pt = new Point();

                pt.x = Math.round(vCircle[0]);
                pt.y = Math.round(vCircle[1]);
                int radius = (int) Math.round(vCircle[2]);
                // draw the found circle
                Imgproc.circle(tempMat, pt, radius, new Scalar(255,0,0),5);

            }
        } else {
            Log.i("Circle :", "No");
        }
////        Rect[] facesArray = faces.toArray();
////        for (int i = 0; i < facesArray.length; i++)
////            Imgproc.rectangle(tempMat, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
//


        return tempMat;
    }



}
