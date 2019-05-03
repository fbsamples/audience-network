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

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.facebook.ads.*
import com.facebook.samples.KotlinAdUnitsSample.R

class BannerFragment: Fragment(), AdListener {
    companion object {
        val TAG = BannerFragment::class.java.simpleName
    }

    private var bannerAdView: AdView? = null
    private var bannerStatusLabel: TextView? = null
    private var bannerAdContainer: RelativeLayout? = null
    private var refreshBannerButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_banner, container, false)

        bannerStatusLabel = view.findViewById(R.id.bannerStatusLabel)
        bannerAdContainer = view.findViewById(R.id.bannerAdContainer)
        refreshBannerButton = view.findViewById(R.id.refreshBannerButton)
        refreshBannerButton?.setOnClickListener {
            loadAdView()
        }
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
        bannerAdView?.let {nonNullBannerAdView ->
            bannerAdContainer?.addView(nonNullBannerAdView)
            nonNullBannerAdView.setAdListener(this)
            nonNullBannerAdView.loadAd()
        }
    }
}
