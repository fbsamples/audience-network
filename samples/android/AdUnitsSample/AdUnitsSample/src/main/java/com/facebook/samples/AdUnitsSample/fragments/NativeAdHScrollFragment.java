/**
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 * <p>
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 * <p>
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.facebook.samples.AdUnitsSample.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdScrollView;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdsManager;
import com.facebook.samples.AdUnitsSample.R;

public class NativeAdHScrollFragment extends Fragment implements NativeAdsManager.Listener {

    private static final int NATIVE_AD_VIEW_HEIGHT_DP = 300;

    private NativeAdsManager manager;
    private @Nullable NativeAdScrollView scrollView;
    private LinearLayout scrollViewContainer;
    private int originalScreenOrientationFlag;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        //  block auto screen orientation for NativeAdSampleFragment.
        if (getActivity() != null) {
            originalScreenOrientationFlag = getActivity().getRequestedOrientation();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native_ad_hscroll, container, false);

        manager = new NativeAdsManager(getActivity(), "YOUR_PLACEMENT_ID", 5);
        manager.setListener(this);
        manager.loadAds(NativeAd.MediaCacheFlag.ALL);

        Button reloadButton = (Button) view.findViewById(R.id.reload_hscroll);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.loadAds();
            }
        });

        scrollViewContainer = (LinearLayout) view.findViewById(R.id.hscroll_container);

        return view;
    }

    @Override
    public void onAdsLoaded() {
        if (getActivity() == null) {
            return;
        }

        Toast.makeText(getActivity(), "Ads loaded", Toast.LENGTH_SHORT).show();

        if (scrollView != null) {
            scrollViewContainer.removeView(scrollView);
        }

        scrollView = new NativeAdScrollView(getActivity(), manager, NATIVE_AD_VIEW_HEIGHT_DP);

        scrollViewContainer.addView(scrollView);
    }

    @Override
    public void onAdError(AdError error) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Ad error: " + error.getErrorMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(originalScreenOrientationFlag);
        }
        super.onDestroyView();
    }
}
