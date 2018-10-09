package com.cby.benstagram.Util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class HeartToggle {
    private static final String TAG = "HeartToggle";

    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    public ImageView imgHeartWhite, imgHeartRed;

    public HeartToggle() {
        Log.d(TAG, "HeartToggle: created");
    }

    public HeartToggle(ImageView imgHeartWhite, ImageView imgHeartRed) {
        Log.d(TAG, "HeartToggle: created");
        this.imgHeartWhite = imgHeartWhite;
        this.imgHeartRed = imgHeartRed;

        setToUnLike();
    }

    public boolean isLikedState(){
        return imgHeartRed.getVisibility() == View.VISIBLE;
    }

    public void toggle(){
        Log.d(TAG, "toggle: toggle start");

        // Like 상태
        if (imgHeartRed.getVisibility() == View.VISIBLE){
            Log.d(TAG, "changeToLike: toggle Like to UnLike");

            changeToUnLike();

        }else if (imgHeartRed.getVisibility() == View.GONE){
            Log.d(TAG, "changeToLike: toggle UnLike to Like");

            changeToLike();
        }
    }

    public void setToLike(){
        imgHeartRed.setVisibility(View.VISIBLE);
        imgHeartWhite.setVisibility(View.GONE);
    }

    public void setToUnLike(){
        imgHeartRed.setVisibility(View.GONE);
        imgHeartWhite.setVisibility(View.VISIBLE);
    }

    private void changeToLike() {

        if (imgHeartRed.getVisibility() == View.VISIBLE) return;

        AnimatorSet animatorSet = new AnimatorSet();

        imgHeartRed.setScaleX(0.1f);
        imgHeartRed.setScaleY(0.1f);

        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgHeartRed , "scaleY" , 0.1f , 1f);
        scaleDownY.setDuration(300);
        scaleDownY.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgHeartRed , "scaleX" , 0.1f , 1f);
        scaleDownX.setDuration(300);
        scaleDownX.setInterpolator(DECELERATE_INTERPOLATOR);

        imgHeartRed.setVisibility(View.VISIBLE);
        imgHeartWhite.setVisibility(View.GONE);

        animatorSet.playTogether(scaleDownY, scaleDownX);
        animatorSet.start();
    }

    private void changeToUnLike() {

        if (imgHeartWhite.getVisibility() == View.VISIBLE) return;

        AnimatorSet animatorSet = new AnimatorSet();

        imgHeartRed.setScaleX(0.1f);
        imgHeartRed.setScaleY(0.1f);

        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(imgHeartRed , "scaleY" , 1f , 0f);
        scaleDownY.setDuration(300);
        scaleDownY.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(imgHeartRed , "scaleX" , 1f , 0f);
        scaleDownX.setDuration(300);
        scaleDownX.setInterpolator(ACCELERATE_INTERPOLATOR);

        imgHeartRed.setVisibility(View.GONE);
        imgHeartWhite.setVisibility(View.VISIBLE);

        animatorSet.playTogether(scaleDownY, scaleDownX);
        animatorSet.start();
    }
}
