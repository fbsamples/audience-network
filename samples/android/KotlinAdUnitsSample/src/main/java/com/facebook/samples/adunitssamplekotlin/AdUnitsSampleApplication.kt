/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin

import android.app.Application
import android.os.StrictMode
import com.facebook.samples.ads.debugsettings.DebugSettings
import com.facebook.samples.adunitssamplekotlin.BuildConfig.DEBUG

class AdUnitsSampleApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    DebugSettings.initialize(this)

    AudienceNetworkInitializeHelper.initialize(this)

    if (DEBUG) {
      StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build())
      StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build())
    }
  }
}
