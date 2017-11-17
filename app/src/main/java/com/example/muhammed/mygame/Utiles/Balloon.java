package com.example.muhammed.mygame.Utiles;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.muhammed.mygame.MainActivity;
import com.example.muhammed.mygame.R;



public class Balloon extends android.support.v7.widget.AppCompatImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
  private ValueAnimator anmi;
  private BalloonListner mlisner;
  private boolean mpoped=false;
/////////////////////////////////////////////////construstore//////////////
    public Balloon(Context context) {
        super(context);
    }
    ///////////////////////////////////////////////////
    public Balloon(Context context,int colore ,int rawhight) {
        super(context);
        mlisner= (BalloonListner) context;
        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(colore);
        int rawwidth=rawhight/2;
         int dphight=PixelHelper.pixelsToDp(rawhight,context);
         int dpwidth=PixelHelper.pixelsToDp(rawwidth,context);
        ViewGroup.LayoutParams prames=new ViewGroup.LayoutParams(dpwidth,dphight);
        setLayoutParams(prames);

    }
    /////////////////////////////////////ballon animation//////////////////////
    public void release(int screanhight, int durion){
        anmi=new ValueAnimator();
        anmi.setDuration(durion);
        anmi.setFloatValues(screanhight,0f);
        anmi.setInterpolator(new LinearInterpolator());
        anmi.setTarget(this);
        anmi.addListener(this);
        anmi.addUpdateListener(this);
        anmi.start();

    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if(!mpoped)
        {
            mlisner.popbaloon(this,false);
        }

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mpoped &&event.getAction()==MotionEvent.ACTION_DOWN)
        {
            mlisner.popbaloon(this,true);
            mpoped=true;
            anmi.cancel();

        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY((Float) valueAnimator.getAnimatedValue());

    }

                                    public void setpoped(boolean b) {
                                    }
//////////////////////////////////////////////////////////
              public  interface BalloonListner
                        {
                            void popbaloon(Balloon balloon,boolean usertouch);
                        }
}
