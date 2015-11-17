import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import kerbler.eddie.testkonami.R;

/**
 * Created by stopn_000 on 11/16/2015.
 */
public class CatEmotion {
    public enum EMOTION {Neutral, SlightlyHappy, Happy, VeryHappy, SlightlyMad, Mad, VeryMad};
    private EMOTION state;
    private int scale;
    private Timer tm;
    private TimerTask calc;
    private ImageView pic;

    CatEmotion(ImageView p) {
        state = EMOTION.Neutral;
        pic = p;
        scale = 0;
        tm = new Timer("tm");
        calc = new TimerTask() {
            @Override
            public void run() {
                if(scale > 0) {
                    scale--;
                }
                else if(scale < 0) {
                    scale++;
                }
                CatEmotion.reCalcFace();
                return;
            }
        }
        tm.schedule(calc,1000,3000);
    }

    public void reCalcFace() {
        if(scale <= -100) {
            state = EMOTION.VeryMad;
        }
        else if(scale <= -66) {
            state = EMOTION.Mad;
        }
        else if(scale <= -33) {
            state = EMOTION.SlightlyMad;
        }
        else if(scale <= 33) {
            state = EMOTION.Neutral;
        }
        else if(scale <= 66) {
            state = EMOTION.SlightlyHappy;
        }
        else if(scale <= 100) {
            state = EMOTION.Happy;
        }
        else {
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

}
