package com.sample.nativetransition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    private static final int UI_ANIMATION_DELAY = 3000;
    private final Handler mHideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        NativeAdLoader.getInstance().setContext(getApplicationContext());
        NativeAdLoader.getInstance().loadAd(getString(R.string.native_placement_id));

        mHideHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NativeAdActivity.class);
                startActivity(intent);
            }
        }, UI_ANIMATION_DELAY);
    }
}
