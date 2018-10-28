package com.example.dell.v;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class WelcomeActivity extends Activity {

    private static final int ANIM_TIME = 2000;
    private static final float SCALE_END = 1.2F;
    private static final int[] Imgs = {R.drawable.welcome_page, R.drawable.welcome_page_2};
    ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        img = findViewById(R.id.welcome_pic);
        startMainActivity();
    }

    private void startMainActivity(){
        Random random = new Random(SystemClock.elapsedRealtime());
        img.setImageResource(Imgs[random.nextInt(Imgs.length)]);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {

                    public void call(Long aLong)
                    {
                        startAnim();
                    }
                });

    }

    private void startAnim() {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(img, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(img, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIM_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {
                startActivity(new Intent(WelcomeActivity.this, Home.class));
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_out);
                WelcomeActivity.this.finish();
            }
        });
    }
}

