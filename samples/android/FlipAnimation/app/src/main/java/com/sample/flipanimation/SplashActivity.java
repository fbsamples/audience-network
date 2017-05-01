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

package com.sample.flipanimation;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity
    implements AdListener {

    private static final int UI_ANIMATION_DELAY = 3000;
    private static final int BACKGROUND_COLOR = Color.argb(60, 40, 40, 40);
    private final Handler mHideHandler = new Handler();

    private View mBrandView;
    private View mAdView;
    private ImageView mAdViewBackground;

    private ImageView nativeAdIcon;
    private TextView nativeAdTitle;
    private MediaView nativeAdMedia;
    private TextView nativeAdBody;
    private LinearLayout adChoicesContainer;
    private ShiningButton shiningButton;


    private NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        mBrandView = findViewById(R.id.brand_card);
        mAdView = findViewById(R.id.native_ad_card);
        mAdView.setAlpha(0f);

        nativeAdIcon = (ImageView) findViewById(R.id.native_ad_icon);
        nativeAdTitle = (TextView) findViewById(R.id.native_ad_title);
        nativeAdMedia = (MediaView) findViewById(R.id.native_ad_media);
        nativeAdBody = (TextView) findViewById(R.id.native_ad_body);
        adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
        shiningButton = (ShiningButton) findViewById(R.id.native_ad_shining_cta);

        mAdViewBackground = (ImageView) findViewById(R.id.native_ad_card_background);

        nativeAd = new NativeAd(this, getString(R.string.native_placement_id));
        nativeAd.setAdListener(this);
        nativeAd.loadAd();

        mHideHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showFlipAnimationOrSkip();
            }
        }, UI_ANIMATION_DELAY);

        final Button skipButton = (Button) findViewById(R.id.skip_ad);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            skip();
            }
        });
    }

    private void showFlipAnimationOrSkip() {
        if (nativeAd == null || !nativeAd.isAdLoaded()) {
            skip();
            return;
        }

        Blurry.with(this)
            .radius(25)
            .sampling(10)
            .color(BACKGROUND_COLOR)
            .capture(mAdViewBackground)
            .into(mAdViewBackground);

        AnimatorSet leftOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_out);
        leftOut.setTarget(mBrandView);

        AnimatorSet leftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in);
        leftIn.setTarget(mAdView);

        leftOut.start();
        leftIn.start();

    }

    private void skip() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onError(Ad ad, AdError error) {
        Toast toast = Toast.makeText(this, "Ad error!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onAdLoaded(Ad ad) {

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        shiningButton.setText(nativeAd.getAdCallToAction());

        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd);

        NativeAd.Image adCover = nativeAd.getAdCoverImage();
        if (adCover == null) {
            mAdViewBackground.setBackgroundColor(BACKGROUND_COLOR);
        } else {
            NativeAd.downloadAndDisplayImage(adCover, mAdViewBackground);
        }

        // Add the AdChoices icon
        AdChoicesView adChoicesView = new AdChoicesView(this, nativeAd, true);
        adChoicesContainer.addView(adChoicesView);

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdMedia);
        clickableViews.add(shiningButton);
        nativeAd.registerViewForInteraction(mAdView, clickableViews);

    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }
}
