package com.sample.horizontalscroll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;

public class MainActivity extends AppCompatActivity {
    private NativeAdsManager manager;
    private NativeAdScrollView nativeAdScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize a NativeAdsManager and request 5 ads
        manager = new NativeAdsManager(this, "YOUR_PLACEMENT_ID", 5);
        manager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                nativeAdScrollView = new NativeAdScrollView(MainActivity.this, manager,
                        NativeAdView.Type.HEIGHT_300);
                LinearLayout hscrollContainer = (LinearLayout) findViewById(R.id.hscrollContainer);
                hscrollContainer.addView(nativeAdScrollView);
            }

            @Override
            public void onAdError(AdError adError) {
                // Ad error callback
            }
        });
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);
    }
}
