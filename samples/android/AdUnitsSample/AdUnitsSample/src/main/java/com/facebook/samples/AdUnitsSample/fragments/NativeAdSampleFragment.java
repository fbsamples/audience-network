/**
 * Copyright (c) 2004-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 * copy, modify, and distribute this software in source code or binary form for use
 * in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase.NativeComponentTag;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.samples.AdUnitsSample.R;

import java.util.ArrayList;
import java.util.List;

public class NativeAdSampleFragment extends Fragment implements NativeAdListener {

    protected static final String TAG = NativeAdSampleFragment.class.getSimpleName();

    private @Nullable TextView nativeAdStatus;
    private @Nullable LinearLayout adChoicesContainer;

    private @Nullable NativeAdLayout nativeAdLayout;
    private @Nullable NativeAd nativeAd;
    private @Nullable AdOptionsView adOptionsView;
    private MediaView nativeAdMedia;

    private int originalScreenOrientationFlag;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        //  block auto screen orientation for NativeAdSampleFragment.
        if (getActivity() != null) {
            originalScreenOrientationFlag = getActivity().getRequestedOrientation();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        }

        View view = inflater.inflate(R.layout.fragment_native_ad_sample, container, false);
        nativeAdLayout = view.findViewById(R.id.native_ad_container);

        nativeAdStatus = view.findViewById(R.id.native_ad_status);
        adChoicesContainer = view.findViewById(R.id.ad_choices_container);

        Button showNativeAdButton = view.findViewById(R.id.load_native_ad_button);
        showNativeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nativeAdStatus != null) {
                    nativeAdStatus.setText(R.string.loading_status);
                }

                // Create a native ad request with a unique placement ID (generate your own on the
                // Facebook app settings). Use different ID for each ad placement in your app.
                nativeAd = new NativeAd(getActivity(), "YOUR_PLACEMENT_ID");

                // Set a listener to get notified when the ad was loaded.
                nativeAd.setAdListener(NativeAdSampleFragment.this);

                // When testing on a device, add its hashed ID to force test ads.
                // The hash ID is printed to log cat when running on a device and loading an ad.
                // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

                // Initiate a request to load an ad.
                nativeAd.loadAd();
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        if (getActivity() != null) {
            getActivity().setRequestedOrientation(originalScreenOrientationFlag);
        }
        adChoicesContainer = null;
        nativeAdLayout = null;
        adOptionsView = null;
        nativeAdStatus = null;
        super.onDestroyView();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        if (nativeAdStatus != null) {
            nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());
        }
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
        if (nativeAd == ad) {
            Log.d(TAG, "onMediaDownloaded");
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }

        if (nativeAdLayout == null) {
            return;
        }

        // Unregister last ad
        nativeAd.unregisterView();

        if (nativeAdStatus != null) {
            nativeAdStatus.setText("");
        }

        if (adChoicesContainer != null) {
            adOptionsView = new AdOptionsView(getActivity(), nativeAd, nativeAdLayout);
            adChoicesContainer.removeAllViews();
            adChoicesContainer.addView(adOptionsView, 0);
        }

        inflateAd(nativeAd, nativeAdLayout);

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        nativeAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int i = view.getId();
                    if (i == R.id.native_ad_call_to_action) {
                        Log.d(TAG, "Call to action button clicked");
                    } else if (i == R.id.native_ad_media) {
                        Log.d(TAG, "Main image clicked");
                    } else {
                        Log.d(TAG, "Other ad component clicked");
                    }
                }
                return false;
            }
        });
    }

    private void inflateAd(NativeAd nativeAd, View adView) {
        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        nativeAdMedia.setListener(getMediaViewListener());

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        sponsoredLabel.setText(R.string.sponsored);

        // You can use the following to specify the clickable areas.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdCallToAction);
        nativeAd.registerViewForInteraction(
                nativeAdLayout,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);

        // Optional: tag views
        NativeComponentTag.tagView(nativeAdIcon, NativeComponentTag.AD_ICON);
        NativeComponentTag.tagView(nativeAdTitle, NativeComponentTag.AD_TITLE);
        NativeComponentTag.tagView(nativeAdBody, NativeComponentTag.AD_BODY);
        NativeComponentTag.tagView(nativeAdSocialContext, NativeComponentTag.AD_SOCIAL_CONTEXT);
        NativeComponentTag.tagView(nativeAdCallToAction, NativeComponentTag.AD_CALL_TO_ACTION);
    }

    @Override
    public void onDestroy() {
        if (nativeAdMedia != null) {
            nativeAdMedia.destroy();
        }

        if (nativeAd != null) {
            nativeAd.unregisterView();
            nativeAd.destroy();
        }

        super.onDestroy();
    }

    private static MediaViewListener getMediaViewListener() {
        return new MediaViewListener() {
            @Override
            public void onVolumeChange(MediaView mediaView, float volume) {
                Log.i(TAG, "MediaViewEvent: Volume " + volume);
            }

            @Override
            public void onPause(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: Paused");
            }

            @Override
            public void onPlay(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: Play");
            }

            @Override
            public void onFullscreenBackground(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: FullscreenBackground");
            }

            @Override
            public void onFullscreenForeground(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: FullscreenForeground");
            }

            @Override
            public void onExitFullscreen(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: ExitFullscreen");
            }

            @Override
            public void onEnterFullscreen(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: EnterFullscreen");
            }

            @Override
            public void onComplete(MediaView mediaView) {
                Log.i(TAG, "MediaViewEvent: Completed");
            }
        };
    }
}
