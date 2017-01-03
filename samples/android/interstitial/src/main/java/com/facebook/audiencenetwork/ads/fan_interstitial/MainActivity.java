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

package com.facebook.audiencenetwork.ads.fan_interstitial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.ads.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.app_title);

        ((Button)findViewById(R.id.button1)).setOnClickListener(this);

        loadInterstitialAd();
    }

    public void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this, "893127754073705_909204815799332");
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                // Ad failed to load
                interstitialAd.destroy();
                Toast.makeText(MainActivity.this, "Error: " + error.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
                Log.v("interstitialAd", error.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad is loaded and ready to be displayed
                // You can now display the full screen ad using this code:
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Use this function as indication for a user's click on the ad.
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }


        });

        interstitialAd.loadAd();
    }

    @Override
    public void onClick(View v) {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        loadInterstitialAd();
    }

}
