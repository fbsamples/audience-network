/**
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 * <p>
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 * <p>
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.samples.AdUnitsSample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.facebook.samples.AdUnitsSample.R;
import com.facebook.samples.AdUnitsSample.adapters.NativeAdRecyclerAdapter;
import com.facebook.samples.AdUnitsSample.models.RecyclerPostItem;

import com.facebook.samples.AdUnitsSample.thridparty.DividerItemDecoration.DividerItemDecoration;

import java.util.ArrayList;

public class NativeAdRecyclerFragment extends Fragment implements NativeAdsManager.Listener {

    private ArrayList<RecyclerPostItem> mPostItemList;
    private NativeAdsManager mNativeAdsManager;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        // Create some dummy post items
        mPostItemList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            mPostItemList.add(new RecyclerPostItem("RecyclerView Item #" + i));
        }

        String placement_id = "YOUR_PLACEMENT_ID";
        mNativeAdsManager = new NativeAdsManager(getActivity(), placement_id, 5);
        mNativeAdsManager.loadAds();
        mNativeAdsManager.setListener(this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native_ad_recycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onAdsLoaded() {
        if (getActivity() == null) {
            return;
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        NativeAdRecyclerAdapter adapter = new NativeAdRecyclerAdapter(getActivity(), mPostItemList,
                mNativeAdsManager);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAdError(AdError error) {
    }

}
