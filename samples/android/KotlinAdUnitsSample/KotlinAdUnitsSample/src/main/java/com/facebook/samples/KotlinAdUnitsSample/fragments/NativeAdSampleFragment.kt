/**
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

package com.facebook.samples.KotlinAdUnitsSample.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.facebook.ads.*
import java.util.ArrayList
import com.facebook.samples.KotlinAdUnitsSample.R

class NativeAdSampleFragment : Fragment(), NativeAdListener {

    private var nativeAdStatus: TextView? = null
    private var adChoicesContainer: LinearLayout? = null

    private var nativeAdLayout: NativeAdLayout? = null
    private var nativeAd: NativeAd? = null
    private var adOptionsView: AdOptionsView? = null
    private var nativeAdMedia: MediaView? = null

    private var originalScreenOrientationFlag: Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //  block auto screen orientation for NativeAdSampleFragment.
        if (activity != null) {
            originalScreenOrientationFlag = activity.requestedOrientation
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
        }

        val view = inflater.inflate(R.layout.fragment_native_ad_sample, container, false)
        nativeAdLayout = view.findViewById(R.id.native_ad_container)

        nativeAdStatus = view.findViewById(R.id.native_ad_status)
        adChoicesContainer = view.findViewById(R.id.ad_choices_container)

        val showNativeAdButton = view.findViewById<Button>(R.id.load_native_ad_button)
        showNativeAdButton.setOnClickListener {
            nativeAdStatus?.setText(R.string.loading_status)

            // Create a native ad request with a unique placement ID (generate your own on the
            // Facebook app settings). Use different ID for each ad placement in your app.
            nativeAd = NativeAd(activity, "YOUR_PLACEMENT_ID")

            // Set a listener to get notified when the ad was loaded.
            nativeAd?.setAdListener(this@NativeAdSampleFragment)

            // When testing on a device, add its hashed ID to force test ads.
            // The hash ID is printed to log cat when running on a device and loading an ad.
            // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

            // Initiate a request to load an ad.
            nativeAd?.loadAd()
        }


        return view
    }

    override fun onDestroyView() {
        activity?.requestedOrientation = originalScreenOrientationFlag

        adChoicesContainer = null
        nativeAdLayout = null
        adOptionsView = null
        nativeAdStatus = null
        super.onDestroyView()
    }

    override fun onError(ad: Ad, error: AdError) {
        nativeAdStatus?.text = "Ad failed to load: " + error.errorMessage
    }

    override fun onAdClicked(ad: Ad) {
        Toast.makeText(activity, "Ad Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    override fun onMediaDownloaded(ad: Ad) {
        if (nativeAd === ad) {
            Log.d(TAG, "onMediaDownloaded")
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (nativeAd == null || nativeAd !== ad) {
            // Race condition, load() called again before last ad was displayed
            return
        }

        if (nativeAdLayout == null) {
            return
        }

        // Unregister last ad
        nativeAd!!.unregisterView()
        nativeAdStatus?.text = ""

        if (adChoicesContainer != null) {
            adOptionsView = AdOptionsView(activity, nativeAd, nativeAdLayout)
            adChoicesContainer?.removeAllViews()
            adChoicesContainer?.addView(adOptionsView, 0)
        }

        inflateAd(nativeAd!!, nativeAdLayout!!)

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        nativeAd!!.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                when (view.id) {
                    R.id.native_ad_call_to_action ->
                        Log.d(TAG, "Call to action button clicked")
                    R.id.native_ad_media ->
                        Log.d(TAG, "Main image clicked")
                    else ->
                        Log.d(TAG, "Other ad component clicked")
                }
            }
            false
        }
    }

    private fun inflateAd(nativeAd: NativeAd, adView: View) {
        // Create native UI using the ad metadata.
        val nativeAdIcon = adView.findViewById<MediaView>(R.id.native_ad_icon)
        val nativeAdTitle = adView.findViewById<TextView>(R.id.native_ad_title)
        val nativeAdBody = adView.findViewById<TextView>(R.id.native_ad_body)
        val sponsoredLabel = adView.findViewById<TextView>(R.id.native_ad_sponsored_label)
        val nativeAdSocialContext = adView.findViewById<TextView>(R.id.native_ad_social_context)
        val nativeAdCallToAction = adView.findViewById<Button>(R.id.native_ad_call_to_action)

        nativeAdMedia = adView.findViewById(R.id.native_ad_media)
        nativeAdMedia?.setListener(mediaViewListener)

        // Setting the Text
        nativeAdSocialContext.text = nativeAd.adSocialContext
        nativeAdCallToAction.text = nativeAd.adCallToAction
        nativeAdCallToAction.visibility = if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        nativeAdTitle.text = nativeAd.advertiserName
        nativeAdBody.text = nativeAd.adBodyText
        sponsoredLabel.setText(R.string.sponsored)

        // You can use the following to specify the clickable areas.
        val clickableViews = ArrayList<View>()
        clickableViews.add(nativeAdIcon)
        clickableViews.add(nativeAdMedia!!)
        clickableViews.add(nativeAdCallToAction)
        nativeAd.registerViewForInteraction(
                nativeAdLayout,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews
        )

        // Optional: tag views
        NativeAdBase.NativeComponentTag.tagView(nativeAdIcon, NativeAdBase.NativeComponentTag.AD_ICON)
        NativeAdBase.NativeComponentTag.tagView(nativeAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE)
        NativeAdBase.NativeComponentTag.tagView(nativeAdBody, NativeAdBase.NativeComponentTag.AD_BODY)
        NativeAdBase.NativeComponentTag.tagView(
                nativeAdSocialContext,
                NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT
        )
        NativeAdBase.NativeComponentTag.tagView(nativeAdCallToAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION)
    }

    override fun onDestroy() {
        nativeAdMedia?.destroy()
        nativeAd?.unregisterView()
        nativeAd?.destroy()

        super.onDestroy()
    }

    companion object {

        private val TAG = NativeAdSampleFragment::class.java.simpleName

        private val mediaViewListener: MediaViewListener
            get() = object : MediaViewListener {
                override fun onVolumeChange(mediaView: MediaView, volume: Float) {
                    Log.i(TAG, "MediaViewEvent: Volume $volume")
                }

                override fun onPause(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: Paused")
                }

                override fun onPlay(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: Play")
                }

                override fun onFullscreenBackground(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: FullscreenBackground")
                }

                override fun onFullscreenForeground(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: FullscreenForeground")
                }

                override fun onExitFullscreen(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: ExitFullscreen")
                }

                override fun onEnterFullscreen(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: EnterFullscreen")
                }

                override fun onComplete(mediaView: MediaView) {
                    Log.i(TAG, "MediaViewEvent: Completed")
                }
            }
    }
}
