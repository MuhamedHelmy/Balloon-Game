package com.example.muhammed.mygame;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammed.mygame.Utiles.Balloon;
import com.example.muhammed.mygame.Utiles.HighScoreHelper;
import com.example.muhammed.mygame.Utiles.SimpleAlertDialog;
import com.example.muhammed.mygame.Utiles.SoundHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements Balloon.BalloonListner{

    TextView score;
    TextView level;
    private Button mbutton;
    private ViewGroup mcontentview;
    private MediaPlayer mdplayer;
    private List<ImageView> mpinimages=new ArrayList<>();
    private List<Balloon> mbaloon=new ArrayList<>();
    /////////////////////////////////////////////////////////
    private int []mballoncolore=new int [3];
    private int mnextcolore;
    private int mscreanwidth;
    private int  mscreanhight;
    private int mlevel;
    private int mscore;
    private int pinsused;
    private int mballonpoped;
    private int ballon_per_level=10;

    //////////////////////////////////////////
    private static final int mindely=500;
    private static final int maxdelay=1500;
    private static final int mindurioration=1000;
    private static final int maxdurioration=8000;
    private static final int numberpins=5;

   ////////////////////////////////////////////////////////
    private boolean mplaying;
    private boolean gamestoped=true;

  ////////////////////////////////////////////////////////
    private SoundHelper soundhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mballoncolore[0]= Color.argb(255,255,0,0);
        mballoncolore[1]= Color.argb(255,0,255,0);
        mballoncolore[2]= Color.argb(255,0,0,255);

        getWindow().setBackgroundDrawableResource(R.drawable.modern_background);

        mcontentview=(ViewGroup)findViewById(R.id.activity_main);
        setofullscrean();






        ViewTreeObserver tree=mcontentview.getViewTreeObserver();
        if(tree.isAlive()){
            tree.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mcontentview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mscreanwidth=mcontentview.getWidth();
                    mscreanhight=mcontentview.getHeight();
                }
            });
        }

        mcontentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setofullscrean();
            }
        });

        score=(TextView)findViewById(R.id.score_display);
        level=(TextView)findViewById(R.id.level_display);
        mbutton=(Button)findViewById(R.id.go_button);
        mpinimages.add((ImageView) findViewById(R.id.pushpin1));
        mpinimages.add((ImageView) findViewById(R.id.pushpin2));
        mpinimages.add((ImageView) findViewById(R.id.pushpin3));
        mpinimages.add((ImageView) findViewById(R.id.pushpin4));
        mpinimages.add((ImageView) findViewById(R.id.pushpin5));
       updatedisplay();
       soundhelper= new SoundHelper(this);
       soundhelper.preparemusicplayer(this);
    }
    //////////////////////////////// fullscrean mode
    public  void setofullscrean()
    {
        ViewGroup root=(ViewGroup)findViewById(R.id.activity_main);
       root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    };
//////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        setofullscrean();
    }
//////////////////////////////////////////// intializig variables
    private void startgame(){
        setofullscrean();
        mlevel=0;
        mscore=0;
        pinsused=0;
        for(ImageView pin:
                mpinimages){
            pin.setImageResource(R.drawable.pin);
        }
        gamestoped=false;
        startlevel();
        soundhelper.playmusic();

    }
 ///////////////////////////
    private void startlevel(){

        mlevel++;
        updatedisplay();
        BalloonLauncher bal=new BalloonLauncher();
        bal.execute(mlevel);
        mplaying=true;
        mballonpoped=0;
        mbutton.setText("RE  GAME");
    }
/////////////////////////////////////////////////////////
    public void play(View view) {

        if(mplaying){
           gameover(false);
        } else if(gamestoped){
            startgame();
        }
          else{
            startlevel();
        }


    }
/////////////////////////////////////////////////////
  public  void finish()
  {
      Toast.makeText(this,String.format("YOU FINISH  LEVEL%d",mlevel),Toast.LENGTH_LONG).show();
      mplaying=false;
      mbutton.setText(String.format("start  level %d",mlevel+1));
  }
//////////////////////////////////////////////////////
    @Override
    public void popbaloon(Balloon balloon, boolean usertouch) {

        mballonpoped++;
           mcontentview.removeView(balloon);
           mbaloon.remove(balloon);
        if (mballonpoped==ballon_per_level){
            finish();

        }
        if(usertouch) {

                    mscore++;
            soundhelper.playSound();

        }
        else {

            pinsused++;
            if (pinsused <= mpinimages.size()) {
                mpinimages.get(pinsused - 1).setImageResource(R.drawable.pin_off);

            }
            if (pinsused == numberpins) {
                //  Toast.makeText(this,"try num !=0 ",Toast.LENGTH_SHORT).show();
                gameover(true);
                return;

            }
        }

        updatedisplay();



    }
///////////////////////////////////////////////////
    private void gameover(boolean allpinsused) {
       // mplaying=false;
      soundhelper.pausemusic();
        Toast.makeText(this,"GAME OVER ",Toast.LENGTH_LONG).show();
        mbutton.setText("RE START");
              for( Balloon balloon : mbaloon
                        ) {
                     mcontentview.removeView(balloon);
                     balloon.setpoped(true);
                }
             mbaloon.clear();
             mplaying=false;
           gamestoped=true;
             mbutton.setText("RESTART GAME");
             if(allpinsused){
                 if(HighScoreHelper.isTopScore(this,mscore)){
                     HighScoreHelper.setTopScore(this,mscore);
                     SimpleAlertDialog dio= SimpleAlertDialog.newInstance("NEW HIGH SCORE ",String.format("YOUR HIGH SCORE IS : %d",mscore));
                     dio.show(getSupportFragmentManager(),null);
                 }
             }


    }

    /////////////////////////////////////////////////////////
    private void updatedisplay() {
        score.setText(String.valueOf(mscore));
        level.setText(String.valueOf(mlevel));


    }

    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(mindely,
                    (maxdelay - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (mplaying && balloonsLaunched < ballon_per_level) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(mscreanwidth - 200);
                publishProgress(xPosition);
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Balloon balloon = new Balloon(this, mballoncolore[mnextcolore], 150);
         mbaloon.add(balloon) ;
        if (mnextcolore + 1 == mballoncolore.length) {
            mnextcolore = 0;
        } else {
            mnextcolore++;
        }

//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x);
        balloon.setY(mscreanhight + balloon.getHeight());
        mcontentview.addView(balloon);

//      Let 'er fly
        int duration = Math.max(mindurioration, maxdurioration - (mlevel * 1000));
        balloon.release(mscreanhight, duration);

    }
}

