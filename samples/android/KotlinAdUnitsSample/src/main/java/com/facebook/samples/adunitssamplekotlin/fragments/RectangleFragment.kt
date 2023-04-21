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

class RectangleFragment : Fragment(), AdListener {

  private var rectangleAdContainer: RelativeLayout? = null
  private var refreshRectangleButton: Button? = null
  private var rectangleStatusLabel: TextView? = null
  private var rectangleAdView: AdView? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_rectangle, container, false)

    rectangleStatusLabel = view.findViewById(R.id.rectangleStatusLabel)
    rectangleAdContainer = view.findViewById(R.id.rectangleAdContainer)
    refreshRectangleButton = view.findViewById(R.id.refreshRectangleButton)
    refreshRectangleButton?.setOnClickListener { loadAdView() }
    loadAdView()
    return view
  }

  override fun onDestroyView() {
    rectangleAdContainer?.removeView(rectangleAdView)
    super.onDestroyView()
  }

  override fun onDestroy() {
    rectangleAdView?.destroy()
    rectangleAdView = null

    super.onDestroy()
  }

  private fun loadAdView() {
    rectangleAdView?.destroy()
    rectangleAdView = null

    // Update progress message
    setLabel(getString(R.string.loading_status))

    // Create a banner's ad view with a unique placement ID (generate your own on the Facebook
    // app settings). Use different ID for each ad placement in your app.
    rectangleAdView = AdView(activity, "YOUR_PLACEMENT_ID", AdSize.RECTANGLE_HEIGHT_250)

    // Reposition the ad and add it to the view hierarchy.
    rectangleAdContainer?.addView(rectangleAdView)

    // Initiate a request to load an ad.
    rectangleAdView?.buildLoadAdConfig()?.withAdListener(this)?.build()
  }

  override fun onError(ad: Ad, error: AdError) {
    if (ad === rectangleAdView) {
      setLabel("Ad failed to load: " + error.errorMessage)
    }
  }

  override fun onAdLoaded(ad: Ad) {
    if (ad === rectangleAdView) {
      setLabel("")
    }
  }

  override fun onAdClicked(ad: Ad) {
    Toast.makeText(this.activity, "Ad Clicked", Toast.LENGTH_SHORT).show()
  }

  override fun onLoggingImpression(ad: Ad) {
    Log.d(TAG, "onLoggingImpression")
  }

  private fun setLabel(status: String) {
    rectangleStatusLabel?.text = status
    if (status.isEmpty()) {
      rectangleStatusLabel?.visibility = View.GONE
    } else {
      rectangleStatusLabel?.visibility = View.VISIBLE
    }
  }

  companion object {
    private val TAG = RectangleFragment::class.java.simpleName
  }
}
