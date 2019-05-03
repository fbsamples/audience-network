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

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.facebook.samples.KotlinAdUnitsSample.fragments.*
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity

class AdUnitsSampleActivity: AppCompatActivity() {
    companion object {
        val TAG: String = AdUnitsSampleActivity::class.java.simpleName
        const val SAMPLE_TYPE: String = "SAMPLE_TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ad_sample)

        val sampleType = intent.getStringExtra(SAMPLE_TYPE)
        if (sampleType != null) {
            val type:AdUnitsSampleType? = AdUnitsSampleType.getSampleTypeFromName(sampleType)
            if (type != null) {
                val fragment: Fragment? = when (type) {
                    AdUnitsSampleType.BANNER -> BannerFragment()
                    AdUnitsSampleType.INSTREAM -> InstreamVideoFragment()
                    AdUnitsSampleType.INTERSTITIAL -> InterstitialFragment()
                    AdUnitsSampleType.RECTANGLE -> RectangleFragment()
                    AdUnitsSampleType.REWARDED -> RewardedVideoFragment()
                    AdUnitsSampleType.NATIVE -> NativeAdSampleFragment()
                    AdUnitsSampleType.NATIVE_BANNER -> NativeBannerAdFragment()
                    AdUnitsSampleType.RECYCLERVIEW -> NativeAdRecyclerFragment()
                    AdUnitsSampleType.HSCROLL -> NativeAdHScrollFragment()
                    AdUnitsSampleType.TEMPLATE -> NativeAdTemplateFragment()
                    AdUnitsSampleType.BANNER_TEMPLATE -> NativeBannerAdTemplateFragment()
                }
                if (fragment != null) {
                    title = type.sampleType
                    supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.ad_units_sample_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == R.id.debug_settings) {
            startActivity(Intent(applicationContext, DebugSettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
