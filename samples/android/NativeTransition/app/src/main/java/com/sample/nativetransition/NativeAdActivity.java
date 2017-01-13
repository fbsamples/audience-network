package com.sample.nativetransition;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.AdChoicesView;

import java.util.ArrayList;
import java.util.List;


public class NativeAdActivity extends AppCompatActivity {

    private static final int NATIVE_AD_DELAY = 5000;
    private LinearLayout adView;
    private CountDownTimer countDownTimer;

    private ImageView nativeAdIcon;
    private TextView nativeAdTitle;
    private MediaView nativeAdMedia;
    private TextView nativeAdBody;
    private Button nativeAdCallToAction;
    private LinearLayout adChoicesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NativeAd nativeAd = NativeAdLoader.getInstance().getNativeAd();
        if (nativeAd == null) {
            Intent intent = new Intent(NativeAdActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        setContentView(R.layout.activity_native_ad);

        // Add the Ad view into the ad container.
        adView = (LinearLayout) findViewById(R.id.native_ad_unit);

        // Create native UI using the ad metadata.
        nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
        nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

        // Download and display the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Download and display the cover image.
        nativeAdMedia.setNativeAd(nativeAd);

        // Add the AdChoices icon
        adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(NativeAdActivity.this, nativeAd, true);
        adChoicesContainer.addView(adChoicesView);

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(adView, clickableViews);

        final Button skipButton = (Button) findViewById(R.id.skip_ad);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                NativeAdActivity.this.skipThisAdWithAnimation();
            }
        });

        countDownTimer = new CountDownTimer(NATIVE_AD_DELAY, 1000) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                skipButton.setText("Skip | " + seconds);
            }
            @Override
            public void onFinish() {
                skipButton.setText("Skip | 0");
                NativeAdActivity.this.skipThisAdWithAnimation();
            }
        };
        countDownTimer.start();
    }

    private void skipThisAdWithAnimation() {
        Intent intent = new Intent(NativeAdActivity.this, MainActivity.class);

        Pair<View, String> p1 = Pair.create((View)nativeAdMedia, getString(R.string.shared_native_media_view));
        Pair<View, String> p2 = Pair.create((View)nativeAdTitle, getString(R.string.shared_native_title));
        Pair<View, String> p3 = Pair.create((View)adChoicesContainer, getString(R.string.shared_native_ad_choice));
        Pair<View, String> p4 = Pair.create((View)nativeAdIcon, getString(R.string.shared_native_icon));
        Pair<View, String> p5 = Pair.create((View)nativeAdCallToAction, getString(R.string.shared_native_cta));

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(NativeAdActivity.this, p1, p2, p3, p4, p5);
        startActivity(intent, options.toBundle());
    }
}
