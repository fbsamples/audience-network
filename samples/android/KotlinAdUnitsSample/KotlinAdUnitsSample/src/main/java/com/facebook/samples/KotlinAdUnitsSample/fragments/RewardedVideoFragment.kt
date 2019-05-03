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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.facebook.ads.*
import com.facebook.samples.KotlinAdUnitsSample.R

class RewardedVideoFragment : Fragment(), S2SRewardedVideoAdListener {

    private var rewardedVideoAdStatusLabel: TextView? = null
    private var loadRewardedVideoButton: Button? = null
    private var showRewardedVideoButton: Button? = null

    private var rewardedVideoAd: RewardedVideoAd? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_rewarded_video, container, false)

        rewardedVideoAdStatusLabel = view.findViewById(R.id.rewardedVideoAdStatusLabel)
        loadRewardedVideoButton = view.findViewById(R.id.loadRewardedVideoButton)
        showRewardedVideoButton = view.findViewById(R.id.showRewardedVideoButton)

        loadRewardedVideoButton?.setOnClickListener {

            rewardedVideoAd?.destroy()
            rewardedVideoAd = null

            rewardedVideoAd = RewardedVideoAd(
                    this@RewardedVideoFragment.activity,
                    "YOUR_PLACEMENT_ID"
            )
            setStatusLabelText("Loading rewarded video ad...")
            rewardedVideoAd?.setAdListener(this)
            rewardedVideoAd?.loadAd(true)
            rewardedVideoAd?.setRewardData(RewardData("YOUR_USER_ID", "YOUR_REWARD"))
        }

        showRewardedVideoButton?.setOnClickListener {
            if (rewardedVideoAd == null || !rewardedVideoAd!!.isAdLoaded) {
                setStatusLabelText("Ad not loaded. Click load to request an ad.")
            } else {
                rewardedVideoAd!!.show()
                setStatusLabelText("")
            }
        }

        return view
    }

    override fun onError(ad: Ad, error: AdError) {
        if (ad === rewardedVideoAd) {
            setStatusLabelText("Rewarded video ad failed to load: " + error.errorMessage)
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (ad === rewardedVideoAd) {
            setStatusLabelText("Ad loaded. Click show to present!")
        }
    }

    override fun onAdClicked(ad: Ad) {
        showToast("Rewarded Video Clicked")
    }

    private fun setStatusLabelText(label: String) {
        rewardedVideoAdStatusLabel?.text = label
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRewardedVideoCompleted() {
        showToast("Rewarded Video View Complete")
    }

    override fun onLoggingImpression(ad: Ad) {
        showToast("Rewarded Video Impression")
    }

    override fun onRewardedVideoClosed() {
        showToast("Rewarded Video Closed")
    }

    override fun onRewardServerFailed() {
        showToast("Reward Video Server Failed")
    }

    override fun onRewardServerSuccess() {
        showToast("Reward Video Server Succeeded")
    }
}
