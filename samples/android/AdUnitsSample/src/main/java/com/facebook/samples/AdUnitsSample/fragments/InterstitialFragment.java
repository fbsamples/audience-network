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
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.facebook.ads.RewardData;
import com.facebook.common.preconditions.Preconditions;
import com.facebook.infer.annotation.Nullsafe;
import com.facebook.samples.AdUnitsSample.R;
import com.facebook.samples.ads.debugsettings.DebugToast;
import java.util.EnumSet;
import javax.annotation.Nullable;

@Nullsafe(Nullsafe.Mode.LOCAL)
public class InterstitialFragment extends Fragment implements InterstitialAdExtendedListener {

  private static final String TAG = InterstitialFragment.class.getSimpleName();

  private TextView interstitialAdStatusLabel;
  private Button loadInterstitialButton;
  private Button showInterstitialButton;
  @Nullable private InterstitialAd interstitialAd;

  private String statusLabel = "";

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_interstitial, container, false);

    // NULLSAFE_FIXME[Field Not Nullable]
    interstitialAdStatusLabel = (TextView) view.findViewById(R.id.interstitialAdStatusLabel);
    // NULLSAFE_FIXME[Field Not Nullable]
    loadInterstitialButton = (Button) view.findViewById(R.id.loadInterstitialButton);
    // NULLSAFE_FIXME[Field Not Nullable]
    showInterstitialButton = (Button) view.findViewById(R.id.showInterstitialButton);

    loadInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (interstitialAd != null) {
              interstitialAd.destroy();
              interstitialAd = null;
            }
            setLabel("Loading interstitial ad...");

            // Create the interstitial unit with a placement ID (generate your own on the Facebook
            // app settings).
            // Use different ID for each ad placement in your app.
            interstitialAd =
                // NULLSAFE_FIXME[Parameter Not Nullable]
                new InterstitialAd(InterstitialFragment.this.getActivity(), "YOUR_PLACEMENT_ID");

            // Load a new interstitial.
            InterstitialAd.InterstitialLoadAdConfig loadAdConfig =
                interstitialAd
                    .buildLoadAdConfig()
                    // Set a listener to get notified on changes
                    // or when the user interact with the ad.
                    .withAdListener(InterstitialFragment.this)
                    .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            interstitialAd.loadAd(loadAdConfig);
          }
        });

    showInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (interstitialAd == null
                || !interstitialAd.isAdLoaded()
                || interstitialAd.isAdInvalidated()) {
              // Ad not ready to show.
              setLabel("Ad not loaded. Click load to request an ad.");
            } else {
              // Ad was loaded, show it!
              setLabel("");
              interstitialAd.show();
            }
          }
        });
    return view;
  }

  @Override
  public void onDestroy() {
    if (interstitialAd != null) {
      interstitialAd.destroy();
      interstitialAd = null;
    }
    super.onDestroy();
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == interstitialAd) {
      setLabel("Interstitial ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == interstitialAd) {
      setLabel("Ad loaded. Click show to present!");
    }
  }

  @Override
  public void onInterstitialDisplayed(Ad ad) {
    showToast("Interstitial Displayed");
  }

  @Override
  public void onInterstitialDismissed(Ad ad) {
    showToast("Interstitial Dismissed");

    // Cleanup.
    Preconditions.checkNotNull(interstitialAd).destroy();
    interstitialAd = null;
  }

  @Override
  public void onAdClicked(Ad ad) {
    showToast("Interstitial Clicked");
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    Log.d(TAG, "onLoggingImpression");
    showToast("Interstitial Impression");
  }

  private void setLabel(String label) {
    statusLabel = label;
    if (interstitialAdStatusLabel != null) {
      interstitialAdStatusLabel.setText(statusLabel);
    }
  }

  @Override
  public void onRewardedAdCompleted() {
    showToast("Reward Received");
  }

  @Override
  public void onRewardedAdServerSucceeded() {
    showToast("Server success!");
  }

  @Override
  public void onRewardedAdServerFailed() {
    showToast("Server failure");
  }

  @Override
  public void onInterstitialActivityDestroyed() {
    showToast("Activity destroyed");
  }

  private void showToast(String message) {
    Log.d(TAG, message);
    if (isAdded()) {
      DebugToast.show(requireActivity(), message, Toast.LENGTH_SHORT);
    }
  }
}
