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

package com.facebook.samples.KotlinAdUnitsSample

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.facebook.samples.KotlinAdUnitsSample.adapters.SampleAdapter
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity

class SampleListActivity : ListActivity() {

    companion object {
        val TAG = SampleListActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(R.string.select_sample)

        val adapter = SampleAdapter(this)
        this.listAdapter = adapter

        this.listView.setOnItemClickListener {
            _: AdapterView<*>?, _: View?, position: Int, _: Long ->

            val item = adapter.getItem(position)
            val sampleName = item.title
            val type = AdUnitsSampleType.getSampleTypeFromName(sampleName)

            if (type != null) {
                // Start sample activity
                intent = Intent(this, AdUnitsSampleActivity::class.java)
                intent.putExtra(AdUnitsSampleActivity.SAMPLE_TYPE, type.sampleType)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.ad_units_sample_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.debug_settings) {
            // Start debug settings
            startActivity(Intent(this, DebugSettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
