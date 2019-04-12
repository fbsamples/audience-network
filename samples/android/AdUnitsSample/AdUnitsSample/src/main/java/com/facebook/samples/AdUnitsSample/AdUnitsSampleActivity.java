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

package com.facebook.samples.AdUnitsSample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.samples.AdUnitsSample.fragments.BannerFragment;
import com.facebook.samples.AdUnitsSample.fragments.InstreamVideoFragment;
import com.facebook.samples.AdUnitsSample.fragments.InterstitialFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdHScrollFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdRecyclerFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdSampleFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeAdTemplateFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeBannerAdFragment;
import com.facebook.samples.AdUnitsSample.fragments.NativeBannerAdTemplateFragment;
import com.facebook.samples.AdUnitsSample.fragments.RectangleFragment;
import com.facebook.samples.AdUnitsSample.fragments.RewardedVideoFragment;
import com.facebook.samples.ads.debugsettings.DebugSettingsActivity;

public class AdUnitsSampleActivity extends FragmentActivity {

    private static final String TAG = AdUnitsSampleActivity.class.getSimpleName();
    public static final String SAMPLE_TYPE = "SAMPLE_TYPE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If you call AudienceNetworkAds.buildInitSettings(Context).initialize()
        // in Application.onCreate() this call is not really necessary.
        // Otherwise call initialize() onCreate() of all Activities that contain ads or
        // from onCreate() of your Splash Activity.
        AudienceNetworkInitializeHelper.initialize(this);

        setContentView(R.layout.activity_ad_sample);

        Intent intent = getIntent();
        String sampleType = intent.getStringExtra(SAMPLE_TYPE);
        Fragment fragment = null;

        if (sampleType == null) {
            return;
        }

        // Basic ad unit sample types
        AdUnitsSampleType type = AdUnitsSampleType.getSampleTypeFromName(sampleType);
        if (type != null) {
            switch (type) {
                case BANNER:
                    fragment = new BannerFragment();
                    break;
                case RECTANGLE:
                    fragment = new RectangleFragment();
                    break;
                case INTERSTITIAL:
                    fragment = new InterstitialFragment();
                    break;
                case INSTREAM:
                    fragment = new InstreamVideoFragment();
                    break;
                case REWARDED:
                    fragment = new RewardedVideoFragment();
                    break;
                case NATIVE:
                    fragment = new NativeAdSampleFragment();
                    break;
                case NATIVE_BANNER:
                    fragment = new NativeBannerAdFragment();
                    break;
                case RECYCLERVIEW:
                    fragment = new NativeAdRecyclerFragment();
                    break;
                case HSCROLL:
                    fragment = new NativeAdHScrollFragment();
                    break;
                case TEMPLATE:
                    fragment = new NativeAdTemplateFragment();
                    break;
                case BANNER_TEMPLATE:
                    fragment = new NativeBannerAdTemplateFragment();
                    break;
            }
            setTitle(type.getName());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ad_units_sample_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.debug_settings) {
            startActivity(new Intent(getApplicationContext(), DebugSettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
