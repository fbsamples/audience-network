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
 
package com.facebook.audiencenetwork.ads.fan_native;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity implements AdListener, View.OnClickListener {

    private String adPlacementId = "YOUR_PLACEMENT_ID";
    private NativeAd nativeAd;
    private LinearLayout adView;
    private AdChoicesView adChoicesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_title);

        ((Button)findViewById(R.id.button1)).setOnClickListener(this);

        loadNativeAd();
    }

    protected void loadNativeAd() {
        nativeAd = new NativeAd(this, adPlacementId);

        nativeAd.setAdListener(this);

        // Initiate a request to load an ad.
        nativeAd.loadAd();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        // Ad failed to load.
        Toast.makeText(MainActivity.this, "Error: " + error.getErrorMessage(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdLoaded(Ad ad) {

        // Add ad into the ad container.
        LinearLayout adContainer = (LinearLayout)findViewById(R.id.ad_container);
        LayoutInflater inflater = LayoutInflater.from(this);
        adView = (LinearLayout)inflater.inflate(R.layout.ad_unit, adContainer, false);
        adContainer.addView(adView);

        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView)adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView)adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = (TextView)adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = (MediaView)adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = (TextView)adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = (Button)adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text.
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        nativeAdMedia.setNativeAd(nativeAd);

        // Add adChoices icon
        adChoicesView = new AdChoicesView(this, nativeAd, true);
        adView.addView(adChoicesView, 0);

        nativeAd.registerViewForInteraction(adView);
    }

    @Override
    public void onAdClicked(Ad ad) {
        // Use this function to detect when an ad was clicked.
    }

    @Override
    public void onClick(View v) {
        if (adView != null) {
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.ad_container);
            adContainer.removeView(adView);
        }
        loadNativeAd();
    }
}
