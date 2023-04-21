/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.facebook.samples.AdUnitsSample.R;
import com.facebook.samples.AdUnitsSample.adapters.NativeAdRecyclerAdapter;
import com.facebook.samples.AdUnitsSample.models.RecyclerPostItem;
import com.facebook.samples.AdUnitsSample.thirdparty.DividerItemDecoration.DividerItemDecoration;
import java.util.ArrayList;

public class NativeAdRecyclerFragment extends Fragment implements NativeAdsManager.Listener {

  private ArrayList<RecyclerPostItem> mPostItemList;
  private NativeAdsManager mNativeAdsManager;
  private RecyclerView mRecyclerView;

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
    RecyclerView.ItemDecoration itemDecoration =
        new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
    mRecyclerView.addItemDecoration(itemDecoration);
    NativeAdRecyclerAdapter adapter =
        new NativeAdRecyclerAdapter(getActivity(), mPostItemList, mNativeAdsManager);
    mRecyclerView.setAdapter(adapter);
  }

  @Override
  public void onAdError(AdError error) {}
}
