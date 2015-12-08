package com.robodoot.dr.RoboApp;

import android.widget.ImageView;
import com.robodoot.dr.facetracktest.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by stopn_000 on 11/16/2015.
 */
public class CatEmotion {
    public enum EMOTION {HAPPY, HAPPY_TONGUE, HAPPIER, HEARTS, ANNOYED, SAD, SADDER, CONCERNED, CRYING, DISGUSTED, HEARTS_TONGUE, KAWAII_EYES_CLOSED, KAWAII_EYES_OPEN, LOOK_RIGHT, LOOK_LEFT, YAWNING};
    private EMOTION state;
    private int scale;
    private Timer tm;
    private TimerTask calc;
    public  ImageView pic;
    protected FdActivity context;
    private ArrayList<Opinion> opinions;

    private boolean default_display = true;

    public CatEmotion(FdActivity c) {
        opinions = new ArrayList<Opinion>();
        state = EMOTION.HAPPY;
        context = c;
        scale = 0;
        tm = new Timer("tm");
        calc = new TimerTask() {
            @Override
            public void run() {
                if(scale > 0) {
                    scale-=2;
                }
                else if(scale < 0) {
                    scale++;
                }
                reCalcFace();
                return;
            }
        };




        tm.schedule(calc, 100, 300);
    }

    public void reCalcFace() {

        if(scale>120)scale=120;
        if(scale<-120)scale=-120;

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(default_display) {
                    if (scale <= -100) {
                        state = EMOTION.SADDER;
                    } else if (scale <= -66) {
                        state = EMOTION.SAD;
                    } else if (scale <= -33) {
                        state = EMOTION.ANNOYED;
                    } else if (scale <= 33) {
                        state = EMOTION.HAPPY;
                    } else if (scale <= 66) {
                        state = EMOTION.HAPPY_TONGUE;
                    } else if (scale <= 100) {
                        state = EMOTION.HAPPIER;
                    } else {
                        state = EMOTION.HEARTS;
                    }
                }

                switch (state) {
                    case HEARTS:
                        pic.setImageResource(R.drawable.face_hearts);
                        break;
                    case HAPPIER:
                        pic.setImageResource(R.drawable.face_happier);
                        break;
                    case HAPPY_TONGUE:
                        pic.setImageResource(R.drawable.face_happytongue);
                        break;
                    case HAPPY:
                        pic.setImageResource(R.drawable.face_happy);
                        break;
                    case ANNOYED:
                        pic.setImageResource(R.drawable.face_annoyed);
                        break;
                    case SAD:
                        pic.setImageResource(R.drawable.face_sad);
                        break;
                    case SADDER:
                        pic.setImageResource(R.drawable.face_sadder);
                        break;
                    case CONCERNED:
                        pic.setImageResource(R.drawable.face_concerned);
                        break;
                    case CRYING:
                        pic.setImageResource(R.drawable.face_crying);
                        break;
                    case DISGUSTED:
                        pic.setImageResource(R.drawable.face_disgusted);
                        break;
                    case HEARTS_TONGUE:
                        pic.setImageResource(R.drawable.face_heartstongue);
                        break;
                    case KAWAII_EYES_CLOSED:
                        pic.setImageResource(R.drawable.face_kawaiieyesclosed);
                        break;
                    case KAWAII_EYES_OPEN:
                        pic.setImageResource(R.drawable.face_kawaiieyesopen);
                        break;
                    case LOOK_LEFT:
                        pic.setImageResource(R.drawable.face_lookleft);
                        break;
                    case LOOK_RIGHT:
                        pic.setImageResource(R.drawable.face_lookright);
                        break;
                    case YAWNING:
                        pic.setImageResource(R.drawable.face_yawning);
                        break;
                    default:
                        pic.setImageResource(R.drawable.face_happy);
                        break;
                }
            }
        });
    }

    private class faceAnimator{

        private ArrayList<EMOTION> emotionStack;
        private ArrayList<Integer> timeStack;
        private Timer cf;
        private TimerTask changeFace;

        public faceAnimator()
        {
            emotionStack = new ArrayList<EMOTION>();
            timeStack = new ArrayList<Integer>();

            cf = new Timer("tm");
            changeFace = new TimerTask() {
                @Override
                public void run() {
                    nextFrame();

                }
            };
        }

        public void addFrame(EMOTION e, int time)
        {
            emotionStack.add(e);
            timeStack.add(time);
            if(default_display)
            {
                default_display=false;
                cf.schedule(changeFace,10);
            }
        }

        private void nextFrame()
        {
            if(emotionStack.size()==0){
                default_display=true;
                return;
            }

            emotionStack.

        }




    }

    public int lookedAt(int ID, boolean smiling, boolean frowning){

        Opinion catsOpinion = getOpinionFromList(ID,opinions);

        if(smiling){
            catsOpinion.addHappiness(3);

        }
        else if (frowning){
            catsOpinion.addHappiness(-3);
        }
        else{
            catsOpinion.addHappiness(-1);


        }


        scale+=catsOpinion.happiness/10;

        if(catsOpinion.happiness<0&&scale<catsOpinion.happiness)scale = catsOpinion.happiness;
        if(catsOpinion.happiness>0&&scale>catsOpinion.happiness)scale = catsOpinion.happiness;

        return catsOpinion.happiness;

    }

    public void addNewID(int newID)
    {
        opinions.add(new Opinion(newID));

    }

    public int getFavPerson(ArrayList<Integer> IDs)
    {
        int Fav = -1;
        int maxOpinion = -20000;
        for(int i=0; i<IDs.size();i++)
        {
            Opinion iOpinion = getOpinionFromList(IDs.get(i),opinions);
            if(iOpinion.happiness>maxOpinion)
            {
                maxOpinion = iOpinion.happiness;
                Fav = iOpinion.ID;

            }
        }

        return Fav;


    }

    private Opinion getOpinionFromList(int oID, ArrayList<Opinion> oList)
    {
        for(int i=0; i<oList.size();i++)
        {
            if(oList.get(i).ID==oID)return oList.get(i);

        }
        Opinion newO = new Opinion(oID);
        oList.add(newO);
        return newO;
    }

    private class Opinion {

        public Opinion(int id)
        {
            ID = id;
            happiness = 0;
        }

        public int ID;
        public int happiness;

        public void addHappiness(int toAdd)
        {
            happiness+=toAdd;
            if(happiness>120)happiness=120;
            if(happiness<-120)happiness=-120;

        }


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

        scale+=5;
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
