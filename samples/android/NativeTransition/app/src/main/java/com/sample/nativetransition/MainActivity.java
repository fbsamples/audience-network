package com.sample.nativetransition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
        NativeAd nativeAd = NativeAdLoader.getInstance().getNativeAd();
        if (nativeAd != null) {
            LinearLayout nativeAdUnit = (LinearLayout) getLayoutInflater().inflate(R.layout.native_ad_main, nativeAdContainer);

            final ImageView nativeAdIcon = (ImageView) nativeAdUnit.findViewById(R.id.native_ad_icon);
            // Download and display the ad icon.
            NativeAd.Image adIcon = nativeAd.getAdIcon();
            NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

            final MediaView nativeAdMedia = (MediaView) nativeAdUnit.findViewById(R.id.native_ad_media);
            nativeAdMedia.setNativeAd(nativeAd);

            // Add the AdChoices icon
            LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
            AdChoicesView adChoicesView = new AdChoicesView(MainActivity.this, nativeAd, true);
            adChoicesContainer.addView(adChoicesView);

            TextView titleView = (TextView) nativeAdUnit.findViewById(R.id.title);
            titleView.setText(nativeAd.getAdTitle());

            Button button = (Button) nativeAdUnit.findViewById(R.id.cta_button);
            button.setText(nativeAd.getAdCallToAction());

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(nativeAdMedia);

            nativeAd.registerViewForInteraction(nativeAdUnit, clickableViews);

            Log.d(TAG, "Native ad filled!");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
