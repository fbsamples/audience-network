/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager

class SplashActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
    // in Application.onCreate() this call is not really necessary.
    // Otherwise call initialize() onCreate() of all Activities that contain ads or
    // from onCreate() of your Splash Activity.
    AudienceNetworkInitializeHelper.initialize(this)

    // Hide title and nav bar, must be done before setContentView.
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

    setContentView(R.layout.activity_splash)

    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed(
        {
          val intent = Intent(this@SplashActivity, SampleListActivity::class.java)
          startActivity(intent)
        },
        SPLASH_TIME.toLong())
  }

  companion object {
    private const val SPLASH_TIME = 2000
  }
}
