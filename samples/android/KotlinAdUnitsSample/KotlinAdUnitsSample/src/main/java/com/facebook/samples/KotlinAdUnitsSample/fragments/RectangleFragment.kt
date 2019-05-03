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


class RectangleFragment : Fragment(), AdListener {

    private var rectangleAdContainer: RelativeLayout? = null
    private var refreshRectangleButton: Button? = null
    private var rectangleStatusLabel: TextView? = null
    private var rectangleAdView: AdView? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
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
        rectangleAdView = AdView(
                activity, "YOUR_PLACEMENT_ID",
                AdSize.RECTANGLE_HEIGHT_250
        )

        // Reposition the ad and add it to the view hierarchy.
        rectangleAdContainer?.addView(rectangleAdView)

        // Set a listener to get notified on changes or when the user interact with the ad.
        rectangleAdView?.setAdListener(this)

        // Initiate a request to load an ad.
        rectangleAdView?.loadAd()
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