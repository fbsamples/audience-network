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
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R

class RewardedVideoFragment : Fragment(), S2SRewardedVideoAdListener {

  private var rewardedVideoAdStatusLabel: TextView? = null
  private var loadRewardedVideoButton: Button? = null
  private var showRewardedVideoButton: Button? = null

  private var rewardedVideoAd: RewardedVideoAd? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_rewarded_video, container, false)

    rewardedVideoAdStatusLabel = view.findViewById(R.id.rewardedVideoAdStatusLabel)
    loadRewardedVideoButton = view.findViewById(R.id.loadRewardedVideoButton)
    showRewardedVideoButton = view.findViewById(R.id.showRewardedVideoButton)

    loadRewardedVideoButton?.setOnClickListener {
      rewardedVideoAd?.destroy()
      rewardedVideoAd = null

      rewardedVideoAd = RewardedVideoAd(this@RewardedVideoFragment.activity, "YOUR_PLACEMENT_ID")
      setStatusLabelText("Loading rewarded video ad...")
      rewardedVideoAd?.loadAd(
          rewardedVideoAd!!
              .buildLoadAdConfig()
              .withAdListener(this)
              .withFailOnCacheFailureEnabled(true)
              .withRewardData(RewardData("YOUR_USER_ID", "YOUR_REWARD"))
              .build())
    }

    showRewardedVideoButton?.setOnClickListener {
      if (rewardedVideoAd == null || !rewardedVideoAd!!.isAdLoaded) {
        setStatusLabelText("Ad not loaded. Click load to request an ad.")
      } else {
        rewardedVideoAd!!.show()
        setStatusLabelText("")
      }
    }

    return view
  }

  override fun onError(ad: Ad, error: AdError) {
    if (ad === rewardedVideoAd) {
      setStatusLabelText("Rewarded video ad failed to load: " + error.errorMessage)
    }
  }

  override fun onAdLoaded(ad: Ad) {
    if (ad === rewardedVideoAd) {
      setStatusLabelText("Ad loaded. Click show to present!")
    }
  }

  override fun onAdClicked(ad: Ad) {
    showToast("Rewarded Video Clicked")
  }

  private fun setStatusLabelText(label: String) {
    rewardedVideoAdStatusLabel?.text = label
  }

  private fun showToast(message: String) {
    if (isAdded) {
      Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
  }

  override fun onRewardedVideoCompleted() {
    showToast("Rewarded Video View Complete")
  }

  override fun onLoggingImpression(ad: Ad) {
    showToast("Rewarded Video Impression")
  }

  override fun onRewardedVideoClosed() {
    showToast("Rewarded Video Closed")
  }

  override fun onRewardServerFailed() {
    showToast("Reward Video Server Failed")
  }

  override fun onRewardServerSuccess() {
    showToast("Reward Video Server Succeeded")
  }
}
