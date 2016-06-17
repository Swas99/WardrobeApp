package com.swastik.wardrobeapp.modules.get_started;

import android.animation.Animator;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.swastik.wardrobeapp.modules.MainActivity;
import com.swastik.wardrobeapp.R;
import com.swastik.wardrobeapp.utils.CommonUtil;

import java.lang.ref.WeakReference;

/**
 * Created by Swastik on 15-06-2016.
 */
public class GetStarted
{
    MainActivity objMainActivity;

    public GetStarted(WeakReference<MainActivity> _MainActivityWeakReference)
    {
        objMainActivity = _MainActivityWeakReference.get();
    }

    public void init()
    {

        objMainActivity.setContentView(R.layout.screen_get_started);
        animateLogo();
        if(!objMainActivity.isLandscape)
            animateAboutText();
        animateGetStartedButton();

        objMainActivity.findViewById(R.id.btnGetStarted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objMainActivity.loadWardrobeScreen();
            }
        });
    }

    private void animateLogo() {
        final View logo = objMainActivity.findViewById(R.id.img_logo);

        logo.setScaleX(-0.27f);
        logo.setScaleY(-0.27f);
        logo.animate().setDuration(1450).setInterpolator(new OvershootInterpolator(2.7f));
        logo.animate().rotation(360).scaleX(1.0f).scaleY(1.0f).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                logo.animate().setDuration(810).setInterpolator(new LinearInterpolator());
                logo.animate().y(45);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animateAboutText() {
        final View tvAbout = objMainActivity.findViewById(R.id.tvAbout);

        new CountDownTimer(1450, 1450) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                float buffer = 99;
                float targetY = objMainActivity.findViewById(R.id.img_logo).getHeight()+72f;
                tvAbout.setVisibility(View.VISIBLE);
                tvAbout.setY(CommonUtil.getScreenHeight(objMainActivity) + buffer);
                tvAbout.animate().setDuration(1080).setInterpolator(new LinearInterpolator());
                tvAbout.animate().y(targetY);
            }
        }.start();
    }

    private void animateGetStartedButton() {
        final View btnGetStarted = objMainActivity.findViewById(R.id.btnGetStarted);

        new CountDownTimer(2600, 2600) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                float targetY = btnGetStarted.getY();
                btnGetStarted.setVisibility(View.VISIBLE);
//                btnGetStarted.setY(CommonUtil.getScreenHeight(objMainActivity) + buffer);
                btnGetStarted.animate().setDuration(360).setInterpolator(new OvershootInterpolator(1.8f));
//                btnGetStarted.animate().y(targetY);
                btnGetStarted.setScaleY(0);
                btnGetStarted.animate().scaleY(1);
            }
        }.start();
    }
}
