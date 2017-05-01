/**
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 * <p>
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary form
 * for use in connection with the web services and APIs provided by Facebook.
 * <p>
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/], Your use of this software is also
 * subject to the Audience Network Terms
 * [https://www.facebook.com/ads/manage/audience_network/publisher_tos].
 * This copyright notice shall be included in all copies or substantial portions
 * of the software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.sample.interstitial;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class InterstitialActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    private Button showInterstitialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        // Instantiate an InterstitialAd object
        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");

        showInterstitialButton = (Button) findViewById(R.id.btn_show_interstitial);
        showInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableButton();
                // Display the Interstitial Ad
                interstitialAd.loadAd();
            }
        });

        // Set listeners for the Interstitial Ad
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Toast.makeText(InterstitialActivity.this, "Interstitial Ad displayed!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                Toast.makeText(InterstitialActivity.this, "Interstitial Ad dismissed!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                Toast.makeText(InterstitialActivity.this, "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
                enableButton();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Show the ad when it's done loading.
                interstitialAd.show();
                enableButton();
            }

            @Override
            public void onAdClicked(Ad ad) {
                Toast.makeText(InterstitialActivity.this, "Interstitial Ad clicked!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Toast.makeText(InterstitialActivity.this, "Impression logged!", Toast
                        .LENGTH_LONG).show();
            }
        });
    }

    public void enableButton() {
        showInterstitialButton.setEnabled(true);
        showInterstitialButton.setText("Show Interstitial");
    }

    public void disableButton() {
        showInterstitialButton.setEnabled(false);
        showInterstitialButton.setText("Ad Loading...");
    }

    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }
}