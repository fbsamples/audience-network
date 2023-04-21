/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import android.app.Application;
import com.facebook.samples.ads.debugsettings.DebugSettings;

public class AdUnitsSampleApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    DebugSettings.initialize(this);

    AudienceNetworkInitializeHelper.initialize(this);
  }
}
