/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.RewardData
import com.facebook.ads.RewardedInterstitialAd
import com.facebook.ads.S2SRewardedInterstitialAdListener
import com.facebook.samples.adunitssamplekotlin.R

class RewardedInterstitialFragment : Fragment(), S2SRewardedInterstitialAdListener {

  private lateinit var rewardedInterstitialAdStatusLabel: TextView
  private lateinit var loadRewardedInterstitialButton: Button
  private lateinit var showRewardedInterstitialButton: Button

  private var rewardedInterstitialAd: RewardedInterstitialAd? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_rewarded_interstitial, container, false)

    rewardedInterstitialAdStatusLabel = view.findViewById(R.id.rewardedInterstitialAdStatusLabel)
    loadRewardedInterstitialButton = view.findViewById(R.id.loadRewardedInterstitialButton)
    showRewardedInterstitialButton = view.findViewById(R.id.showRewardedInterstitialButton)

    loadRewardedInterstitialButton.setOnClickListener {
      rewardedInterstitialAd?.destroy()
      rewardedInterstitialAd = null

      rewardedInterstitialAd =
          RewardedInterstitialAd(this@RewardedInterstitialFragment.activity, "YOUR_PLACEMENT_ID")

      rewardedInterstitialAd?.let { rewardedInterstitialAd ->
        setStatusLabelText("Loading rewarded interstitial ad...")
        rewardedInterstitialAd.loadAd(
            rewardedInterstitialAd
                .buildLoadAdConfig()
                .withAdListener(this)
                .withFailOnCacheFailureEnabled(true)
                .withRewardData(RewardData("YOUR_USER_ID", "YOUR_REWARD"))
                .build())
      }
    }

    showRewardedInterstitialButton.setOnClickListener {
      if (rewardedInterstitialAd?.isAdLoaded == true) {
        rewardedInterstitialAd?.show()
        setStatusLabelText("")
      } else {
        setStatusLabelText("Ad not loaded. Click load to request an ad.")
      }
    }

    return view
  }

  override fun onError(ad: Ad, error: AdError) {
    if (ad === rewardedInterstitialAd) {
      setStatusLabelText("Rewarded interstitial ad failed to load: " + error.errorMessage)
    }
  }

  override fun onAdLoaded(ad: Ad) {
    if (ad === rewardedInterstitialAd) {
      setStatusLabelText("Ad loaded. Click show to present!")
    }
  }

  override fun onAdClicked(ad: Ad) {
    showToast("Rewarded Interstitial Clicked")
  }

  private fun setStatusLabelText(label: String) {
    rewardedInterstitialAdStatusLabel.text = label
  }

  private fun showToast(message: String) {
    if (isAdded) {
      Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
  }

  override fun onRewardedInterstitialCompleted() {
    showToast("Rewarded Interstitial View Complete")
  }

  override fun onLoggingImpression(ad: Ad) {
    showToast("Rewarded Interstitial Impression")
  }

  override fun onRewardedInterstitialClosed() {
    showToast("Rewarded Interstitial Closed")
  }

  override fun onRewardServerFailed() {
    showToast("Rewarded Interstitial Server Failed")
  }

  override fun onRewardServerSuccess() {
    showToast("Rewarded Interstitial Server Succeeded")
  }
}
