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
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.facebook.ads.*
import com.facebook.samples.KotlinAdUnitsSample.R

class NativeAdTemplateFragment : Fragment(), NativeAdListener {

    private var mNativeAd: NativeAd? = null
    private var mLayoutHeightDp = DEFAULT_HEIGHT_DP

    private var mAdBackgroundColor: Int = 0
    private var mTitleColor: Int = 0
    private var mCtaTextColor: Int = 0
    private var mContentColor: Int = 0
    private var mCtaBgColor: Int = 0

    private var mStatusText: TextView? = null
    private var mNativeAdContainer: ViewGroup? = null
    private var mBackgroundColorSpinner: Spinner? = null
    private var mShowCodeButton: Button? = null
    private var mReloadButton: Button? = null
    private var mSeekBar: SeekBar? = null
    private var mAdView: View? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.fragment_native_ad_template, container, false)

        mStatusText = view.findViewById(R.id.status)
        mNativeAdContainer = view.findViewById(R.id.templateContainer)
        mShowCodeButton = view.findViewById(R.id.showCodeButton)
        mReloadButton = view.findViewById(R.id.reloadAdButton)
        mBackgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner)
        mSeekBar = view.findViewById(R.id.seekBar)

        setUpLayoutBuilders()
        setUpButtons()

        createAndLoadNativeAd()

        return view
    }

    override fun onAdLoaded(ad: Ad) {
        if (mNativeAd == null || mNativeAd !== ad) {
            // Race condition, load() called again before last ad was displayed
            return
        }

        mStatusText?.setText(R.string.ad_loaded)
        reloadAdContainer()
    }

    override fun onError(ad: Ad, error: AdError) {
        val msg = resources.getString(R.string.ad_load_failed, error.errorMessage)
        mStatusText?.text = msg
    }

    override fun onAdClicked(ad: Ad) {
        mStatusText?.setText(R.string.ad_clicked)
    }

    override fun onLoggingImpression(ad: Ad) {
        Log.d(TAG, "onLoggingImpression")
    }

    override fun onMediaDownloaded(ad: Ad) {
        Log.d(TAG, "onMediaDownloaded")
    }

    override fun onDestroy() {
        mNativeAd = null
        super.onDestroy()
    }

    private fun createAndLoadNativeAd() {
        // Create a native ad request with a unique placement ID
        // (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        mNativeAd = NativeAd(activity, "YOUR_PLACEMENT_ID")

        // Set a listener to get notified when the ad was loaded.
        mNativeAd!!.setAdListener(this)

        // Initiate a request to load an ad.
        mNativeAd!!.loadAd()

        mStatusText?.setText(R.string.ad_loading)
    }

    private fun reloadAdContainer() {
        val activity = activity
        if (activity != null && mNativeAd != null && mNativeAd!!.isAdLoaded) {
            mNativeAdContainer!!.removeAllViews()

            // Create a NativeAdViewAttributes object and set the attributes
            val attributes = NativeAdViewAttributes()
                    .setBackgroundColor(mAdBackgroundColor)
                    .setTitleTextColor(mTitleColor)
                    .setDescriptionTextColor(mContentColor)
                    .setButtonBorderColor(mCtaTextColor)
                    .setButtonTextColor(mCtaTextColor)
                    .setButtonColor(mCtaBgColor)

            // Use NativeAdView.render to generate the ad View
            mAdView = NativeAdView.render(activity, mNativeAd!!, attributes)

            mNativeAdContainer!!.addView(mAdView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0))
            updateAdViewParams()

            mShowCodeButton!!.setText(R.string.show_code)
        }
    }

    private fun setUpLayoutBuilders() {
        val backgroundColorSpinnerAdapter = ArrayAdapter.createFromResource(
                activity,
                R.array.background_color_array,
                android.R.layout.simple_spinner_item
        )
        backgroundColorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        )
        mBackgroundColorSpinner!!.adapter = backgroundColorSpinnerAdapter

        mBackgroundColorSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    arg0: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                when (mBackgroundColorSpinner!!.selectedItemPosition) {
                    0 -> {
                        mAdBackgroundColor = Color.WHITE
                        mTitleColor = COLOR_DARK_GRAY
                        mCtaTextColor = COLOR_CTA_BLUE_BG
                        mContentColor = COLOR_LIGHT_GRAY
                        mCtaBgColor = Color.WHITE
                    }
                    1 -> {
                        mAdBackgroundColor = Color.BLACK
                        mTitleColor = Color.WHITE
                        mContentColor = Color.LTGRAY
                        mCtaTextColor = Color.BLACK
                        mCtaBgColor = Color.WHITE
                    }
                }
                reloadAdContainer()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mSeekBar!!.progress = DEFAULT_PROGRESS_DP
        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                mLayoutHeightDp = progress * ((MAX_HEIGHT_DP - MIN_HEIGHT_DP) / 100) + MIN_HEIGHT_DP
                updateAdViewParams()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun updateAdViewParams() {
        if (mAdView == null) {
            return
        }
        val params = mAdView!!.layoutParams
        params.height = (Resources.getSystem().displayMetrics.density * mLayoutHeightDp).toInt()
        mAdView!!.layoutParams = params
        mAdView!!.requestLayout()
    }

    private fun setUpButtons() {
        mShowCodeButton!!.setOnClickListener {
            if (mShowCodeButton!!.text === resources.getString(R.string.show_ad)) {
                reloadAdContainer()
            } else {
                showCodeInAdContainer()
            }
        }

        mReloadButton!!.setOnClickListener { createAndLoadNativeAd() }
    }

    private fun showCodeInAdContainer() {
        val lines = resources.getStringArray(R.array.code_snippet_mediumrect_template)
        val codeSnippet = StringBuilder()
        for (line in lines) {
            codeSnippet.append(line).append("\r\n")
        }
        mNativeAdContainer!!.removeAllViews()
        val code = TextView(activity)
        code.text = codeSnippet
        code.setBackgroundColor(Color.WHITE)
        code.setTextColor(Color.BLACK)
        mNativeAdContainer!!.addView(code, 0)

        mShowCodeButton!!.setText(R.string.show_ad)
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
