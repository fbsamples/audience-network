/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.facebook.samples.AdUnitsSample.fragments.BannerFragment;
import com.facebook.samples.AdUnitsSample.fragments.InterstitialFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdHScrollFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdRecyclerFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdSampleFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdTemplateFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeBannerAdFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeBannerAdTemplateFragment;
import com.facebook.samples.AdUnitsSample.fragments.RectangleFragment;
import com.facebook.samples.AdUnitsSample.fragments.RewardedInterstitialFragment;
import com.facebook.samples.AdUnitsSample.fragments.RewardedVideoFragment;
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity;

public class AdUnitsSampleActivity extends FragmentActivity {

  private static final String TAG = AdUnitsSampleActivity.class.getSimpleName();
  public static final String SAMPLE_TYPE = "SAMPLE_TYPE";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
    // in Application.onCreate() this call is not really necessary.
    // Otherwise call initialize() onCreate() of all Activities that contain ads or
    // from onCreate() of your Splash Activity.
    AudienceNetworkInitializeHelper.initialize(this);

    setContentView(R.layout.activity_ad_sample);

    if (savedInstanceState != null) {
      return;
    }

    Intent intent = getIntent();
    String sampleType = intent.getStringExtra(SAMPLE_TYPE);
    Fragment fragment = null;

    if (sampleType == null) {
      return;
    }

    // Basic ad unit sample types
    AdUnitsSampleType type = AdUnitsSampleType.getSampleTypeFromName(sampleType);
    if (type != null) {
      switch (type) {
        case BANNER:
          fragment = new BannerFragment();
          break;
        case RECTANGLE:
          fragment = new RectangleFragment();
          break;
        case INTERSTITIAL:
          fragment = new InterstitialFragment();
          break;
        case REWARDED_VIDEO:
          fragment = new RewardedVideoFragment();
          break;
        case REWARDED_INTERSTITIAL:
          fragment = new RewardedInterstitialFragment();
          break;
        case NATIVE:
          fragment = new NativeAdSampleFragment();
          break;
        case NATIVE_BANNER:
          fragment = NativeBannerAdFragment.newInstance(false);
          break;
        case NATIVE_BANNER_WITH_IMAGE_VIEW:
          fragment = NativeBannerAdFragment.newInstance(true);
          break;
        case RECYCLERVIEW:
          fragment = new NativeAdRecyclerFragment();
          break;
        case HSCROLL:
          fragment = new NativeAdHScrollFragment();
          break;
        case TEMPLATE:
          fragment = new NativeAdTemplateFragment();
          break;
        case BANNER_TEMPLATE:
          fragment = new NativeBannerAdTemplateFragment();
          break;
      }
      fragment.setRetainInstance(true);
      setTitle(type.getName());
      getSupportFragmentManager()
          .beginTransaction()
          .add(R.id.fragment_container, fragment)
          .commit();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.ad_units_sample_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int i = item.getItemId();
    if (i == R.id.debug_settings) {
      startActivity(new Intent(getApplicationContext(), DebugSettingsActivity.class));
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
