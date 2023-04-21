/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.*
import com.facebook.samples.adunitssamplekotlin.R
import com.facebook.samples.adunitssamplekotlin.models.RecyclerPostItem
import java.util.ArrayList

class NativeAdRecyclerAdapter(
    private val activity: Activity,
    private val postItems: List<RecyclerPostItem>,
    private val nativeAdsManager: NativeAdsManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private val adItems: MutableList<NativeAd>

  init {
    adItems = ArrayList()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return if (viewType == AD_TYPE) {
      val inflatedView =
          LayoutInflater.from(parent.context).inflate(R.layout.native_ad_unit, parent, false)
              as NativeAdLayout
      AdHolder(inflatedView)
    } else {
      val inflatedView =
          LayoutInflater.from(parent.context).inflate(R.layout.recycler_post_item, parent, false)
      PostHolder(inflatedView)
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (position % AD_DISPLAY_FREQUENCY == 0) AD_TYPE else POST_TYPE
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    if (holder.itemViewType == AD_TYPE) {
      val ad: NativeAd?

      if (adItems.size > position / AD_DISPLAY_FREQUENCY) {
        ad = adItems[position / AD_DISPLAY_FREQUENCY]
      } else {
        ad = nativeAdsManager.nextNativeAd()
        if (ad != null && !ad.isAdInvalidated) {
          adItems.add(ad)
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
        adHolder.btnAdCallToAction.visibility =
            if (nonNullAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        val adOptionsView = AdOptionsView(activity, nonNullAd, adHolder.nativeAdLayout)
        adHolder.adChoicesContainer.addView(adOptionsView, 0)

        val clickableViews = ArrayList<View>()
        clickableViews.add(adHolder.ivAdIcon)
        clickableViews.add(adHolder.mvAdMedia)
        clickableViews.add(adHolder.btnAdCallToAction)
        nonNullAd.registerViewForInteraction(
            adHolder.nativeAdLayout, adHolder.mvAdMedia, adHolder.ivAdIcon, clickableViews)
      }
    } else {
      val postHolder = holder as PostHolder

      // Calculate where the next postItem index is by subtracting ads we've shown.
      val index = position - position / AD_DISPLAY_FREQUENCY - 1

      val postItem = postItems[index]
      postHolder.tvPostContent.text = postItem.postContent
    }
  }

  override fun getItemCount(): Int {
    return postItems.size + adItems.size
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
    internal var tvAdSocialContext: TextView =
        nativeAdLayout.findViewById(R.id.native_ad_social_context)
    internal var tvAdSponsoredLabel: TextView =
        nativeAdLayout.findViewById(R.id.native_ad_sponsored_label)
    internal var btnAdCallToAction: Button =
        nativeAdLayout.findViewById(R.id.native_ad_call_to_action)
    internal var adChoicesContainer: LinearLayout =
        nativeAdLayout.findViewById(R.id.ad_choices_container)
  }

  companion object {

    private const val AD_DISPLAY_FREQUENCY = 5
    private const val POST_TYPE = 0
    private const val AD_TYPE = 1
  }
}
