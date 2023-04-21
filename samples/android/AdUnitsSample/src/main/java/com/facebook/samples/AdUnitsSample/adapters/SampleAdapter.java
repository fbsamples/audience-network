/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.facebook.samples.AdUnitsSample.AdUnitsSampleType;
import com.facebook.samples.AdUnitsSample.R;

public class SampleAdapter extends ArrayAdapter<SampleAdapter.Item> {

  public static class Item {
    private String title;
    private boolean isSection;

    public Item(String title, boolean isSection) {
      this.title = title;
      this.isSection = isSection;
    }

    public Item(String title) {
      this(title, false);
    }

    public String getTitle() {
      return title;
    }
  }

  private Context context;
  private LayoutInflater inflater;

  public SampleAdapter(Context context) {
    super(context, 0);

    this.context = context;

    // Add the samples and section headers
    add(new Item("Basic Samples", true));
    add(new Item(AdUnitsSampleType.BANNER.getName()));
    add(new Item(AdUnitsSampleType.RECTANGLE.getName()));
    add(new Item(AdUnitsSampleType.INTERSTITIAL.getName()));
    add(new Item(AdUnitsSampleType.REWARDED_VIDEO.getName()));
    add(new Item(AdUnitsSampleType.REWARDED_INTERSTITIAL.getName()));

    // Native ad samples
    add(new Item("Native Ad Samples", true));
    add(new Item(AdUnitsSampleType.NATIVE.getName()));
    add(new Item(AdUnitsSampleType.NATIVE_BANNER.getName()));
    add(new Item(AdUnitsSampleType.RECYCLERVIEW.getName()));
    add(new Item(AdUnitsSampleType.HSCROLL.getName()));
    add(new Item(AdUnitsSampleType.TEMPLATE.getName()));
    add(new Item(AdUnitsSampleType.BANNER_TEMPLATE.getName()));
    add(new Item(AdUnitsSampleType.NATIVE_BANNER_WITH_IMAGE_VIEW.getName()));

    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    final Item item = (Item) getItem(position);
    if (item != null) {
      if (item.isSection) {
        v = inflater.inflate(R.layout.list_item_section, parent, false);
        v.setOnClickListener(null);
        v.setOnLongClickListener(null);
        v.setLongClickable(false);

        final TextView title = (TextView) v.findViewById(R.id.list_item_section_text);
        title.setText(item.title);
      } else {
        v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        final TextView title = (TextView) v.findViewById(android.R.id.text1);
        title.setText(item.title);
      }
    }
    return v;
  }
}
