/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import androidx.annotation.Nullable;

public enum AdUnitsSampleType {
  BANNER("Banner"),
  RECTANGLE("Rectangle"),
  INTERSTITIAL("Interstitial"),
  REWARDED_VIDEO("Rewarded Video"),
  REWARDED_INTERSTITIAL("Rewarded Interstitial"),
  NATIVE("Native Ad"),
  NATIVE_BANNER("Native Banner Ad"),
  RECYCLERVIEW("Native Ad in RecyclerView"),
  HSCROLL("Native Ad in H-Scroll"),
  TEMPLATE("Native Ad Template"),
  BANNER_TEMPLATE("Native Banner Ad Template"),
  NATIVE_BANNER_WITH_IMAGE_VIEW("Native Banner Ad With ImageView");

  private final String mName;

  AdUnitsSampleType(String mName) {
    this.mName = mName;
  }

  public String getName() {
    return this.mName;
  }

  public static @Nullable AdUnitsSampleType getSampleTypeFromName(String name) {
    for (AdUnitsSampleType type : AdUnitsSampleType.values()) {
      if (type.getName().contentEquals(name)) {
        return type;
      }
    }
    return null;
  }
}
