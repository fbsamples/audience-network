/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity
import com.facebook.samples.adunitssamplekotlin.fragments.*

class AdUnitsSampleActivity : AppCompatActivity() {
  companion object {
    val TAG: String = AdUnitsSampleActivity::class.java.simpleName
    const val SAMPLE_TYPE: String = "SAMPLE_TYPE"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_ad_sample)

    if (savedInstanceState != null) {
      return
    }

    val sampleType = intent.getStringExtra(SAMPLE_TYPE)
    if (sampleType != null) {
      val type: AdUnitsSampleType? = AdUnitsSampleType.getSampleTypeFromName(sampleType)
      if (type != null) {
        val fragment: Fragment? =
            when (type) {
              AdUnitsSampleType.BANNER -> BannerFragment()
              AdUnitsSampleType.INTERSTITIAL -> InterstitialFragment()
              AdUnitsSampleType.RECTANGLE -> RectangleFragment()
              AdUnitsSampleType.REWARDED_VIDEO -> RewardedVideoFragment()
              AdUnitsSampleType.REWARDED_INTERSTITIAL -> RewardedInterstitialFragment()
              AdUnitsSampleType.NATIVE -> NativeAdSampleFragment()
              AdUnitsSampleType.NATIVE_BANNER -> NativeBannerAdFragment()
              AdUnitsSampleType.RECYCLERVIEW -> NativeAdRecyclerFragment()
              AdUnitsSampleType.HSCROLL -> NativeAdHScrollFragment()
              AdUnitsSampleType.TEMPLATE -> NativeAdTemplateFragment()
              AdUnitsSampleType.BANNER_TEMPLATE -> NativeBannerAdTemplateFragment()
            }
        if (fragment != null) {
          fragment.setRetainInstance(true)
          title = type.sampleType
          supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.ad_units_sample_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val i = item.itemId
    if (i == R.id.debug_settings) {
      startActivity(Intent(applicationContext, DebugSettingsActivity::class.java))
      return true
    }

    return super.onOptionsItemSelected(item)
  }
}
