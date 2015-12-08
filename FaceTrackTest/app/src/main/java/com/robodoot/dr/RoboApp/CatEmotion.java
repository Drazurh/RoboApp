package com.robodoot.dr.RoboApp;

import android.widget.ImageView;
import com.robodoot.dr.facetracktest.R;

import java.util.ArrayList;
import java.util.List;
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
    private ArrayList<Opinion> opinions;

    public CatEmotion(FdActivity c) {
        opinions = new ArrayList<Opinion>();
        state = EMOTION.Neutral;
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
