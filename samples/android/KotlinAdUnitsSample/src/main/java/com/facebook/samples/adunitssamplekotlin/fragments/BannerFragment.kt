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
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R

class BannerFragment : Fragment(), AdListener {
  companion object {
    val TAG = BannerFragment::class.java.simpleName
  }

  private var bannerAdView: AdView? = null
  private var bannerStatusLabel: TextView? = null
  private var bannerAdContainer: RelativeLayout? = null
  private var refreshBannerButton: Button? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_banner, container, false)

    bannerStatusLabel = view.findViewById(R.id.bannerStatusLabel)
    bannerAdContainer = view.findViewById(R.id.bannerAdContainer)
    refreshBannerButton = view.findViewById(R.id.refreshBannerButton)
    refreshBannerButton?.setOnClickListener { loadAdView() }
    loadAdView()
    return view
  }

  override fun onError(ad: Ad?, error: AdError?) {
    if (ad == bannerAdView) {
      Log.e(TAG, "Banner failed to load: " + error?.errorMessage)
    }
  }

  override fun onAdLoaded(ad: Ad?) {
    if (ad == bannerAdView) {
      setLabel("")
    }
  }

  override fun onAdClicked(ad: Ad?) {
    Toast.makeText(this.activity, "Ad clicked!", Toast.LENGTH_SHORT).show()
  }

  override fun onLoggingImpression(ad: Ad?) {
    Log.d(TAG, "onLoggingImpression")
  }

  private fun setLabel(status: String) {
    bannerStatusLabel?.text = status
  }

  private fun loadAdView() {
    bannerAdView?.destroy()
    bannerAdView = null

    setLabel(getString(R.string.loading_status))

    bannerAdView = AdView(this.activity, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50)
    bannerAdView?.let { nonNullBannerAdView ->
      bannerAdContainer?.addView(nonNullBannerAdView)
      nonNullBannerAdView.loadAd(
          nonNullBannerAdView.buildLoadAdConfig().withAdListener(this).build())
    }
  }
}
