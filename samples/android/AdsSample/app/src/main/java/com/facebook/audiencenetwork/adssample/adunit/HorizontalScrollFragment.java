package com.facebook.audiencenetwork.adssample.adunit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;
import com.facebook.audiencenetwork.adssample.R;

public class HorizontalScrollFragment extends Fragment {
    private NativeAdsManager manager;
    private NativeAdScrollView nativeAdScrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_horizontal_scroll, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showHScroll();
    }

    public void showHScroll() {
        // Initialize a NativeAdsManager and request 5 ads
        manager = new NativeAdsManager(getContext(), "YOUR_PLACEMENT_ID", 5);
        manager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {
                nativeAdScrollView = new NativeAdScrollView(getContext(), manager,
                        NativeAdView.Type.HEIGHT_300);
                LinearLayout hscrollContainer = (LinearLayout) getView().findViewById(R.id
                        .hscrollContainer);
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
