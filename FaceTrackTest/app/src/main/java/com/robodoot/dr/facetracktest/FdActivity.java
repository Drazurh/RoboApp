package com.robodoot.dr.facetracktest;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_face;
import org.bytedeco.javacpp.opencv_face.*;

import org.bytedeco.javacpp.opencv_imgproc;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import testkonami.CatEmotion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class FdActivity extends Activity implements GestureDetector.OnGestureListener, CvCameraViewListener2 {

    private GestureDetector gDetector;
    public enum CHAR {U, D, L, R}
    public static Vector<CHAR> psswd = new Vector<>();
    public static Vector<CHAR> entry = new Vector<>();

    private static final String TAG = "OCVSample::Activity";
    public static final int JAVA_DETECTOR = 0;

    private int learn_frames = 0;

    private CatEmotion kitty;
    public enum Directions {UP, DOWN, LEFT, RIGHT}
    private Mat mRgba;
    private Mat mGray;
    private Mat tempMat1;
    private MatOfRect faces;
    private MatOfRect smiles;
    private ImageView[] arrows;


    private Rect[] FaceLocationBuffer;
    private Mat[]  FaceMatBuffer;
    private Mat[]  EigenMats;
    private int IDcount;
    private ArrayList<Scalar> UserColors;
    private ArrayList<ArrayList<Mat>> TrainingSets;
    private FaceRecognizer faceRecognizer;

    private File mCascadeFile;
    private CascadeClassifier mJavaDetectorFace;
    private CascadeClassifier mJavaDetectorSmile;

    private String[] mDetectorName;

    private float mRelativeFaceSize = 0.1f;
    private int mAbsoluteFaceSize = 0;

    private JavaCameraView mOpenCvCameraView;
    boolean[] filter;

    private Size stds = new Size(50,50);

    double xCenter = -1;
    double yCenter = -1;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(
                                R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir,
                                "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        filter = new boolean[5];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        // --------------------------------- load smile
                        // classificator -----------------------------------
                        InputStream isS = getResources().openRawResource(
                                R.raw.haarcascade_smile);
                        File cascadeDirS = getDir("cascadeS",
                                Context.MODE_PRIVATE);
                        File cascadeFileS = new File(cascadeDirS,
                                "haarcascade_smile.xml");
                        FileOutputStream osS = new FileOutputStream(cascadeFileS);

                        byte[] bufferS = new byte[4096];
                        int bytesReadS;
                        while ((bytesReadS = isS.read(bufferS)) != -1) {
                            osS.write(bufferS, 0, bytesReadS);
                        }
                        isS.close();
                        osS.close();

                        mJavaDetectorFace = new CascadeClassifier(
                                mCascadeFile.getAbsolutePath());
                        if (mJavaDetectorFace.empty()) {
                            mJavaDetectorFace = null;
                        } else


                        mJavaDetectorSmile = new CascadeClassifier(
                                cascadeFileS.getAbsolutePath());
                        if (mJavaDetectorSmile.empty()) {
                            mJavaDetectorSmile = null;
                        }
                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mOpenCvCameraView.setCameraIndex(1);
                    mOpenCvCameraView.enableFpsMeter();
                    mOpenCvCameraView.enableView();

                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public FdActivity() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_fd);

        psswd.add(CHAR.U);
        psswd.add(CHAR.U);
        psswd.add(CHAR.D);
        psswd.add(CHAR.D);
        psswd.add(CHAR.L);
        psswd.add(CHAR.R);
        psswd.add(CHAR.L);
        psswd.add(CHAR.R);
        gDetector = new GestureDetector(getApplicationContext(), this);
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        arrows = new ImageView[4];
        arrows[0]=(ImageView)findViewById(R.id.arrow_up);
        arrows[1]=(ImageView)findViewById(R.id.arrow_right);
        arrows[2]=(ImageView)findViewById(R.id.arrow_down);
        arrows[3]=(ImageView)findViewById(R.id.arrow_left);
        for (int i = 0; i < 4; i++) arrows[i].setVisibility(View.INVISIBLE);



        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
        kitty = new CatEmotion(this);
        kitty.pic=(ImageView)findViewById(R.id.image_place_holder);

    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {
        return gDetector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        tempMat1 = new Mat();
        faces = new MatOfRect();
        smiles = new MatOfRect();
        FaceLocationBuffer = new Rect[15];
        for(int i = 0;i<15;i++)FaceLocationBuffer[i]=new Rect(new Point(0,0),new Size(1,1));
        FaceMatBuffer  = new Mat[15];
        for(int i = 0;i<15;i++)FaceMatBuffer[i]=new Mat();
        EigenMats = new Mat[10];
        for(int i = 0;i<10;i++)EigenMats[i]=new Mat();
        IDcount = 1;
        UserColors = new ArrayList<Scalar>();
        UserColors.add(0, new Scalar(0, 0, 0));
        //mOpenCvCameraView.setAlpha(0f);
        mOpenCvCameraView.bringToFront();
        TrainingSets = new ArrayList<ArrayList<Mat>>();
        TrainingSets.add(0,new ArrayList<Mat>());
        faceRecognizer = opencv_face.createFisherFaceRecognizer();
        kitty.pic.setVisibility(View.GONE);


    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        tempMat1.release();
        faces.release();
        smiles.release();
        for(int i = 0;i<9;i++)EigenMats[i].release();
        for(int i = 0;i<12;i++)FaceMatBuffer[i].release();
    }

    public int checkForRecognition(Mat face)
    {
        if (IDcount==1)return 0;
        Size before = face.size();
        Imgproc.resize(face, face, stds);
       // int ID = faceRecognizer.predict(convert(face));
       // Imgproc.resize(face,face,before);
       // Imgproc.rectangle(face,new Point(0,0),new Point(face.cols(),face.rows()),UserColors.get(ID),10);
       // return ID;
        return 0;
    }

    public boolean adjustFaceBuffer(Rect faceRect)
    {


        int sumX = faceRect.x;
        int sumY = faceRect.y;
        int sumA = (int)faceRect.size().area();
        for(int i=FaceLocationBuffer.length-1;i>0;i--)
        {

            FaceMatBuffer[i - 1].copyTo(FaceMatBuffer[i]);
            FaceLocationBuffer[i]= FaceLocationBuffer[i-1];
            sumX = sumX + FaceLocationBuffer[i].x;
            sumY = sumY + FaceLocationBuffer[i].y;
            sumA = sumA + (int)FaceLocationBuffer[i].size().area();

        }

        FaceLocationBuffer[0]=faceRect;
        mRgba.submat(faceRect).copyTo(FaceMatBuffer[0]);
        Imgproc.cvtColor(FaceMatBuffer[0], FaceMatBuffer[0], Imgproc.COLOR_RGB2GRAY);

        if(FaceLocationBuffer[FaceLocationBuffer.length-1].size().area()<2)return false;
        int AvgX = sumX/ FaceLocationBuffer.length;
        int AvgY = sumY/ FaceLocationBuffer.length;
        int AvgA = sumA/ FaceLocationBuffer.length;

        double pX = AvgX/mRgba.width();
        double pY = AvgY/mRgba.height();

        if(pX<0.35) turnCamera(Directions.RIGHT);
        if(pY<0.35) turnCamera(Directions.DOWN);
        if(pX>0.65) turnCamera(Directions.LEFT);
        if(pY>0.65) turnCamera(Directions.UP);


        int count = 0;
        for(int i=0;i< FaceLocationBuffer.length;i++)
        {
            double dist = Math.sqrt(Math.pow(FaceLocationBuffer[i].x-AvgX,2)+Math.pow(FaceLocationBuffer[i].y-AvgY,2));
            if (dist<20) {
                double areaChange = Math.abs(FaceLocationBuffer[i].size().area() / AvgA - 1);
                if (areaChange < 0.15)
                    count++;
            }
        }

        int j=0;
        if (count>=EigenMats.length) {
            for (int i = 0; i < FaceLocationBuffer.length; i++) {
                if (j>=EigenMats.length) break;
                if (Math.sqrt(Math.pow(FaceLocationBuffer[i].x - AvgX, 2) + Math.pow(FaceLocationBuffer[i].y - AvgY, 2)) < 20)
                    if (Math.abs(FaceLocationBuffer[i].size().area() / AvgA - 1) < 0.15) {
                        FaceMatBuffer[i].copyTo(EigenMats[j]);
                        j++;
                    }
            }

            for(int i = 0;i<FaceLocationBuffer.length;i++)
            {
                FaceMatBuffer[i].release();
                FaceMatBuffer[i]=new Mat();
                FaceLocationBuffer[i]=new Rect(new Point(0,0),new Size(1,1));
            }


            return true;
        }

        return false;
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {


        try {



            inputFrame.rgba().copyTo(mRgba);
            inputFrame.gray().copyTo(mGray);

            Core.flip(mRgba.t(), mRgba, 0);
            Core.flip(mGray.t(), mGray, 0);



            if (mAbsoluteFaceSize == 0) {
                int height = mGray.rows();
                if (Math.round(height * mRelativeFaceSize) > 0) {
                    mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                }
            }


            if (mJavaDetectorFace != null)
                mJavaDetectorFace.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());

            Rect biggestFace = new Rect(new Point(0,0),new Size(1,1));

            Rect[] facesArray = faces.toArray();


            for (int i = 0; i < facesArray.length; i++) {



                xCenter = (facesArray[i].x + facesArray[i].width + facesArray[i].x) / 2;
                yCenter = (facesArray[i].y + facesArray[i].y + facesArray[i].height) / 2;
                Point center = new Point(xCenter, yCenter);

                Rect r = facesArray[i];


                mGray.submat(r).copyTo(tempMat1);
                Imgproc.equalizeHist(tempMat1, tempMat1);



                if(checkForRecognition(tempMat1)>0)

                    break;

                Imgproc.cvtColor(tempMat1, tempMat1, Imgproc.COLOR_GRAY2RGBA);
                tempMat1.copyTo(mRgba.submat(r));
                Imgproc.rectangle(mRgba, r.br(), r.tl(), new Scalar(255, 0, 0), 8);
                if(r.size().area()>biggestFace.size().area())biggestFace=r;

                //try to detect frowns
                Point mouthPt1 = new Point(r.x + r.width / 10, r.y + r.height / 2 + r.height / 10);
                Point mouthPt2 = new Point(r.x + r.width - r.width / 10, r.y + r.height - r.height / 10);
                Rect mouthRect = new Rect(mouthPt1, mouthPt2);
                    //find the number of smiles


                    if (mJavaDetectorSmile != null)
                        mJavaDetectorSmile.detectMultiScale(mGray.submat(mouthRect), smiles, 1.4, 3, 0, new Size(mouthRect.width * 0.6, mouthRect.height * 0.4), new Size());

                    if(smiles.size().area()-1>0)kitty.smiledAt();

            }

            if (biggestFace.size().area()!=1)
            {
                if(adjustFaceBuffer(biggestFace))
                    addNewUser();
            }







        }
        catch(Exception e){
            System.gc();
            return null;}

        return mRgba;
    }

    private opencv_core.Mat convert(Mat m)
    {
        Bitmap bmp = null;
        Mat tmp = new Mat (m.cols(), m.rows(), CvType.CV_8U, new Scalar(4));
        try {

            Imgproc.cvtColor(m, tmp, Imgproc.COLOR_GRAY2RGBA);
            bmp = Bitmap.createBitmap(tmp.cols(), tmp.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tmp, bmp);
        }
        catch (CvException e){Log.d("Exception", e.getMessage());}


        opencv_core.IplImage img = BitmapToIplImage(bmp, m.rows(), m.cols());
        return opencv_core.cvarrToMat(img);



    }

    public static opencv_core.IplImage BitmapToIplImage(Bitmap source, int cols, int rows) {
        opencv_core.IplImage container = opencv_core.IplImage.create(cols, rows, opencv_core.IPL_DEPTH_8U, 4);
        source.copyPixelsToBuffer(container.getByteBuffer());
        return container;
    }



    private void addNewUser(){
        Random rand = new Random();
        UserColors.add(IDcount, new Scalar(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255)));

        opencv_core.MatVector TrainingMats = new opencv_core.MatVector(EigenMats.length);
        TrainingSets.add(IDcount, new ArrayList<Mat>());

        opencv_core.Mat labels = new opencv_core.Mat(EigenMats.length,1,opencv_core.CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();
        Imgproc.resize(EigenMats[0],EigenMats[0],stds);
        TrainingSets.get(IDcount).add(0, EigenMats[0].clone());

        labelsBuf.put(0, 0);
        Imgproc.circle(EigenMats[0], new Point(stds.width / 2, stds.height/2), (int)stds.width, new Scalar(255, 255, 0), 30);
        TrainingMats.put((long)0,convert(EigenMats[0]));
        for(int i=1; i<EigenMats.length;i++)
        {
            Imgproc.resize(EigenMats[i],EigenMats[i],stds);
            TrainingSets.get(IDcount).add(i, EigenMats[i].clone());
            TrainingMats.put((long)i,convert(EigenMats[i]));
            labelsBuf.put(i,IDcount);
        }

        faceRecognizer.train(TrainingMats, labels);



//        int i =0;
//        int sumH = 0;
//        int sumW = 0;
//        for(i =0;i<EigenMats.length;i++)
//        {
//            sumH = sumH+EigenMats[i].height();
//            sumW = sumW+EigenMats[i].width();
//        }
//        i=0;
//        Mat avgImg = new Mat(new Size(sumW/EigenMats.length,sumH/EigenMats.length), EigenMats[0].type());
//        i = 0;
//        for(i=0;i<EigenMats.length;i++)
//        {
//
//            Core.divide(EigenMats[i], new Scalar(EigenMats.length), EigenMats[i]);
//            Imgproc.resize(avgImg,avgImg,EigenMats[i].size());
//            Core.add(avgImg, EigenMats[i], avgImg);
//        }


//        for(i=0;i<EigenMats.length;i++)
//        {
//            Imgproc.resize(avgImg,avgImg,TrainingSets.get(IDcount).get(i).size());
//            Core.absdiff(TrainingSets.get(IDcount).get(i),avgImg,TrainingSets.get(IDcount).get(i));
//
//        }


//        Imgproc.cvtColor(TrainingSets.get(IDcount).get(2), TrainingSets.get(IDcount).get(2), Imgproc.COLOR_GRAY2RGBA);
//        TrainingSets.get(IDcount).get(2).copyTo(mRgba.submat(new Rect(new Point(50, 50), TrainingSets.get(IDcount).get(2).size())));




        IDcount++;
        //checkForRecognition(EigenMats[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

    public boolean onSwipe(Direction direction){
        if(direction == Direction.right) {
            entry.add(CHAR.R);
            //((ImageView)findViewById(R.id.image_place_holder)).setImageResource(R.drawable.right);
        }
        else if(direction == Direction.left) {
            entry.add(CHAR.L);
            // ((ImageView)findViewById(R.id.image_place_holder)).setImageResource(R.drawable.left);
        }
        else if(direction == Direction.up) {
            entry.add(CHAR.U);
            // ((ImageView)findViewById(R.id.image_place_holder)).setImageResource(R.drawable.up);
        }
        else if(direction == Direction.down) {
            entry.add(CHAR.D);
            // ((ImageView)findViewById(R.id.image_place_holder)).setImageResource(R.drawable.down);
        }

        if(entry.equals(psswd)) {
            //((ImageView)findViewById(R.id.image_place_holder)).setImageResource(R.drawable.enter);
            //Code to switch activities to open Menu.
            //Intent intent = new Intent(this, MenuActivity.class);
            entry.clear();
            kitty.feedTreat();
        }

        else if(entry.lastElement() != psswd.elementAt(entry.size()-1)) {
            entry.clear();
            kitty.beat();

        }
        return true;
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);
        return Direction.get(angle);
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }


    public enum Direction{
        up,
        down,
        left,
        right;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction get(double angle){
            if(inRange(angle, 45, 135)){
                return Direction.up;
            }
            else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
                return Direction.right;
            }
            else if(inRange(angle, 225, 315)){
                return Direction.down;
            }
            else{
                return Direction.left;
            }

        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        /*
        Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
        Let e1 be the initial event
        e2 can be located at 4 different positions, consider the following diagram
        (Assume that lines are separated by 90 degrees.)


        \ A  /
        \  /
        D   e1   B
        /  \
        / C  \

        So if (x2,y2) falls in region:
        A => it's an UP swipe
        B => it's a RIGHT swipe
        C => it's a DOWN swipe
        D => it's a LEFT swipe

        */

        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Direction direction = getDirection(x1, y1, x2, y2);
        return onSwipe(direction);
    }

    @Override
    public void onLongPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        // TODO Auto-generated method stub
        return false;
    }




    public void turnCamera(Directions d)
    {
        switch (d) {
            case UP:
                arrows[0].setVisibility(View.VISIBLE);
                break;
            case RIGHT:
                arrows[1].setVisibility(View.VISIBLE);
                break;
            case DOWN:
                arrows[2].setVisibility(View.VISIBLE);
                break;
            case LEFT:
                arrows[3].setVisibility(View.VISIBLE);
                break;
            default:
        }
    }


}