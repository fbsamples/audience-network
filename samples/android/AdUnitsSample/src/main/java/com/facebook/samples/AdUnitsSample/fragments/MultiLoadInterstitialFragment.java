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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.CacheFlag;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.facebook.ads.RewardData;
import com.facebook.samples.AdUnitsSample.R;
import java.util.EnumSet;

public class MultiLoadInterstitialFragment extends Fragment
    implements InterstitialAdExtendedListener {

  private static final String TAG = MultiLoadInterstitialFragment.class.getSimpleName();

  @Nullable private TextView mInterstitialAdStatusLabel;
  @Nullable private Button mLoadInterstitialButton;
  @Nullable private Button mShowInterstitialButton;
  @Nullable private InterstitialAd mInterstitialAd;

  @Nullable private TextView mInterstitialAdStatusLabel2;
  @Nullable private Button mLoadInterstitialButton2;
  @Nullable private Button mShowInterstitialButton2;
  @Nullable private InterstitialAd mInterstitialAd2;

  @Nullable private TextView mInterstitialAdStatusLabel3;
  @Nullable private Button mLoadInterstitialButton3;
  @Nullable private Button mShowInterstitialButton3;
  @Nullable private InterstitialAd mInterstitialAd3;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_multi_load_interstitial, container, false);

    mInterstitialAdStatusLabel = (TextView) view.findViewById(R.id.interstitialAdStatusLabel);
    mLoadInterstitialButton = (Button) view.findViewById(R.id.loadInterstitialButton);
    mShowInterstitialButton = (Button) view.findViewById(R.id.showInterstitialButton);

    if (mInterstitialAdStatusLabel == null
        || mLoadInterstitialButton == null
        || mShowInterstitialButton == null) {
      return view;
    }
    mLoadInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd != null) {
              mInterstitialAd.destroy();
              mInterstitialAd = null;
            }
            setLabel(mInterstitialAdStatusLabel, "Loading interstitial ad...");
            // Create the interstitial unit with a placement ID (generate your own on the Facebook
            // app settings).
            // Use different ID for each ad placement in your app.
            mInterstitialAd =
                new InterstitialAd(
                    MultiLoadInterstitialFragment.this.requireActivity(), "YOUR_PLACEMENT_ID");

            // Load a new interstitial.
            InterstitialAd.InterstitialLoadAdConfig loadAdConfig =
                mInterstitialAd
                    .buildLoadAdConfig()
                    // Set a listener to get notified on changes
                    // or when the user interact with the ad.
                    .withAdListener(MultiLoadInterstitialFragment.this)
                    .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            mInterstitialAd.loadAd(loadAdConfig);
          }
        });

    mShowInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd == null
                || !mInterstitialAd.isAdLoaded()
                || mInterstitialAd.isAdInvalidated()) {
              // Ad not ready to show.
              setLabel(mInterstitialAdStatusLabel, "Ad not loaded. Click load to request an ad.");
            } else {
              // Ad was loaded, show it!
              mInterstitialAd.show();
              setLabel(mInterstitialAdStatusLabel, "");
            }
          }
        });

    mInterstitialAdStatusLabel2 = (TextView) view.findViewById(R.id.interstitialAdStatusLabel2);
    mLoadInterstitialButton2 = (Button) view.findViewById(R.id.loadInterstitialButton2);
    mShowInterstitialButton2 = (Button) view.findViewById(R.id.showInterstitialButton2);

    if (mInterstitialAdStatusLabel2 == null
        || mLoadInterstitialButton2 == null
        || mShowInterstitialButton2 == null) {
      return view;
    }

    mLoadInterstitialButton2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd2 != null) {
              mInterstitialAd2.destroy();
              mInterstitialAd2 = null;
            }
            setLabel(mInterstitialAdStatusLabel2, "Loading interstitial ad...");
            // Create the interstitial unit with a placement ID (generate your own on the Facebook
            // app settings).
            // Use different ID for each ad placement in your app.
            mInterstitialAd2 =
                new InterstitialAd(
                    MultiLoadInterstitialFragment.this.requireActivity(), "YOUR_PLACEMENT_ID");

            // Load a new interstitial.
            InterstitialAd.InterstitialLoadAdConfig loadAdConfig =
                mInterstitialAd2
                    .buildLoadAdConfig()
                    // Set a listener to get notified on changes
                    // or when the user interact with the ad.
                    .withAdListener(MultiLoadInterstitialFragment.this)
                    .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            mInterstitialAd2.loadAd(loadAdConfig);
          }
        });

    mShowInterstitialButton2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd2 == null
                || !mInterstitialAd2.isAdLoaded()
                || mInterstitialAd2.isAdInvalidated()) {
              // Ad not ready to show.
              setLabel(mInterstitialAdStatusLabel2, "Ad not loaded. Click load to request an ad.");

            } else {
              // Ad was loaded, show it!
              mInterstitialAd2.show();
              setLabel(mInterstitialAdStatusLabel2, "");
            }
          }
        });

    mInterstitialAdStatusLabel3 = (TextView) view.findViewById(R.id.interstitialAdStatusLabel3);
    mLoadInterstitialButton3 = (Button) view.findViewById(R.id.loadInterstitialButton3);
    mShowInterstitialButton3 = (Button) view.findViewById(R.id.showInterstitialButton3);

    if (mInterstitialAdStatusLabel3 == null
        || mLoadInterstitialButton3 == null
        || mShowInterstitialButton3 == null) {
      return view;
    }

    mLoadInterstitialButton3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd3 != null) {
              mInterstitialAd3.destroy();
              mInterstitialAd3 = null;
            }
            setLabel(mInterstitialAdStatusLabel3, "Loading interstitial ad...");
            // Create the interstitial unit with a placement ID (generate your own on the Facebook
            // app settings).
            // Use different ID for each ad placement in your app.
            mInterstitialAd3 =
                new InterstitialAd(
                    MultiLoadInterstitialFragment.this.requireActivity(), "YOUR_PLACEMENT_ID");

            // Load a new interstitial.
            InterstitialAd.InterstitialLoadAdConfig loadAdConfig =
                mInterstitialAd3
                    .buildLoadAdConfig()
                    // Set a listener to get notified on changes
                    // or when the user interact with the ad.
                    .withAdListener(MultiLoadInterstitialFragment.this)
                    .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            mInterstitialAd3.loadAd(loadAdConfig);
          }
        });

    mShowInterstitialButton3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (mInterstitialAd3 == null
                || !mInterstitialAd3.isAdLoaded()
                || mInterstitialAd3.isAdInvalidated()) {
              // Ad not ready to show.
              setLabel(mInterstitialAdStatusLabel3, "Ad not loaded. Click load to request an ad.");
            } else {
              // Ad was loaded, show it!
              mInterstitialAd3.show();
              setLabel(mInterstitialAdStatusLabel3, "");
            }
          }
        });

    return view;
  }

  private void destroy(@Nullable InterstitialAd interstitialAd) {
    if (interstitialAd != null) {
      interstitialAd.destroy();
      interstitialAd = null;
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();

    destroy(mInterstitialAd);
    destroy(mInterstitialAd2);
    destroy(mInterstitialAd3);
    mInterstitialAdStatusLabel = null;
    mInterstitialAdStatusLabel2 = null;
    mInterstitialAdStatusLabel3 = null;
    mLoadInterstitialButton = null;
    mLoadInterstitialButton2 = null;
    mLoadInterstitialButton3 = null;
    mShowInterstitialButton = null;
    mShowInterstitialButton2 = null;
    mShowInterstitialButton3 = null;
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == mInterstitialAd) {
      setLabel(
          mInterstitialAdStatusLabel, "Interstitial ad failed to load: " + error.getErrorMessage());
    } else if (ad == mInterstitialAd2) {

      setLabel(
          mInterstitialAdStatusLabel2,
          "Interstitial ad failed to load: " + error.getErrorMessage());
    } else if (ad == mInterstitialAd3) {

      setLabel(
          mInterstitialAdStatusLabel3,
          "Interstitial ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == mInterstitialAd) {
      setLabel(mInterstitialAdStatusLabel, "Ad loaded. Click show to present!");
    } else if (ad == mInterstitialAd2) {

      setLabel(mInterstitialAdStatusLabel2, "Ad loaded. Click show to present!");
    } else if (ad == mInterstitialAd3) {

      setLabel(mInterstitialAdStatusLabel3, "Ad loaded. Click show to present!");
    }
  }

  @Override
  public void onInterstitialDisplayed(Ad ad) {
    if (isAdded()) {
      Toast.makeText(getActivity(), "Interstitial Displayed", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onInterstitialDismissed(Ad ad) {
    if (isAdded()) {
      Toast.makeText(getActivity(), "Interstitial Dismissed", Toast.LENGTH_SHORT).show();
    }

    // Cleanup.
    if (ad == mInterstitialAd) {
      destroy(mInterstitialAd);
    } else if (ad == mInterstitialAd2) {

      destroy(mInterstitialAd2);
    } else if (ad == mInterstitialAd3) {

      destroy(mInterstitialAd3);
    }
  }

  @Override
  public void onAdClicked(Ad ad) {
    if (isAdded()) {
      Toast.makeText(getActivity(), "Interstitial Clicked", Toast.LENGTH_SHORT).show();
    }
  }

  /** showing toast for tracking impression for manual testing */
  @Override
  public void onLoggingImpression(Ad ad) {
    Log.d(TAG, "onLoggingImpression");
    Toast.makeText(getActivity(), "Interstitial Impression", Toast.LENGTH_SHORT).show();
  }

  private void setLabel(@Nullable TextView interstitialAdStatusLabel, String label) {
    if (interstitialAdStatusLabel != null) {
      interstitialAdStatusLabel.setText(label);
    }
  }

  @Override
  public void onRewardedAdCompleted() {}

  @Override
  public void onRewardedAdServerSucceeded() {}

  @Override
  public void onRewardedAdServerFailed() {}

  @Override
  public void onInterstitialActivityDestroyed() {
    if (isAdded()) {
      Toast.makeText(getActivity(), "Activity destroyed", Toast.LENGTH_SHORT).show();
    }
  }
}
