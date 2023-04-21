/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager
import com.facebook.samples.adunitssamplekotlin.R
import com.facebook.samples.adunitssamplekotlin.adapters.NativeAdRecyclerAdapter
import com.facebook.samples.adunitssamplekotlin.models.RecyclerPostItem
import java.util.ArrayList

class NativeAdRecyclerFragment : Fragment(), NativeAdsManager.Listener {

  private var postItemList: ArrayList<RecyclerPostItem>? = null
  private var nativeAdsManager: NativeAdsManager? = null
  private var recyclerView: RecyclerView? = null

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {

    // Create some dummy post items
    postItemList = ArrayList()
    for (i in 1..100) {
      postItemList!!.add(RecyclerPostItem("RecyclerView Item #$i"))
    }

    val placementId = "YOUR_PLACEMENT_ID"
    nativeAdsManager = NativeAdsManager(activity, placementId, 5)
    nativeAdsManager!!.loadAds()
    nativeAdsManager!!.setListener(this)

    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_native_ad_recycler, container, false)
    recyclerView = view.findViewById(R.id.recyclerView)
    return view
  }

  override fun onAdsLoaded() {
    if (activity == null) {
      return
    }

    recyclerView!!.layoutManager = LinearLayoutManager(activity)
    val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
    recyclerView!!.addItemDecoration(itemDecoration)
    val adapter = NativeAdRecyclerAdapter(activity!!, postItemList!!, nativeAdsManager!!)
    recyclerView!!.adapter = adapter
  }

  override fun onAdError(error: AdError) = Unit
}
