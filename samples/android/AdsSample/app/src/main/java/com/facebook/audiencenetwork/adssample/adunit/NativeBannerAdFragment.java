// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.audiencenetwork.adssample.adunit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.facebook.audiencenetwork.adssample.R;

import java.util.ArrayList;
import java.util.List;

public class NativeBannerAdFragment extends Fragment implements NativeAdListener {
    private static final String TAG = NativeBannerAdFragment.class.getSimpleName();

    private LinearLayout mAdView;
    private RelativeLayout mAdChoicesContainer;
    private RelativeLayout mNativeBannerAdContainer;

    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private @Nullable
    AdChoicesView mAdChoicesView;

    private TextView mNativeBannerAdStatusLabel;

    private boolean isAdViewAdded;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_native_banner_ad, container, false);
        mNativeBannerAdContainer =
                (RelativeLayout) view.findViewById(R.id.native_banner_ad_container);
        mNativeBannerAdStatusLabel = (TextView) view.findViewById(R.id.native_banner_status_label);

        mAdView = (LinearLayout) inflater.inflate(
                R.layout.native_banner_ad_unit,
                mNativeBannerAdContainer,
                false);

        mAdChoicesContainer = (RelativeLayout) mAdView.findViewById(R.id.ad_choices_container);

        Button showNativeBannerAdButton =
                (Button) view.findViewById(R.id.refresh_native_banner_button);
        showNativeBannerAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNativeBannerAdStatusLabel.setText(getString(R.string.loading_status));

                // Create a native ad request with a unique placement ID (generate your own on the
                // Facebook app settings). Use different ID for each ad placement in your app.
                mNativeBannerAd = new NativeBannerAd(getContext(), "YOUR_PLACEMENT_ID");

                // Set a listener to get notified when the ad was loaded.
                mNativeBannerAd.setAdListener(NativeBannerAdFragment.this);

                // When testing on a device, add its hashed ID to force test ads.
                // The hash ID is printed to log cat when running on a device and loading an ad.
                // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

                // Initiate a request to load an ad.
                mNativeBannerAd.loadAd(NativeAdBase.MediaCacheFlag.ALL);
            }
        });
        //  load the Native Banner when this fragment is created
        //  as the Banner in BannerFragment does.
        showNativeBannerAdButton.performClick();

        return view;
    }

    @Override
    public void onError(Ad ad, AdError error) {
        mNativeBannerAdStatusLabel.setText("Ad Failed to Load: " + error.getErrorMessage());
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (mNativeBannerAd == null || mNativeBannerAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }
        if (!isAdViewAdded) {
            isAdViewAdded = true;
            mNativeBannerAdContainer.addView(mAdView);
        }
        // Unregister last ad
        mNativeBannerAd.unregisterView();

        mNativeBannerAdStatusLabel.setText("");

        // Using the AdChoicesView is optional, but your native ad unit should
        // be clearly delineated from the rest of your app content. See
        // https://developers.facebook.com/docs/audience-network/guidelines/native-ads#native
        // for details. We recommend using the AdChoicesView.
        if (mAdChoicesView == null) {
            mAdChoicesView = new AdChoicesView(getActivity(), mNativeBannerAd, true);
            mAdChoicesContainer.addView(mAdChoicesView, 0);
        }

        inflateAd(mNativeBannerAd, mAdView, getContext());

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        mNativeBannerAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int i = view.getId();
                    if (i == R.id.native_ad_call_to_action) {
                        Log.d(TAG, "Call to action button clicked");
                    } else if (i == R.id.native_icon_view) {
                        Log.d(TAG, "Main image clicked");
                    } else {
                        Log.d(TAG, "Other ad component clicked");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(getActivity(), "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "onLoggingImpression");
    }

    @Override
    public void onMediaDownloaded(Ad ad) {
        Log.d(TAG, "onMediaDownloaded");
    }

    private void inflateAd(NativeBannerAd nativeBannerAd, View adView, final Context context) {
        // Create native UI using the ad metadata.
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext =
                (TextView) adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = (AdIconView) adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

    @Override
    public void onDestroy() {
        if (mNativeBannerAd != null) {
            mNativeBannerAd.unregisterView();
            mNativeBannerAd = null;
        }
        super.onDestroy();
    }
}
