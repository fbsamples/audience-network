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

import android.content.res.Resources
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


class InstreamVideoFragment : Fragment(), InstreamVideoAdListener {

    private var mInstreamVideoAdStatusLabel: TextView? = null
    private var mShowInstreamVideoButton: Button? = null
    private var mAdViewContainer: RelativeLayout? = null
    private var mInstreamVideoAdView: InstreamVideoAdView? = null

    private var mIsShowClicked = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instream_video, container, false)

        mInstreamVideoAdStatusLabel = view.findViewById(R.id.instreamVideoAdStatusLabel)
        val loadInstreamVideoButton = view.findViewById<Button>(R.id.loadInstreamVideoButton)
        mShowInstreamVideoButton = view.findViewById(R.id.showInstreamVideoButton)
        val destroyInstreamVideoButton = view.findViewById<Button>(R.id.destroyInstreamVideoButton)
        mAdViewContainer = view.findViewById(R.id.adViewContainer)

        loadInstreamVideoButton.setOnClickListener{
            mIsShowClicked = false
            if (mInstreamVideoAdView != null) {
                mInstreamVideoAdView?.destroy()
                mAdViewContainer?.removeAllViews()
            }
            mInstreamVideoAdView = InstreamVideoAdView(
                    this.activity,
                    "YOUR_PLACEMENT_ID",
                    AdSize(
                            (mAdViewContainer!!.measuredWidth * DENSITY).toInt(),
                            (mAdViewContainer!!.measuredHeight * DENSITY).toInt()
                    )
            )
            mInstreamVideoAdView?.setAdListener(this)
            mInstreamVideoAdView?.loadAd()

            setStatusLabelText("Loading Instream video ad...")
        }

        mShowInstreamVideoButton?.setOnClickListener {
            if (mInstreamVideoAdView == null || !mInstreamVideoAdView!!.isAdLoaded) {
                setStatusLabelText("Ad not loaded. Click load to request an ad.")
            } else {
                if (mInstreamVideoAdView!!.parent !== mAdViewContainer) {
                    mAdViewContainer?.addView(mInstreamVideoAdView)
                }
                mInstreamVideoAdView!!.show()
                setStatusLabelText("Ad Showing")
                mIsShowClicked = true
            }
        }

        destroyInstreamVideoButton.setOnClickListener {
            if (mInstreamVideoAdView != null) {
                mInstreamVideoAdView!!.destroy()
                mInstreamVideoAdView = null
                mAdViewContainer?.removeAllViews()
                setStatusLabelText("Ad destroyed")
            }
        }

        // Restore state
        if (savedInstanceState == null) {
            return view
        }
        val adState = savedInstanceState.getBundle(AD)
        if (adState != null) {
            mInstreamVideoAdView = InstreamVideoAdView(
                    this.activity,
                    adState
            )
            mInstreamVideoAdView?.setAdListener(object : InstreamVideoAdListener {
                override fun onAdVideoComplete(ad: Ad) {
                    this@InstreamVideoFragment.onAdVideoComplete(ad)
                }

                override fun onError(ad: Ad, error: AdError) {
                    this@InstreamVideoFragment.onError(ad, error)
                }

                override fun onAdLoaded(ad: Ad) {
                    this@InstreamVideoFragment.onAdLoaded(ad)
                    mShowInstreamVideoButton?.callOnClick()
                }

                override fun onAdClicked(ad: Ad) {
                    this@InstreamVideoFragment.onAdClicked(ad)
                }

                override fun onLoggingImpression(ad: Ad) {
                    this@InstreamVideoFragment.onLoggingImpression(ad)
                }
            })
            mInstreamVideoAdView?.loadAd()
        }

        return view
    }

    override fun onError(ad: Ad, error: AdError) {
        if (ad === mInstreamVideoAdView) {
            setStatusLabelText("Instream video ad failed to load: " + error.errorMessage)
        }
    }

    override fun onAdLoaded(ad: Ad) {
        if (ad === mInstreamVideoAdView) {
            setStatusLabelText("Ad loaded. Click show to present!")
        }
    }

    override fun onAdClicked(ad: Ad) {
        Toast.makeText(this.activity, "Instream Video Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    private fun setStatusLabelText(label: String) {
        mInstreamVideoAdStatusLabel?.text = label
    }

    override fun onAdVideoComplete(ad: Ad) {
        Toast.makeText(this.activity, "Instream Video Completed", Toast.LENGTH_SHORT).show()
        mAdViewContainer?.removeView(mInstreamVideoAdView)
        mIsShowClicked = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mInstreamVideoAdView == null) {
            return
        }
        val adState = mInstreamVideoAdView!!.saveInstanceState
        if (adState != null) {
            outState.putBundle(AD, adState)
        }
    }

    override fun onStop() {
        if (mInstreamVideoAdView != null) {
            mAdViewContainer?.removeView(mInstreamVideoAdView)
        }
        super.onStop()
    }

    override fun onResume() {
        if (mIsShowClicked &&
                mInstreamVideoAdView != null &&
                mInstreamVideoAdView!!.parent !== mAdViewContainer
        ) {
            mAdViewContainer?.addView(mInstreamVideoAdView)
        }
        super.onResume()
    }

    companion object {

        private val TAG = InstreamVideoFragment::class.java.simpleName
        private const val AD = "ad"

        private val DENSITY = Resources.getSystem().displayMetrics.density.toDouble()
    }
}
