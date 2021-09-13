/*
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
