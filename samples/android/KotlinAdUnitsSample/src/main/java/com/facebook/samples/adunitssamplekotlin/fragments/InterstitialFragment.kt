/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R
import java.util.*

class InterstitialFragment : Fragment(), InterstitialAdListener {

  companion object {
    val TAG = InterstitialFragment::class.java.simpleName
  }

  private var interstitialAd: InterstitialAd? = null

  private var interstitialAdStatusLabel: TextView? = null
  private var loadInterstitialButton: Button? = null
  private var showInterstitialButton: Button? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_interstitial, container, false)
    interstitialAdStatusLabel = view?.findViewById(R.id.interstitialAdStatusLabel)
    loadInterstitialButton = view?.findViewById(R.id.loadInterstitialButton)
    showInterstitialButton = view?.findViewById(R.id.showInterstitialButton)

    loadInterstitialButton?.setOnClickListener {
      if (interstitialAd != null) {
        interstitialAd?.destroy()
        interstitialAd = null
      }
      setLabel("Loading interstitial ad...")

      interstitialAd = InterstitialAd(this.activity, "YOUR_PLACEMENT_ID")
      interstitialAd?.loadAd(
          interstitialAd!!
              .buildLoadAdConfig()
              .withAdListener(this)
              .withCacheFlags(EnumSet.of(CacheFlag.VIDEO))
              .build())
    }

    showInterstitialButton?.setOnClickListener {
      if (interstitialAd == null || !interstitialAd!!.isAdLoaded) {
        setLabel("Ad not loaded. Click load to request an ad.")
      } else {
        interstitialAd?.show()
        setLabel("")
      }
    }
    return view
  }

  override fun onDestroy() {
    interstitialAd?.destroy()
    interstitialAd = null

    super.onDestroy()
  }

  override fun onError(ad: Ad, error: AdError) {
    if (ad === interstitialAd) {
      setLabel("Interstitial ad failed to load: " + error.errorMessage)
    }
  }

  override fun onAdLoaded(ad: Ad) {
    if (ad === interstitialAd) {
      setLabel("Ad loaded. Click show to present!")
    }
  }

  override fun onInterstitialDisplayed(ad: Ad) {
    if (isAdded) {
      Toast.makeText(activity, "Interstitial Displayed", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onInterstitialDismissed(ad: Ad) {
    if (isAdded) {
      Toast.makeText(activity, "Interstitial Dismissed", Toast.LENGTH_SHORT).show()
    }

    // Cleanup.
    interstitialAd?.destroy()
    interstitialAd = null
  }

  override fun onAdClicked(ad: Ad) {
    if (isAdded) {
      Toast.makeText(activity, "Interstitial Clicked", Toast.LENGTH_SHORT).show()
    }
  }

  override fun onLoggingImpression(ad: Ad) {
    Log.d(TAG, "onLoggingImpression")
  }

  private fun setLabel(label: String) {
    interstitialAdStatusLabel?.text = label
  }
}
