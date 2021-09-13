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

class NativeBannerAdTemplateFragment : Fragment(), NativeAdListener {

  private var nativeBannerAd: NativeBannerAd? = null
  private var viewType: NativeBannerAdView.Type = NativeBannerAdView.Type.HEIGHT_100

  private var adBackgroundColor: Int = 0
  private var titleColor: Int = 0
  private var linkColor: Int = 0
  private var contentColor: Int = 0
  private var ctaBgColor: Int = 0

  private var statusText: TextView? = null
  private var nativeAdContainer: ViewGroup? = null
  private var backgroundColorSpinner: Spinner? = null
  private var adViewTypeSpinner: Spinner? = null
  private var showCodeButton: Button? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_native_banner_ad_template, container, false)

    statusText = view.findViewById(R.id.status)
    nativeAdContainer = view.findViewById(R.id.templateContainer)
    showCodeButton = view.findViewById(R.id.showCodeButton)
    backgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner)
    adViewTypeSpinner = view.findViewById(R.id.adViewTypeSpinner)

    val backgroundColorSpinnerAdapter =
        ArrayAdapter.createFromResource(
            inflater.context, R.array.background_color_array, android.R.layout.simple_spinner_item)
    backgroundColorSpinnerAdapter.setDropDownViewResource(
        android.R.layout.simple_spinner_dropdown_item)
    backgroundColorSpinner!!.adapter = backgroundColorSpinnerAdapter

    val adViewTypeSpinnerAdapter =
        ArrayAdapter.createFromResource(
            inflater.context,
            R.array.ad_bannerview_type_array,
            android.R.layout.simple_spinner_item)
    adViewTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    adViewTypeSpinner!!.adapter = adViewTypeSpinnerAdapter

    setSpinnerListeners()
    setButtonListeners()

    createAndLoadNativeAd()

    return view
  }

  private fun createAndLoadNativeAd() {
    // Create a native banner ad request with a unique placement ID
    // (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    nativeBannerAd = NativeBannerAd(context, "YOUR_PLACEMENT_ID")

    // Initiate a request to load an ad.
    nativeBannerAd!!.loadAd(
        nativeBannerAd!!
            .buildLoadAdConfig()
            // Set a listener to get notified when the ad was loaded.
            .withAdListener(this)
            .build())

    statusText?.setText(R.string.ad_loading)
  }

  private fun reloadAdContainer() {
    val activity = activity
    if (activity != null && nativeBannerAd != null && nativeBannerAd!!.isAdLoaded) {
      nativeAdContainer!!.removeAllViews()

      // Create a NativeAdViewAttributes object and set the attributes
      val attributes =
          NativeAdViewAttributes(context)
              .setBackgroundColor(adBackgroundColor)
              .setTitleTextColor(titleColor)
              .setDescriptionTextColor(contentColor)
              .setButtonBorderColor(ctaBgColor)
              .setButtonTextColor(linkColor)
              .setButtonColor(ctaBgColor)

      // Use NativeAdView.render to generate the ad View
      val adView = NativeBannerAdView.render(activity, nativeBannerAd!!, viewType, attributes)

      // Add adView to the container showing Ads
      nativeAdContainer!!.addView(adView, 0)
      nativeAdContainer!!.setBackgroundColor(Color.TRANSPARENT)

      showCodeButton!!.setText(R.string.show_code)
    }
  }

  private fun setSpinnerListeners() {
    backgroundColorSpinner!!.onItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
          override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
            when (backgroundColorSpinner!!.selectedItemPosition) {
              0 -> {
                adBackgroundColor = Color.WHITE
                titleColor = COLOR_DARK_GRAY
                linkColor = Color.WHITE
                contentColor = COLOR_LIGHT_GRAY
                ctaBgColor = COLOR_CTA_BLUE_BG
              }
              1 -> {
                adBackgroundColor = Color.BLACK
                titleColor = Color.WHITE
                contentColor = Color.LTGRAY
                linkColor = Color.BLACK
                ctaBgColor = Color.WHITE
              }
            }
            reloadAdContainer()
          }

          override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

    adViewTypeSpinner!!.onItemSelectedListener =
        object : android.widget.AdapterView.OnItemSelectedListener {
          override fun onItemSelected(arg0: AdapterView<*>, view: View, position: Int, id: Long) {
            when (adViewTypeSpinner!!.selectedItemPosition) {
              0 -> viewType = NativeBannerAdView.Type.HEIGHT_50
              1 -> viewType = NativeBannerAdView.Type.HEIGHT_100
              2 -> viewType = NativeBannerAdView.Type.HEIGHT_120
            }
            reloadAdContainer()
          }

          override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
  }

  private fun setButtonListeners() {
    showCodeButton!!.setOnClickListener {
      if (showCodeButton!!.text === resources.getString(R.string.show_ad)) {
        reloadAdContainer()
      } else {
        showCodeInAdContainer()
      }
    }
  }

  private fun showCodeInAdContainer() {
    val lines = resources.getStringArray(R.array.code_snippet_banner_template)
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

  override fun onAdLoaded(ad: Ad) {
    if (nativeBannerAd == null || nativeBannerAd !== ad) {
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
    nativeBannerAd = null
    super.onDestroy()
  }

  companion object {

    private val TAG = NativeBannerAdTemplateFragment::class.java.simpleName

    private const val COLOR_LIGHT_GRAY = -0x6f6b64
    private const val COLOR_DARK_GRAY = -0xb1a99b
    private const val COLOR_CTA_BLUE_BG = -0xbf7f01
  }
}
