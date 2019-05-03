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

package com.facebook.samples.KotlinAdUnitsSample.adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.ads.*
import com.facebook.samples.KotlinAdUnitsSample.models.RecyclerPostItem
import com.facebook.samples.KotlinAdUnitsSample.R
import java.util.ArrayList

class NativeAdRecyclerAdapter(
        private val mActivity: Activity,
        private val mPostItems: List<RecyclerPostItem>,
        private val mNativeAdsManager: NativeAdsManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mAdItems: MutableList<NativeAd>

    init {
        mAdItems = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AD_TYPE) {
            val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.native_ad_unit, parent, false) as NativeAdLayout
            AdHolder(inflatedView)
        } else {
            val inflatedView = LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_post_item, parent, false
            )
            PostHolder(inflatedView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position % AD_DISPLAY_FREQUENCY == 0) AD_TYPE else POST_TYPE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == AD_TYPE) {
            val ad: NativeAd?

            if (mAdItems.size > position / AD_DISPLAY_FREQUENCY) {
                ad = mAdItems[position / AD_DISPLAY_FREQUENCY]
            } else {
                ad = mNativeAdsManager.nextNativeAd()
                if (ad != null && !ad.isAdInvalidated) {
                    mAdItems.add(ad)
                } else {
                    Log.w(NativeAdRecyclerAdapter::class.java.simpleName, "Ad is invalidated!")
                }
            }

            val adHolder = holder as AdHolder
            adHolder.adChoicesContainer.removeAllViews()

            ad?.let { nonNullAd ->

                adHolder.tvAdTitle.text = nonNullAd.advertiserName
                adHolder.tvAdBody.text = nonNullAd.adBodyText
                adHolder.tvAdSocialContext.text = nonNullAd.adSocialContext
                adHolder.tvAdSponsoredLabel.setText(R.string.sponsored)
                adHolder.btnAdCallToAction.text = nonNullAd.adCallToAction
                adHolder.btnAdCallToAction.visibility = if (nonNullAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
                val adOptionsView = AdOptionsView(mActivity, nonNullAd, adHolder.nativeAdLayout)
                adHolder.adChoicesContainer.addView(adOptionsView, 0)

                val clickableViews = ArrayList<View>()
                clickableViews.add(adHolder.ivAdIcon)
                clickableViews.add(adHolder.mvAdMedia)
                clickableViews.add(adHolder.btnAdCallToAction)
                nonNullAd.registerViewForInteraction(
                    adHolder.nativeAdLayout,
                    adHolder.mvAdMedia,
                    adHolder.ivAdIcon,
                    clickableViews
                )
            }
        } else {
            val postHolder = holder as PostHolder

            //Calculate where the next postItem index is by subtracting ads we've shown.
            val index = position - position / AD_DISPLAY_FREQUENCY - 1

            val postItem = mPostItems[index]
            postHolder.tvPostContent.text = postItem.postContent
        }
    }

    override fun getItemCount(): Int {
        return mPostItems.size + mAdItems.size
    }

    private class PostHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvPostContent: TextView = view.findViewById(R.id.tvPostContent)
    }

    private class AdHolder internal constructor(internal var nativeAdLayout: NativeAdLayout) :
        RecyclerView.ViewHolder(nativeAdLayout) {
        internal var mvAdMedia: MediaView = nativeAdLayout.findViewById(R.id.native_ad_media)
        internal var ivAdIcon: MediaView = nativeAdLayout.findViewById(R.id.native_ad_icon)
        internal var tvAdTitle: TextView = nativeAdLayout.findViewById(R.id.native_ad_title)
        internal var tvAdBody: TextView = nativeAdLayout.findViewById(R.id.native_ad_body)
        internal var tvAdSocialContext: TextView = nativeAdLayout.findViewById(R.id.native_ad_social_context)
        internal var tvAdSponsoredLabel: TextView = nativeAdLayout.findViewById(R.id.native_ad_sponsored_label)
        internal var btnAdCallToAction: Button = nativeAdLayout.findViewById(R.id.native_ad_call_to_action)
        internal var adChoicesContainer: LinearLayout = nativeAdLayout.findViewById(R.id.ad_choices_container)
    }

    companion object {

        private const val AD_DISPLAY_FREQUENCY = 5
        private const val POST_TYPE = 0
        private const val AD_TYPE = 1
    }
}
