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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.facebook.samples.KotlinAdUnitsSample.AdUnitsSampleType
import com.facebook.samples.KotlinAdUnitsSample.R


class SampleAdapter(context: Context, resource: Int): ArrayAdapter<SampleAdapter.Item>(context, resource) {
    class Item(val title: String, val isSection: Boolean = false)

    private val inflater: LayoutInflater = LayoutInflater.from(this.context)

    init {
        add(Item("Basic Samples", true))
        add(Item(AdUnitsSampleType.BANNER.sampleType))
        add(Item(AdUnitsSampleType.RECTANGLE.sampleType))
        add(Item(AdUnitsSampleType.INTERSTITIAL.sampleType))
        add(Item(AdUnitsSampleType.INSTREAM.sampleType))
        add(Item(AdUnitsSampleType.REWARDED.sampleType))

        add(Item("Native Ad Samples", true))
        add(Item(AdUnitsSampleType.NATIVE.sampleType))
        add(Item(AdUnitsSampleType.NATIVE_BANNER.sampleType))
        add(Item(AdUnitsSampleType.RECYCLERVIEW.sampleType))
        add(Item(AdUnitsSampleType.HSCROLL.sampleType))
        add(Item(AdUnitsSampleType.TEMPLATE.sampleType))
        add(Item(AdUnitsSampleType.BANNER_TEMPLATE.sampleType))
    }

    constructor(context: Context): this(context, 0)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var v = convertView
        val item = getItem(position)
        item?.let {nonNullItem ->
            if (nonNullItem.isSection) {
                v = inflater.inflate(R.layout.list_item_section, parent, false)
                v?.let {nonNullView ->
                    nonNullView.setOnClickListener(null)
                    nonNullView.setOnLongClickListener(null)
                    nonNullView.isLongClickable = false
                    nonNullView.findViewById<TextView>(R.id.list_item_section_text)?.text = nonNullItem.title
                }
            } else {
                v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
                v?.findViewById<TextView>(android.R.id.text1)?.text = nonNullItem.title
            }
        }
        return v
    }
}
