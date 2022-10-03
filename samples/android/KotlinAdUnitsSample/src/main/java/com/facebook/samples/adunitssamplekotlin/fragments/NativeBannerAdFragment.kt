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
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R
import java.util.*

class NativeBannerAdFragment : Fragment(), NativeAdListener {

  private var adView: LinearLayout? = null
  private var adChoicesContainer: FrameLayout? = null
  private var nativeBannerAdContainer: NativeAdLayout? = null
  private var nativeBannerAd: NativeBannerAd? = null

  private var nativeBannerAdStatusLabel: TextView? = null

  private var isAdViewAdded: Boolean = false

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.fragment_native_banner_ad, container, false)
    nativeBannerAdContainer = view.findViewById(R.id.native_banner_ad_container)
    nativeBannerAdStatusLabel = view.findViewById(R.id.native_banner_status_label)

    adView =
        inflater.inflate(R.layout.native_banner_ad_unit, nativeBannerAdContainer, false)
            as LinearLayout

    adChoicesContainer = adView!!.findViewById(R.id.ad_choices_container)

    val showNativeBannerAdButton = view.findViewById<Button>(R.id.refresh_native_banner_button)
    showNativeBannerAdButton.setOnClickListener {
      nativeBannerAdStatusLabel!!.text = getString(R.string.loading_status)

      // Create a native ad request with a unique placement ID (generate your own on the
      // Facebook app settings). Use different ID for each ad placement in your app.
      nativeBannerAd = NativeBannerAd(context, "YOUR_PLACEMENT_ID")

      // When testing on a device, add its hashed ID to force test ads.
      // The hash ID is printed to log cat when running on a device and loading an ad.
      // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

      // Initiate a request to load an ad.
      nativeBannerAd!!.loadAd(
          nativeBannerAd!!
              .buildLoadAdConfig()
              // Set a listener to get notified when the ad was loaded.
              .withAdListener(this)
              .withMediaCacheFlag(NativeAdBase.MediaCacheFlag.ALL)
              .build())
    }
    //  load the Native Banner when this fragment is created
    //  as the Banner in BannerFragment does.
    showNativeBannerAdButton.performClick()

    return view
  }

  override fun onError(ad: Ad, error: AdError) {
    nativeBannerAdStatusLabel?.text = "Ad Failed to Load: " + error.errorMessage
  }

  override fun onAdLoaded(ad: Ad) {
    if (nativeBannerAd == null || nativeBannerAd !== ad) {
      // Race condition, load() called again before last ad was displayed
      return
    }
    if (!isAdViewAdded) {
      isAdViewAdded = true
      nativeBannerAdContainer!!.addView(adView)
    }
    // Unregister last ad
    nativeBannerAd!!.unregisterView()

    nativeBannerAdStatusLabel?.text = ""

    // Using the AdOptionsView is optional, but your native ad unit should
    // be clearly delineated from the rest of your app content. See
    // https://developers.facebook.com/docs/audience-network/guidelines/native-ads#native
    // for details. We recommend using the AdOptionsView.
    val adOptionsView =
        AdOptionsView(
            activity,
            nativeBannerAd,
            nativeBannerAdContainer,
            AdOptionsView.Orientation.HORIZONTAL,
            20)
    adChoicesContainer?.removeAllViews()
    adChoicesContainer?.addView(adOptionsView)

    inflateAd(nativeBannerAd!!, adView!!)

    // Registering a touch listener to log which ad component receives the touch event.
    // We always return false from onTouch so that we don't swallow the touch event (which
    // would prevent click events from reaching the NativeAd control).
    // The touch listener could be used to do animations.
    nativeBannerAd!!.setOnTouchListener { view, event ->
      if (event.action == MotionEvent.ACTION_DOWN) {
        when (view.id) {
          R.id.native_ad_call_to_action -> Log.d(TAG, "Call to action button clicked")
          R.id.native_icon_view -> Log.d(TAG, "Main image clicked")
          else -> Log.d(TAG, "Other ad component clicked")
        }
      }
      false
    }
  }

  override fun onAdClicked(ad: Ad) {
    Toast.makeText(activity, "Ad Clicked", Toast.LENGTH_SHORT).show()
  }

  override fun onLoggingImpression(ad: Ad) {
    Log.d(TAG, "onLoggingImpression")
  }

  override fun onMediaDownloaded(ad: Ad) {
    Log.d(TAG, "onMediaDownloaded")
  }

  private fun inflateAd(nativeBannerAd: NativeBannerAd, adView: View) {
    // Create native UI using the ad metadata.
    val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title)
    val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context)
    val sponsoredLabel = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
    val nativeAdIconView = adView.findViewById<MediaView>(R.id.native_icon_view)
    val nativeAdCallToAction = adView.findViewById<Button>(R.id.native_ad_call_to_action)

    // Setting the Text
    nativeAdCallToAction.text = nativeBannerAd.adCallToAction
    nativeAdCallToAction.visibility =
        if (nativeBannerAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE

    nativeAdTitle.text = nativeBannerAd.advertiserName
    nativeAdSocialContext.text = nativeBannerAd.adSocialContext

    // You can use the following to specify the clickable areas.
    val clickableViews = ArrayList<View>()
    clickableViews.add(nativeAdCallToAction)
    nativeBannerAd.registerViewForInteraction(
        nativeBannerAdContainer, nativeAdIconView, clickableViews)

    sponsoredLabel.setText(R.string.sponsored)
  }

  override fun onDestroy() {
    if (nativeBannerAd != null) {
      nativeBannerAd!!.unregisterView()
      nativeBannerAd = null
    }
    super.onDestroy()
  }

  companion object {
    private val TAG = NativeBannerAdFragment::class.java.simpleName
  }
}
