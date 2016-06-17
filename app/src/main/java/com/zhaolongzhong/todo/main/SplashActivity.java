package com.zhaolongzhong.todo.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zhaolongzhong.todo.R;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        View view = findViewById(android.R.id.content);

        if (view != null) {
            Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            loadAnimation.setDuration(1500);
            view.startAnimation(loadAnimation);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (handler == null) {
            handler = new Handler();
        }

        handler.postDelayed(runnable, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeCallbacks();
    }

    @Override
    public void onBackPressed() {
        removeCallbacks();
        finish();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MainActivity.newInstance(SplashActivity.this);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    };

    private void removeCallbacks() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
