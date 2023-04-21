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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdBase
import com.facebook.ads.NativeAdScrollView
import com.facebook.ads.NativeAdsManager
import com.facebook.samples.adunitssamplekotlin.R

class NativeAdHScrollFragment : Fragment(), NativeAdsManager.Listener {

  private var manager: NativeAdsManager? = null
  private var scrollView: NativeAdScrollView? = null
  private var scrollViewContainer: LinearLayout? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_native_ad_hscroll, container, false)

    manager = NativeAdsManager(activity, "YOUR_PLACEMENT_ID", 5)
    manager!!.setListener(this)
    manager!!.loadAds(NativeAdBase.MediaCacheFlag.ALL)

    val reloadButton = view.findViewById(R.id.reload_hscroll) as Button
    reloadButton.setOnClickListener { manager!!.loadAds() }

    scrollViewContainer = view.findViewById(R.id.hscroll_container)

    return view
  }

  override fun onAdsLoaded() {
    if (activity == null) {
      return
    }

    Toast.makeText(activity, "Ads loaded", Toast.LENGTH_SHORT).show()

    if (scrollView != null) {
      scrollViewContainer?.removeView(scrollView)
    }

    scrollView = NativeAdScrollView(activity, manager, NATIVE_AD_VIEW_HEIGHT_DP)

    scrollViewContainer?.addView(scrollView)
  }

  override fun onAdError(error: AdError) {
    if (activity != null) {
      Toast.makeText(activity, "Ad error: " + error.errorMessage, Toast.LENGTH_SHORT).show()
    }
  }

  companion object {

    private const val NATIVE_AD_VIEW_HEIGHT_DP = 300
  }
}
