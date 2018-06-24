/*
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary
 * form for use in connection with the web services and APIs provided by
 * Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.facebook.audiencenetwork.adssample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.facebook.audiencenetwork.adssample.adunit.BannerFragment;
import com.facebook.audiencenetwork.adssample.adunit.InterstitialFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeAdHScrollFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeAdRecyclerFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeAdSampleFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeAdTemplateFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeBannerAdFragment;
import com.facebook.audiencenetwork.adssample.adunit.NativeBannerAdTemplateFragment;
import com.facebook.audiencenetwork.adssample.adunit.RectangleFragment;
import com.facebook.audiencenetwork.adssample.adunit.RewardedVideoFragment;

public class AdUnitsSampleActivity extends FragmentActivity {

    private static final String TAG = AdUnitsSampleActivity.class.getSimpleName();
    public static final String SAMPLE_TYPE = "SAMPLE_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}

