/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.samples.AdUnitsSample.R;

public class RectangleFragment extends Fragment implements AdListener {

  private static final String TAG = RectangleFragment.class.getSimpleName();

  private RelativeLayout rectangleAdContainer;
  private Button refreshRectangleButton;
  private TextView rectangleStatusLabel;
  private @Nullable AdView rectangleAdView;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_rectangle, container, false);

    rectangleStatusLabel = (TextView) view.findViewById(R.id.rectangleStatusLabel);
    rectangleAdContainer = (RelativeLayout) view.findViewById(R.id.rectangleAdContainer);
    refreshRectangleButton = (Button) view.findViewById(R.id.refreshRectangleButton);
    refreshRectangleButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadAdView();
          }
        });
    loadAdView();
    return view;
  }

  @Override
  public void onDestroyView() {
    rectangleAdContainer.removeView(rectangleAdView);
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    if (rectangleAdView != null) {
      rectangleAdView.destroy();
      rectangleAdView = null;
    }
    super.onDestroy();
  }

  private void loadAdView() {
    if (rectangleAdView != null) {
      rectangleAdView.destroy();
      rectangleAdView = null;
    }

    // Update progress message
    setLabel(getString(R.string.loading_status));

    // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
    // app settings). Use different ID for each ad placement in your app.
    rectangleAdView = new AdView(getActivity(), "YOUR_PLACEMENT_ID", AdSize.RECTANGLE_HEIGHT_250);

    // Reposition the ad and add it to the view hierarchy.
    rectangleAdContainer.addView(rectangleAdView);

    // Initiate a request to load an ad.
    rectangleAdView.loadAd(rectangleAdView.buildLoadAdConfig().withAdListener(this).build());
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == rectangleAdView) {
      setLabel("Ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == rectangleAdView) {
      setLabel("");
    }
  }

  @Override
  public void onAdClicked(Ad ad) {
    Toast.makeText(this.getActivity(), "Ad Clicked", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    Log.d(TAG, "onLoggingImpression");
  }

  private void setLabel(String status) {
    rectangleStatusLabel.setText(status);
    if (status.isEmpty()) {
      rectangleStatusLabel.setVisibility(View.GONE);
    } else {
      rectangleStatusLabel.setVisibility(View.VISIBLE);
    }
  }
}
