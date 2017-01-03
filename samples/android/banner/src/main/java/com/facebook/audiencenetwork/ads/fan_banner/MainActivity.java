/**
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to  
 * use, copy, modify, and distribute this software in source code or binary form 
 * for use in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of 
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/], Your use of this software is also
 * subject to the Audience Network Terms 
 * [https://www.facebook.com/ads/manage/audience_network/publisher_tos]. 
 * This copyright notice shall be included in all copies or substantial portions 
 * of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
 
package com.facebook.audiencenetwork.ads.fan_banner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity implements AdListener {

    private String adPlacementId = "893127754073705_909121259141021";
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_title);

        loadBanner();
    }

    protected void loadBanner() {
        // Instantiate an AdView view
        adView = new AdView(this, adPlacementId, AdSize.BANNER_HEIGHT_50);

        adView.setAdListener(this);

        // Request to load an ad
        adView.loadAd();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        // Ad failed to load.
        adView.destroy();
        Toast.makeText(MainActivity.this, "Error: " + error.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {
        // Ad was loaded
        // Add code to show the ad's view
        // Find the main layout of your activity
        LinearLayout layout = (LinearLayout) findViewById(R.id.ad_container);
        layout.addView(adView);

    }

    @Override
    public void onAdClicked(Ad ad) {
        // Use this function to detect when an ad was clicked.
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
