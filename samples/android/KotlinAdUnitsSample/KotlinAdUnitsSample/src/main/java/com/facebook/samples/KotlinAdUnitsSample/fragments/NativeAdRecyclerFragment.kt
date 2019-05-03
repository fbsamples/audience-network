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
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.ads.AdError
import com.facebook.ads.NativeAdsManager
import com.facebook.samples.KotlinAdUnitsSample.R
import com.facebook.samples.KotlinAdUnitsSample.adapters.NativeAdRecyclerAdapter
import com.facebook.samples.KotlinAdUnitsSample.models.RecyclerPostItem
import java.util.ArrayList

class NativeAdRecyclerFragment : Fragment(), NativeAdsManager.Listener {

    private var mPostItemList: ArrayList<RecyclerPostItem>? = null
    private var mNativeAdsManager: NativeAdsManager? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Create some dummy post items
        mPostItemList = ArrayList()
        for (i in 1..100) {
            mPostItemList!!.add(RecyclerPostItem("RecyclerView Item #$i"))
        }

        val placementId = "YOUR_PLACEMENT_ID"
        mNativeAdsManager = NativeAdsManager(activity, placementId, 5)
        mNativeAdsManager!!.loadAds()
        mNativeAdsManager!!.setListener(this)

        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_native_ad_recycler, container, false)
        mRecyclerView = view.findViewById(R.id.recyclerView)
        return view
    }

    override fun onAdsLoaded() {
        if (activity == null) {
            return
        }

        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        mRecyclerView!!.addItemDecoration(itemDecoration)
        val adapter = NativeAdRecyclerAdapter(
                activity, mPostItemList!!,
                mNativeAdsManager!!
        )
        mRecyclerView!!.adapter = adapter
    }

    override fun onAdError(error: AdError) {}

}
