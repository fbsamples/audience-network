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
import android.widget.TextView
import android.widget.Toast
import com.facebook.ads.*
import com.facebook.samples.KotlinAdUnitsSample.R
import java.util.*


class InterstitialFragment: Fragment(), InterstitialAdListener {

    companion object {
        val TAG = InterstitialFragment::class.java.simpleName
    }

    private var interstitialAd: InterstitialAd? = null

    private var interstitialAdStatusLabel: TextView? = null
    private var loadInterstitialButton: Button? = null
    private var showInterstitialButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_interstitial, container, false)
        interstitialAdStatusLabel = view?.findViewById(R.id.interstitialAdStatusLabel)
        loadInterstitialButton = view?.findViewById(R.id.loadInterstitialButton)
        showInterstitialButton = view?.findViewById(R.id.showInterstitialButton)

        loadInterstitialButton?.setOnClickListener {
            if (interstitialAd != null) {
                interstitialAd?.destroy()
                interstitialAd = null
            }
            setLabel("Loading interstitial ad...")

            interstitialAd = InterstitialAd(this.activity, "YOUR_PLACEMENT_ID")
            interstitialAd?.setAdListener(this)
            interstitialAd?.loadAd(EnumSet.of(CacheFlag.VIDEO))
        }

        showInterstitialButton?.setOnClickListener {
            if (interstitialAd == null || !interstitialAd!!.isAdLoaded) {
                setLabel("Ad not loaded. Click load to request an ad.")
            } else {
                interstitialAd?.show()
                setLabel("")
            }

        }
        return view
    }

    override fun onDestroy() {
        interstitialAd?.destroy()
        interstitialAd = null

        super.onDestroy()
    }

    override fun onError(ad: Ad, error: AdError) {
        if (ad === interstitialAd) {
            setLabel("Interstitial ad failed to load: " + error.errorMessage)
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (ad === interstitialAd) {
            setLabel("Ad loaded. Click show to present!")
        }
    }

    override fun onInterstitialDisplayed(ad: Ad) {
        if (isAdded) {
            Toast.makeText(activity, "Interstitial Displayed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onInterstitialDismissed(ad: Ad) {
        if (isAdded) {
            Toast.makeText(activity, "Interstitial Dismissed", Toast.LENGTH_SHORT).show()
        }

        // Cleanup.
        interstitialAd?.destroy()
        interstitialAd = null
    }

    override fun onAdClicked(ad: Ad) {
        if (isAdded) {
            Toast.makeText(activity, "Interstitial Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    private fun setLabel(label:String) {
        interstitialAdStatusLabel?.text = label
    }
}