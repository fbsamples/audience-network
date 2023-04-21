/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {

  private static final int SPLASH_TIME = 2000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
    // in Application.onCreate() this call is not really necessary.
    // Otherwise call initialize() onCreate() of all Activities that contain ads or
    // from onCreate() of your Splash Activity.
    AudienceNetworkInitializeHelper.initialize(this);

    // Hide title and nav bar, must be done before setContentView.
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow()
        .setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_splash);

    final Handler handler = new Handler(Looper.getMainLooper());
    handler.postDelayed(
        new Runnable() {
          @Override
          public void run() {
            Intent intent = new Intent(SplashActivity.this, SampleListActivity.class);
            startActivity(intent);
          }
        },
        SPLASH_TIME);
  }
}
