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
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.S2SRewardedVideoAdListener;
import com.facebook.samples.AdUnitsSample.R;

public class RewardedVideoFragment extends Fragment implements S2SRewardedVideoAdListener {

  private TextView rewardedVideoAdStatusLabel;
  private Button loadRewardedVideoButton;
  private Button showRewardedVideoButton;

  private RewardedVideoAd rewardedVideoAd;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_rewarded_video, container, false);

    rewardedVideoAdStatusLabel = (TextView) view.findViewById(R.id.rewardedVideoAdStatusLabel);
    loadRewardedVideoButton = (Button) view.findViewById(R.id.loadRewardedVideoButton);
    showRewardedVideoButton = (Button) view.findViewById(R.id.showRewardedVideoButton);

    loadRewardedVideoButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (rewardedVideoAd != null) {
              rewardedVideoAd.destroy();
              rewardedVideoAd = null;
            }
            rewardedVideoAd =
                new RewardedVideoAd(RewardedVideoFragment.this.getActivity(), "YOUR_PLACEMENT_ID");
            RewardedVideoAd.RewardedVideoLoadAdConfig loadAdConfig =
                rewardedVideoAd
                    .buildLoadAdConfig()
                    .withAdListener(RewardedVideoFragment.this)
                    .withFailOnCacheFailureEnabled(true)
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            rewardedVideoAd.loadAd(loadAdConfig);
            setStatusLabelText("Loading rewarded video ad...");
          }
        });

    showRewardedVideoButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (rewardedVideoAd == null
                || !rewardedVideoAd.isAdLoaded()
                || rewardedVideoAd.isAdInvalidated()) {
              setStatusLabelText("Ad not loaded. Click load to request an ad.");
            } else {
              rewardedVideoAd.show();
              setStatusLabelText("");
            }
          }
        });

    return view;
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == rewardedVideoAd) {
      setStatusLabelText("Rewarded video ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == rewardedVideoAd) {
      setStatusLabelText("Ad loaded. Click show to present!");
    }
  }

  @Override
  public void onAdClicked(Ad ad) {
    showToast("Rewarded Video Clicked");
  }

  private void setStatusLabelText(String label) {
    if (rewardedVideoAdStatusLabel != null) {
      rewardedVideoAdStatusLabel.setText(label);
    }
  }

  private void showToast(String message) {
    if (isAdded()) {
      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRewardedVideoCompleted() {
    showToast("Rewarded Video View Complete");
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    showToast("Rewarded Video Impression");
  }

  @Override
  public void onRewardedVideoClosed() {
    showToast("Rewarded Video Closed");
  }

  @Override
  public void onRewardServerFailed() {
    showToast("Reward Video Server Failed");
  }

  @Override
  public void onRewardServerSuccess() {
    showToast("Reward Video Server Succeeded");
  }
}
