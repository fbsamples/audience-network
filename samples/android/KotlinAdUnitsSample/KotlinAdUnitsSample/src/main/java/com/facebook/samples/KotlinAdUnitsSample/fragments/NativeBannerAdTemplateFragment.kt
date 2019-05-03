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

class NativeBannerAdTemplateFragment : Fragment(), NativeAdListener {

    private var mNativeBannerAd: NativeBannerAd? = null
    private var mViewType: NativeBannerAdView.Type = NativeBannerAdView.Type.HEIGHT_100

    private var mAdBackgroundColor: Int = 0
    private var mTitleColor: Int = 0
    private var mLinkColor: Int = 0
    private var mContentColor: Int = 0
    private var mCtaBgColor: Int = 0

    private var mStatusText: TextView? = null
    private var mNativeAdContainer: ViewGroup? = null
    private var mBackgroundColorSpinner: Spinner? = null
    private var mAdViewTypeSpinner: Spinner? = null
    private var mShowCodeButton: Button? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater!!.inflate(R.layout.fragment_native_banner_ad_template, container, false)

        mStatusText = view.findViewById(R.id.status)
        mNativeAdContainer = view.findViewById(R.id.templateContainer)
        mShowCodeButton = view.findViewById(R.id.showCodeButton)
        mBackgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner)
        mAdViewTypeSpinner = view.findViewById(R.id.adViewTypeSpinner)

        val backgroundColorSpinnerAdapter = ArrayAdapter.createFromResource(
                activity,
                R.array.background_color_array,
                android.R.layout.simple_spinner_item
        )
        backgroundColorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        )
        mBackgroundColorSpinner!!.adapter = backgroundColorSpinnerAdapter

        val adViewTypeSpinnerAdapter = ArrayAdapter.createFromResource(
                activity,
                R.array.ad_bannerview_type_array, android.R.layout.simple_spinner_item
        )
        adViewTypeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        )
        mAdViewTypeSpinner!!.adapter = adViewTypeSpinnerAdapter

        setSpinnerListeners()
        setButtonListeners()

        createAndLoadNativeAd()

        return view
    }

    private fun createAndLoadNativeAd() {
        // Create a native banner ad request with a unique placement ID
        // (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        mNativeBannerAd = NativeBannerAd(context, "YOUR_PLACEMENT_ID")

        // Set a listener to get notified when the ad was loaded.
        mNativeBannerAd!!.setAdListener(this)

        // Initiate a request to load an ad.
        mNativeBannerAd!!.loadAd()

        mStatusText?.setText(R.string.ad_loading)
    }

    private fun reloadAdContainer() {
        val activity = activity
        if (activity != null && mNativeBannerAd != null && mNativeBannerAd!!.isAdLoaded) {
            mNativeAdContainer!!.removeAllViews()

            // Create a NativeAdViewAttributes object and set the attributes
            val attributes = NativeAdViewAttributes()
                    .setBackgroundColor(mAdBackgroundColor)
                    .setTitleTextColor(mTitleColor)
                    .setDescriptionTextColor(mContentColor)
                    .setButtonBorderColor(mCtaBgColor)
                    .setButtonTextColor(mLinkColor)
                    .setButtonColor(mCtaBgColor)

            // Use NativeAdView.render to generate the ad View
            val adView = NativeBannerAdView.render(activity, mNativeBannerAd!!, mViewType, attributes)

            // Add adView to the container showing Ads
            mNativeAdContainer!!.addView(adView, 0)
            mNativeAdContainer!!.setBackgroundColor(Color.TRANSPARENT)

            mShowCodeButton!!.setText(R.string.show_code)
        }
    }

    private fun setSpinnerListeners() {
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
                        mLinkColor = Color.WHITE
                        mContentColor = COLOR_LIGHT_GRAY
                        mCtaBgColor = COLOR_CTA_BLUE_BG
                    }
                    1 -> {
                        mAdBackgroundColor = Color.BLACK
                        mTitleColor = Color.WHITE
                        mContentColor = Color.LTGRAY
                        mLinkColor = Color.BLACK
                        mCtaBgColor = Color.WHITE
                    }
                }
                reloadAdContainer()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        mAdViewTypeSpinner!!.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    arg0: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
            ) {
                when (mAdViewTypeSpinner!!.selectedItemPosition) {
                    0 -> mViewType = NativeBannerAdView.Type.HEIGHT_50
                    1 -> mViewType = NativeBannerAdView.Type.HEIGHT_100
                    2 -> mViewType = NativeBannerAdView.Type.HEIGHT_120
                }
                reloadAdContainer()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setButtonListeners() {
        mShowCodeButton!!.setOnClickListener {
            if (mShowCodeButton!!.text === resources.getString(R.string.show_ad)) {
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
        mNativeAdContainer!!.removeAllViews()
        val code = TextView(activity)
        code.text = codeSnippet
        code.setBackgroundColor(Color.WHITE)
        code.setTextColor(Color.BLACK)
        mNativeAdContainer!!.addView(code, 0)

        mShowCodeButton!!.setText(R.string.show_ad)
    }

    override fun onAdLoaded(ad: Ad) {
        if (mNativeBannerAd == null || mNativeBannerAd !== ad) {
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
        mNativeBannerAd = null
        super.onDestroy()
    }

    companion object {

        private val TAG = NativeBannerAdTemplateFragment::class.java.simpleName

        private const val COLOR_LIGHT_GRAY = -0x6f6b64
        private const val COLOR_DARK_GRAY = -0xb1a99b
        private const val COLOR_CTA_BLUE_BG = -0xbf7f01
    }
}
