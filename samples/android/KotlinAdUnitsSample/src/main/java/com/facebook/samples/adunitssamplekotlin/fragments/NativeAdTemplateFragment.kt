/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R

class NativeAdTemplateFragment : Fragment(), NativeAdListener {

  private var nativeAd: NativeAd? = null
  private var layoutHeightDp = DEFAULT_HEIGHT_DP

  private var adBackgroundColor: Int = 0
  private var titleColor: Int = 0
  private var ctaTextColor: Int = 0
  private var contentColor: Int = 0
  private var ctaBgColor: Int = 0

  private var statusText: TextView? = null
  private var nativeAdContainer: ViewGroup? = null
  private var backgroundColorSpinner: Spinner? = null
  private var showCodeButton: Button? = null
  private var reloadButton: Button? = null
  private var seekBar: SeekBar? = null
  private var adView: View? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_native_ad_template, container, false)

    statusText = view.findViewById(R.id.status)
    nativeAdContainer = view.findViewById(R.id.templateContainer)
    showCodeButton = view.findViewById(R.id.showCodeButton)
    reloadButton = view.findViewById(R.id.reloadAdButton)
    backgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner)
    seekBar = view.findViewById(R.id.seekBar)

    setUpLayoutBuilders(inflater.context)
    setUpButtons()

    createAndLoadNativeAd()

    return view
  }

  override fun onAdLoaded(ad: Ad) {
    if (nativeAd == null || nativeAd !== ad) {
      // Race condition, load() called again before last ad was displayed
      return
    }

    statusText?.setText(R.string.ad_loaded)
    reloadAdContainer()
  }

  override fun onError(ad: Ad, error: AdError) {
    val msg = resources.getString(R.string.ad_load_failed, error.errorMessage)
    statusText?.text = msg
  }

  override fun onAdClicked(ad: Ad) {
    statusText?.setText(R.string.ad_clicked)
  }

  override fun onLoggingImpression(ad: Ad) {
    Log.d(TAG, "onLoggingImpression")
  }

  override fun onMediaDownloaded(ad: Ad) {
    Log.d(TAG, "onMediaDownloaded")
  }

  override fun onDestroy() {
    nativeAd = null
    super.onDestroy()
  }

  private fun createAndLoadNativeAd() {
    // Create a native ad request with a unique placement ID
    // (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    nativeAd = NativeAd(activity, "YOUR_PLACEMENT_ID")

    // Initiate a request to load an ad.
    nativeAd!!.loadAd(
        nativeAd!!
            .buildLoadAdConfig()
            // Set a listener to get notified when the ad was loaded.
            .withAdListener(this)
            .build())

    statusText?.setText(R.string.ad_loading)
  }

  private fun reloadAdContainer() {
    val activity = activity
    if (activity != null && nativeAd != null && nativeAd!!.isAdLoaded) {
      nativeAdContainer!!.removeAllViews()

      // Create a NativeAdViewAttributes object and set the attributes
      val attributes =
          NativeAdViewAttributes(context)
              .setBackgroundColor(adBackgroundColor)
              .setTitleTextColor(titleColor)
              .setDescriptionTextColor(contentColor)
              .setButtonBorderColor(ctaTextColor)
              .setButtonTextColor(ctaTextColor)
              .setButtonColor(ctaBgColor)

      // Use NativeAdView.render to generate the ad View
      adView = NativeAdView.render(activity, nativeAd!!, attributes)

      nativeAdContainer!!.addView(
          adView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0))
      updateAdViewParams()

      showCodeButton!!.setText(R.string.show_code)
    }
  }

  private fun setUpLayoutBuilders(context: Context) {
    val backgroundColorSpinnerAdapter =
        ArrayAdapter.createFromResource(
            context, R.array.background_color_array, android.R.layout.simple_spinner_item)
    backgroundColorSpinnerAdapter.setDropDownViewResource(
        android.R.layout.simple_spinner_dropdown_item)
    backgroundColorSpinner!!.adapter = backgroundColorSpinnerAdapter

    backgroundColorSpinner!!.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
            when (backgroundColorSpinner!!.selectedItemPosition) {
              0 -> {
                adBackgroundColor = Color.WHITE
                titleColor = COLOR_DARK_GRAY
                ctaTextColor = COLOR_CTA_BLUE_BG
                contentColor = COLOR_LIGHT_GRAY
                ctaBgColor = Color.WHITE
              }
              1 -> {
                adBackgroundColor = Color.BLACK
                titleColor = Color.WHITE
                contentColor = Color.LTGRAY
                ctaTextColor = Color.BLACK
                ctaBgColor = Color.WHITE
              }
            }
            reloadAdContainer()
          }

          override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

    seekBar!!.progress = DEFAULT_PROGRESS_DP
    seekBar!!.setOnSeekBarChangeListener(
        object : SeekBar.OnSeekBarChangeListener {
          override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            layoutHeightDp = progress * ((MAX_HEIGHT_DP - MIN_HEIGHT_DP) / 100) + MIN_HEIGHT_DP
            updateAdViewParams()
          }

          override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

          override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
        })
  }

  private fun updateAdViewParams() {
    if (adView == null) {
      return
    }
    val params = adView!!.layoutParams
    params.height = (Resources.getSystem().displayMetrics.density * layoutHeightDp).toInt()
    adView!!.layoutParams = params
    adView!!.requestLayout()
  }

  private fun setUpButtons() {
    showCodeButton!!.setOnClickListener {
      if (showCodeButton!!.text === resources.getString(R.string.show_ad)) {
        reloadAdContainer()
      } else {
        showCodeInAdContainer()
      }
    }

    reloadButton!!.setOnClickListener { createAndLoadNativeAd() }
  }

  private fun showCodeInAdContainer() {
    val lines = resources.getStringArray(R.array.code_snippet_mediumrect_template)
    val codeSnippet = StringBuilder()
    for (line in lines) {
      codeSnippet.append(line).append("\r\n")
    }
    nativeAdContainer!!.removeAllViews()
    val code = TextView(activity)
    code.text = codeSnippet
    code.setBackgroundColor(Color.WHITE)
    code.setTextColor(Color.BLACK)
    nativeAdContainer!!.addView(code, 0)

    showCodeButton!!.setText(R.string.show_ad)
  }

  companion object {

    private val TAG = NativeAdTemplateFragment::class.java.simpleName

    private const val COLOR_LIGHT_GRAY = -0x6f6b64
    private const val COLOR_DARK_GRAY = -0xb1a99b
    private const val COLOR_CTA_BLUE_BG = -0xbf7f01

    private const val MIN_HEIGHT_DP = 200
    private const val MAX_HEIGHT_DP = 500
    private const val DEFAULT_HEIGHT_DP = 350
    private const val DEFAULT_PROGRESS_DP = 50
  }
}
