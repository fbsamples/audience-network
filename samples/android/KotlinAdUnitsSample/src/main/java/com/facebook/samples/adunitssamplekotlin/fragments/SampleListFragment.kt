/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.adunitssamplekotlin.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.facebook.samples.adunitssamplekotlin.AdUnitsSampleActivity
import com.facebook.samples.adunitssamplekotlin.AdUnitsSampleType
import com.facebook.samples.adunitssamplekotlin.adapters.SampleAdapter

class SampleListFragment : ListFragment() {

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    listAdapter = SampleAdapter(inflater.context)
    return super.onCreateView(inflater, container, savedInstanceState)
  }

  override fun onListItemClick(parent: ListView?, view: View?, position: Int, id: Long) {
    val item: SampleAdapter.Item = listAdapter.getItem(position) as SampleAdapter.Item
    val sampleName = item.title
    val type = AdUnitsSampleType.getSampleTypeFromName(sampleName)

    if (type != null) {
      // Start sample activity
      val intent = Intent(context, AdUnitsSampleActivity::class.java)
      intent.putExtra(AdUnitsSampleActivity.SAMPLE_TYPE, type.sampleType)
      startActivity(intent)
    }
  }
}
