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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdBase
import com.facebook.ads.NativeAdScrollView
import com.facebook.ads.NativeAdsManager
import com.facebook.samples.KotlinAdUnitsSample.R

class NativeAdHScrollFragment : Fragment(), NativeAdsManager.Listener {

    private var manager: NativeAdsManager? = null
    private var scrollView: NativeAdScrollView? = null
    private var scrollViewContainer: LinearLayout? = null
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
            Toast.makeText(
                    activity, "Ad error: " + error.errorMessage,
                    Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        if (activity != null) {
            activity.requestedOrientation = originalScreenOrientationFlag
        }
        super.onDestroyView()
    }

    companion object {

        private const val NATIVE_AD_VIEW_HEIGHT_DP = 300
    }
}
