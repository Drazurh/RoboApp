package com.robodoot.dr.RoboApp;

import android.widget.ImageView;
import com.robodoot.dr.facetracktest.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stopn_000 on 11/16/2015.
 */
public class CatEmotion {
    public enum EMOTION {Neutral, SlightlyHappy, Happy, VeryHappy, SlightlyMad, Mad, VeryMad};
    private EMOTION state;
    private int scale;
    private Timer tm;
    private TimerTask calc;
    public  ImageView pic;
    protected FdActivity context;

    public CatEmotion(FdActivity c) {
        state = EMOTION.Neutral;
        context = c;
        scale = 0;
        tm = new Timer("tm");
        calc = new TimerTask() {
            @Override
            public void run() {
                if(scale > 0) {
                    scale-=4;
                }
                else if(scale < 0) {
                    scale++;
                }
                reCalcFace();
                return;
            }
        };
        tm.schedule(calc,100,300);
    }

    public void reCalcFace() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (scale <= -100) {
                    state = EMOTION.VeryMad;
                } else if (scale <= -66) {
                    state = EMOTION.Mad;
                } else if (scale <= -33) {
                    state = EMOTION.SlightlyMad;
                } else if (scale <= 33) {
                    state = EMOTION.Neutral;
                } else if (scale <= 66) {
                    state = EMOTION.SlightlyHappy;
                } else if (scale <= 100) {
                    state = EMOTION.Happy;
                } else {
                    state = EMOTION.VeryHappy;
                }

                switch (state) {
                    case VeryHappy:
                        pic.setImageResource(R.drawable.supersmile);
                        break;
                    case Happy:
                        pic.setImageResource(R.drawable.smile);
                        break;
                    case SlightlyHappy:
                        pic.setImageResource(R.drawable.lesssmile);
                        break;
                    case Neutral:
                        pic.setImageResource(R.drawable.neutral);
                        break;
                    case SlightlyMad:
                        pic.setImageResource(R.drawable.sadface);
                        break;
                    case Mad:
                        pic.setImageResource(R.drawable.angryface);
                        break;
                    case VeryMad:
                        pic.setImageResource(R.drawable.superanger);
                        break;
                    default:
                        pic.setImageResource(R.drawable.neutral);
                        break;
                }
            }
        });
    }
    public void startle()
    {


    }

    public void feedTreat()
    {
        scale+=200;


    }

    public void beat()
    {
        scale-=200;

    }

    public void smiledAt()
    {

        scale+=3;
        reCalcFace();
    }

    public void smile()
    {
        scale = 100;
        reCalcFace();
    }

    public void neutral()
    {
        scale = 0;
        reCalcFace();

    }

}
