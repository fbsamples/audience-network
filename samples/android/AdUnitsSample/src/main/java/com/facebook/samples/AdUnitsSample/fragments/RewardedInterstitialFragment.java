/*
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 *
 * <p>You are hereby granted a non-exclusive, worldwide, royalty-free license to use, copy, modify,
 * and distribute this software in source code or binary form for use in connection with the web
 * services and APIs provided by Facebook.
 *
 * <p>As with any software that integrates with the Facebook platform, your use of this software is
 * subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be included in all copies
 * or substantial portions of the software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
import com.facebook.ads.RewardedInterstitialAd;
import com.facebook.ads.S2SRewardedInterstitialAdListener;
import com.facebook.samples.AdUnitsSample.R;

public class RewardedInterstitialFragment extends Fragment
    implements S2SRewardedInterstitialAdListener {

  private TextView rewardedInterstitialAdStatusLabel;
  private Button loadRewardedInterstitialButton;
  private Button showRewardedInterstitialButton;

  private RewardedInterstitialAd rewardedInterstitialAd;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_rewarded_interstitial, container, false);

    rewardedInterstitialAdStatusLabel =
        (TextView) view.findViewById(R.id.rewardedInterstitialAdStatusLabel);
    loadRewardedInterstitialButton =
        (Button) view.findViewById(R.id.loadRewardedInterstitialButton);
    showRewardedInterstitialButton =
        (Button) view.findViewById(R.id.showRewardedInterstitialButton);

    loadRewardedInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (rewardedInterstitialAd != null) {
              rewardedInterstitialAd.destroy();
              rewardedInterstitialAd = null;
            }
            rewardedInterstitialAd =
                new RewardedInterstitialAd(
                    RewardedInterstitialFragment.this.getActivity(), "YOUR_PLACEMENT_ID");
            RewardedInterstitialAd.RewardedInterstitialLoadAdConfig loadAdConfig =
                rewardedInterstitialAd
                    .buildLoadAdConfig()
                    .withAdListener(RewardedInterstitialFragment.this)
                    .withFailOnCacheFailureEnabled(true)
                    .withRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD", 10))
                    .build();
            rewardedInterstitialAd.loadAd(loadAdConfig);
            setStatusLabelText("Loading rewarded interstitial ad...");
          }
        });

    showRewardedInterstitialButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (rewardedInterstitialAd == null
                || !rewardedInterstitialAd.isAdLoaded()
                || rewardedInterstitialAd.isAdInvalidated()) {
              setStatusLabelText("Ad not loaded. Click load to request an ad.");
            } else {
              rewardedInterstitialAd.show();
              setStatusLabelText("");
            }
          }
        });

    return view;
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == rewardedInterstitialAd) {
      setStatusLabelText("Rewarded interstitial ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == rewardedInterstitialAd) {
      setStatusLabelText("Ad loaded. Click show to present!");
    }
  }

  @Override
  public void onAdClicked(Ad ad) {
    showToast("Rewarded Interstitial Clicked");
  }

  private void setStatusLabelText(String label) {
    if (rewardedInterstitialAdStatusLabel != null) {
      rewardedInterstitialAdStatusLabel.setText(label);
    }
  }

  private void showToast(String message) {
    if (isAdded()) {
      Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onRewardedInterstitialCompleted() {
    showToast("Rewarded Interstitial View Complete");
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    showToast("Rewarded Interstitial Impression");
  }

  @Override
  public void onRewardedInterstitialClosed() {
    showToast("Rewarded Interstitial Closed");
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
