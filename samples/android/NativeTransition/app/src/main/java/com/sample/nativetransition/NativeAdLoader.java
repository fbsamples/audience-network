package com.sample.nativetransition;

import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;

public class NativeAdLoader {
    private NativeAd mNativeAd;
    private boolean isValid;
    private Context mContext;
    private static final String TAG = "NativeAdLoader";

    private static final NativeAdLoader instance = new NativeAdLoader();

    public static NativeAdLoader getInstance() {
        return instance;
    }

    public void setContext(Context context) {
        mContext = context.getApplicationContext();
    }

    public void loadAd(String placementID) {
        if (mContext == null) {
            return;
        }

        isValid = false;
        mNativeAd = new NativeAd(mContext, placementID);
        mNativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                isValid = true;
                Log.d(TAG, "Ad loaded!");
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        mNativeAd.loadAd();
    }

    public NativeAd getNativeAd() {
        if (!isValid) {
            return null;
        }

        return mNativeAd;
    }
}
