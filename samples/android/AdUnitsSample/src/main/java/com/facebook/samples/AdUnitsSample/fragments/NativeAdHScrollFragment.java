/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdsManager;
import com.facebook.samples.AdUnitsSample.R;

public class NativeAdHScrollFragment extends Fragment implements NativeAdsManager.Listener {

  private static final int NATIVE_AD_VIEW_HEIGHT_DP = 300;

  private NativeAdsManager manager;
  private @Nullable NativeAdScrollView scrollView;
  private LinearLayout scrollViewContainer;

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_native_ad_hscroll, container, false);

    manager = new NativeAdsManager(getActivity(), "YOUR_PLACEMENT_ID", 5);
    manager.setListener(this);
    manager.loadAds(NativeAd.MediaCacheFlag.ALL);

    Button reloadButton = (Button) view.findViewById(R.id.reload_hscroll);
    reloadButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            manager.loadAds();
          }
        });

    scrollViewContainer = (LinearLayout) view.findViewById(R.id.hscroll_container);

    return view;
  }

  @Override
  public void onAdsLoaded() {
    if (getActivity() == null) {
      return;
    }

    Toast.makeText(getActivity(), "Ads loaded", Toast.LENGTH_SHORT).show();

    if (scrollView != null) {
      scrollViewContainer.removeView(scrollView);
    }

    scrollView = new NativeAdScrollView(getActivity(), manager, NATIVE_AD_VIEW_HEIGHT_DP);

    scrollViewContainer.addView(scrollView);
  }

  @Override
  public void onAdError(AdError error) {
    if (getActivity() != null) {
      Toast.makeText(getActivity(), "Ad error: " + error.getErrorMessage(), Toast.LENGTH_SHORT)
          .show();
    }
  }
}
