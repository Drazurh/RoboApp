package com.robodoot.dr.facetracktest;

import android.app.Activity;
import android.content.Context;
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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Vector;


public class MainActivity extends Activity implements CvCameraViewListener2 {


    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    public static final int JAVA_DETECTOR = 0;
    private static final int TM_SQDIFF = 0;
    private static final int TM_SQDIFF_NORMED = 1;
    private static final int TM_CCOEFF = 2;
    private static final int TM_CCOEFF_NORMED = 3;
    private static final int TM_CCORR = 4;
    private static final int TM_CCORR_NORMED = 5;

    private Mat tempMat;
    private Mat tempMat2;
    private Mat ROI;
    private Mat mZoomWindow;
    private Mat mZoomWindow2;

    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private CascadeClassifier mJavaDetectorEye;

    private int learn_frames = 0;
    private Mat teplateR;
    private Mat teplateL;
    int method = 0;

    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;

    double xCenter = -1;
    double yCenter = -1;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Log.i(TAG, "OpenCV loaded");
                    mOpenCvCameraView.enableView();

                    // load cascade file from application resources
//                    InputStream is = getResources().openRawResource(
//                            R.raw.lbpcascade_frontalface);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//                    mCascadeFile = new File(cascadeDir,
//                            "lbpcascade_frontalface.xml");
//                    FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//                    byte[] buffer = new byte[4096];
//                    int bytesRead;
//                    while ((bytesRead = is.read(buffer)) != -1) {
//                        os.write(buffer, 0, bytesRead);
//                    }
//                    is.close();
//                    os.close();
//
//                    // --------------------------------- load left eye
//                    // classificator -----------------------------------
//                    InputStream iser = getResources().openRawResource(
//                            R.raw.haarcascade_lefteye_2splits);
                    File cascadeDirER = getDir("cascadeER",
                            Context.MODE_PRIVATE);
                    File cascadeFileER = new File(cascadeDirER,
                            "haarcascade_eye_right.xml");
//                    FileOutputStream oser = new FileOutputStream(cascadeFileER);
//
//                    byte[] bufferER = new byte[4096];
//                    int bytesReadER;
//                    while ((bytesReadER = iser.read(bufferER)) != -1) {
//                        oser.write(bufferER, 0, bytesReadER);
//                    }
//                    iser.close();
//                    oser.close();

                    mJavaDetector = new CascadeClassifier(
                            mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
//                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetector = null;
                    } else
//                        Log.i(TAG, "Loaded cascade classifier from "
//                                + mCascadeFile.getAbsolutePath());

                    mJavaDetectorEye = new CascadeClassifier(
                            cascadeFileER.getAbsolutePath());
                    if (mJavaDetectorEye.empty()) {
//                        Log.e(TAG, "Failed to load cascade classifier");
                        mJavaDetectorEye = null;
                    } else
//                        Log.i(TAG, "Loaded cascade classifier from "
//                                + mCascadeFile.getAbsolutePath());



                    cascadeDir.delete();

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
        mZoomWindow = new Mat();
        mZoomWindow2 = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        tempMat.release();
        tempMat2.release();
        ROI.release();
        mZoomWindow.release();
        mZoomWindow2.release();

    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
//
//        tempMat = inputFrame.rgba();
//        tempMat2 = inputFrame.gray();
//
//
////        Core.flip(tempMat2.t(),tempMat2,1);
//
//        Imgproc.GaussianBlur(tempMat2,tempMat2,new Size(21,21),6,6);
//        Imgproc.HoughCircles(tempMat2, ROI, Imgproc.CV_HOUGH_GRADIENT, 1, tempMat2.rows()/8,120,40,0,0);
//
//        int totalCirclesDetected = 0;
//        if (ROI.cols() > 0) {
//            for (int x = 0; x < Math.min(ROI.cols(), 10); x++) {
//                double vCircle[] = ROI.get(0, x);
//                if (vCircle == null)
//                    break;
//                Log.i("Circle :", "Yes ");
//
//                totalCirclesDetected++;
//                Point pt = new Point();
//
//                pt.x = Math.round(vCircle[0]);
//                pt.y = Math.round(vCircle[1]);
//                int radius = (int) Math.round(vCircle[2]);
//                // draw the found circle
//                Imgproc.circle(tempMat2, pt, radius, new Scalar(255,0,0),5);
//
//            }
//        } else {
//            Log.i("Circle :", "No");
//        }
//////        Rect[] facesArray = faces.toArray();
//////        for (int i = 0; i < facesArray.length; i++)
//
//
//
//        return tempMat;


        tempMat = inputFrame.rgba();
        tempMat2 = inputFrame.gray();

        if (mAbsoluteFaceSize == 0) {
            int height = tempMat2.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        if (mZoomWindow == null || mZoomWindow2 == null)
            CreateAuxiliaryMats();

        MatOfRect faces = new MatOfRect();

        if (mJavaDetector != null)
            mJavaDetector.detectMultiScale(tempMat2, faces, 1.1, 2,
                    2, // TODO: objdetect.CV_HAAR_SCALE_IMAGE
                    new Size(mAbsoluteFaceSize, mAbsoluteFaceSize),
                    new Size());

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Imgproc.rectangle(tempMat, facesArray[i].tl(), facesArray[i].br(),
                    FACE_RECT_COLOR, 3);
            xCenter = (facesArray[i].x + facesArray[i].width + facesArray[i].x) / 2;
            yCenter = (facesArray[i].y + facesArray[i].y + facesArray[i].height) / 2;
            Point center = new Point(xCenter, yCenter);

            Imgproc.circle(tempMat, center, 10, new Scalar(255, 0, 0, 255), 3);

            Imgproc.putText(tempMat, "[" + center.x + "," + center.y + "]",
                    new Point(center.x + 20, center.y + 20),
                    Core.FONT_HERSHEY_SIMPLEX, 0.7, new Scalar(255, 255, 255,
                            255));

            Rect r = facesArray[i];
            // compute the eye area
            Rect eyearea = new Rect(r.x + r.width / 8,
                    (int) (r.y + (r.height / 4.5)), r.width - 2 * r.width / 8,
                    (int) (r.height / 3.0));
            // split it
            Rect eyearea_right = new Rect(r.x + r.width / 16,
                    (int) (r.y + (r.height / 4.5)),
                    (r.width - 2 * r.width / 16) / 2, (int) (r.height / 3.0));
            Rect eyearea_left = new Rect(r.x + r.width / 16
                    + (r.width - 2 * r.width / 16) / 2,
                    (int) (r.y + (r.height / 4.5)),
                    (r.width - 2 * r.width / 16) / 2, (int) (r.height / 3.0));
            // draw the area - mGray is working grayscale mat, if you want to
            // see area in rgb preview, change mGray to mRgba
            Imgproc.rectangle(tempMat, eyearea_left.tl(), eyearea_left.br(),
                    new Scalar(255, 0, 0, 255), 2);
            Imgproc.rectangle(tempMat, eyearea_right.tl(), eyearea_right.br(),
                    new Scalar(255, 0, 0, 255), 2);

            if (learn_frames < 5) {
                teplateR = get_template(mJavaDetectorEye, eyearea_right, 24);
                teplateL = get_template(mJavaDetectorEye, eyearea_left, 24);
                learn_frames++;
            } else {
                // Learning finished, use the new templates for template
                // matching
                match_eye(eyearea_right, teplateR, method);
                match_eye(eyearea_left, teplateL, method);

            }


            // cut eye areas and put them to zoom windows
            Imgproc.resize(tempMat.submat(eyearea_left), mZoomWindow2,
                    mZoomWindow2.size());
            Imgproc.resize(tempMat.submat(eyearea_right), mZoomWindow,
                    mZoomWindow.size());


        }

        return tempMat;
    }




    //functions from http://romanhosek.cz/android-eye-detection-updated-for-opencv-2-4-6/

    private void CreateAuxiliaryMats() {
        if (tempMat2.empty())
            return;

        int rows = tempMat2.rows();
        int cols = tempMat2.cols();

        if (mZoomWindow == null) {
            mZoomWindow = tempMat.submat(rows / 2 + rows / 10, rows, cols / 2
                    + cols / 10, cols);
            mZoomWindow2 = tempMat.submat(0, rows / 2 - rows / 10, cols / 2
                    + cols / 10, cols);
        }

    }

    private Mat get_template(CascadeClassifier clasificator, Rect area, int size) {
        Mat template = new Mat();
        Mat mROI = tempMat2.submat(area);
        MatOfRect eyes = new MatOfRect();
        Point iris = new Point();
        Rect eye_template = new Rect();
        clasificator.detectMultiScale(mROI, eyes, 1.15, 2,
                Objdetect.CASCADE_FIND_BIGGEST_OBJECT
                        | Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30),
                new Size());

        Rect[] eyesArray = eyes.toArray();
        for (int i = 0; i < eyesArray.length;) {
            Rect e = eyesArray[i];
            e.x = area.x + e.x;
            e.y = area.y + e.y;
            Rect eye_only_rectangle = new Rect((int) e.tl().x,
                    (int) (e.tl().y + e.height * 0.4), (int) e.width,
                    (int) (e.height * 0.6));
            mROI = tempMat2.submat(eye_only_rectangle);
            Mat vyrez = tempMat.submat(eye_only_rectangle);


            Core.MinMaxLocResult mmG = Core.minMaxLoc(mROI);

            Imgproc.circle(vyrez, mmG.minLoc, 2, new Scalar(255, 255, 255, 255), 2);
            iris.x = mmG.minLoc.x + eye_only_rectangle.x;
            iris.y = mmG.minLoc.y + eye_only_rectangle.y;
            eye_template = new Rect((int) iris.x - size / 2, (int) iris.y
                    - size / 2, size, size);
            Imgproc.rectangle(tempMat, eye_template.tl(), eye_template.br(),
                    new Scalar(255, 0, 0, 255), 2);
            template = (tempMat2.submat(eye_template)).clone();
            return template;
        }
        return template;
    }

    private void match_eye(Rect area, Mat mTemplate, int type) {
        Point matchLoc;
        Mat mROI = tempMat2.submat(area);
        int result_cols = mROI.cols() - mTemplate.cols() + 1;
        int result_rows = mROI.rows() - mTemplate.rows() + 1;
        // Check for bad template size
        if (mTemplate.cols() == 0 || mTemplate.rows() == 0) {
            return ;
        }
        Mat mResult = new Mat(result_cols, result_rows, CvType.CV_8U);

        switch (type) {
            case Imgproc.TM_SQDIFF:
                Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_SQDIFF);
                break;
            case Imgproc.TM_SQDIFF_NORMED:
                Imgproc.matchTemplate(mROI, mTemplate, mResult,
                        Imgproc.TM_SQDIFF_NORMED);
                break;
            case Imgproc.TM_CCOEFF:
                Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_CCOEFF);
                break;
            case Imgproc.TM_CCOEFF_NORMED:
                Imgproc.matchTemplate(mROI, mTemplate, mResult,
                        Imgproc.TM_CCOEFF_NORMED);
                break;
            case Imgproc.TM_CCORR:
                Imgproc.matchTemplate(mROI, mTemplate, mResult, Imgproc.TM_CCORR);
                break;
            case Imgproc.TM_CCORR_NORMED:
                Imgproc.matchTemplate(mROI, mTemplate, mResult,
                        Imgproc.TM_CCORR_NORMED);
                break;
        }

        Core.MinMaxLocResult mmres = Core.minMaxLoc(mResult);
        // there is difference in matching methods - best match is max/min value
        if (type == Imgproc.TM_SQDIFF || type == Imgproc.TM_SQDIFF_NORMED) {
            matchLoc = mmres.minLoc;
        } else {
            matchLoc = mmres.maxLoc;
        }

        Point matchLoc_tx = new Point(matchLoc.x + area.x, matchLoc.y + area.y);
        Point matchLoc_ty = new Point(matchLoc.x + mTemplate.cols() + area.x,
                matchLoc.y + mTemplate.rows() + area.y);

        Imgproc.rectangle(tempMat, matchLoc_tx, matchLoc_ty, new Scalar(255, 255, 0,
                255));
        Rect rec = new Rect(matchLoc_tx,matchLoc_ty);


    }


}
