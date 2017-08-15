/*
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary
 * form for use in connection with the web services and APIs provided by
 * Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.sample.admobwrapper;

import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;


public class FacebookCustomEventInterstitialForwarder implements InterstitialAdListener {
    private CustomEventInterstitialListener mInterstitialListener;

    private final static String TAG = FacebookCustomEventInterstitialForwarder.class.getSimpleName();

    public FacebookCustomEventInterstitialForwarder(CustomEventInterstitialListener listener) {
        this.mInterstitialListener = listener;
    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        Log.d(TAG, "FacebookCustomEventInterstitial displayed");
        // Interstitial displayed callback
        mInterstitialListener.onAdOpened();
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {
        Log.d(TAG, "FacebookCustomEventInterstitial dismissed");
        // Interstitial dismissed callback
        mInterstitialListener.onAdClosed();
    }

    @Override
    public void onError(Ad ad, AdError adError) {
        Log.e(TAG, "FacebookCustomEventInterstitial Error: " + adError.getErrorMessage());
        switch (adError.getErrorCode()) {
            case AdError.INTERNAL_ERROR_CODE:
                mInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                break;
            case AdError.NETWORK_ERROR_CODE:
                mInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                break;
            default:
                mInterstitialListener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                break;
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        Log.d(TAG, "FacebookCustomEventInterstitial loaded!");
        mInterstitialListener.onAdLoaded();
    }

    @Override
    public void onAdClicked(Ad ad) {
        Log.d(TAG, "FacebookCustomEventInterstitial clicked!");
        // Ad clicked callback
        mInterstitialListener.onAdClicked();
        mInterstitialListener.onAdLeftApplication();
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "FacebookCustomEventInterstitial impression logged!");
    }

}
