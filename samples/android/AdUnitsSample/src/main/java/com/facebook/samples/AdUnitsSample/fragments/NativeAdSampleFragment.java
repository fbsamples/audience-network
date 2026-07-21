/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.MediaView;
import com.facebook.ads.MediaViewListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdBase.NativeComponentTag;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdOptionsViewPosition;
import com.facebook.common.preconditions.Preconditions;
import com.facebook.infer.annotation.Nullsafe;
import com.facebook.samples.AdUnitsSample.R;
import com.facebook.samples.ads.debugsettings.DebugToast;
import java.util.ArrayList;
import java.util.List;

@Nullsafe(Nullsafe.Mode.LOCAL)
public class NativeAdSampleFragment extends Fragment implements NativeAdListener {

  protected static final String TAG = NativeAdSampleFragment.class.getSimpleName();

  private @Nullable TextView nativeAdStatus;
  private @Nullable LinearLayout adChoicesContainer;

  private @Nullable NativeAdLayout nativeAdLayout;
  private @Nullable NativeAd nativeAd;
  // NULLSAFE_FIXME[Field Not Initialized]
  private MediaView nativeAdMedia;
  private NativeAd.NativeOptions mNativeOptions;

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_native_ad_sample, container, false);
    nativeAdLayout = view.findViewById(R.id.native_ad_container);

    nativeAdStatus = view.findViewById(R.id.native_ad_status);
    adChoicesContainer = view.findViewById(R.id.ad_choices_container);

    // Create native options to enable or disable the different options on media
    mNativeOptions = new NativeAd.NativeOptions();

    Switch disableFullScreenSwitch = view.findViewById(R.id.switch_fullscreen);
    if (disableFullScreenSwitch != null) {
      disableFullScreenSwitch.setOnCheckedChangeListener(
          (compoundButton, isChecked) -> mNativeOptions.setDisableFullScreen(isChecked));
    }

    Switch unMuteSwitch = view.findViewById(R.id.switch_unmute);
    if (unMuteSwitch != null) {
      unMuteSwitch.setOnCheckedChangeListener(
          (compoundButton, isChecked) -> mNativeOptions.setUnMuteVolume(isChecked));
    }

    Switch hideMediaControlsSwitch = view.findViewById(R.id.switch_hide_media_controls);
    if (hideMediaControlsSwitch != null) {
      hideMediaControlsSwitch.setOnCheckedChangeListener(
          (compoundButton, isChecked) -> mNativeOptions.setHideMediaControls(isChecked));
    }

    Button showNativeAdButton = view.findViewById(R.id.load_native_ad_button);
    Preconditions.checkNotNull(showNativeAdButton)
        .setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                if (nativeAdStatus != null) {
                  nativeAdStatus.setText(R.string.loading_status);
                }
                if (nativeAd != null) {
                  nativeAd.destroy();
                }

                // Create a native ad request with a unique placement ID (generate your own on the
                // Facebook app settings). Use different ID for each ad placement in your app.
                nativeAd = new NativeAd(requireActivity(), "YOUR_PLACEMENT_ID", mNativeOptions);

                // When testing on a device, add its hashed ID to force test ads.
                // The hash ID is printed to log cat when running on a device and loading an ad.
                // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

                // Initiate a request to load an ad.
                nativeAd.loadAd(
                    nativeAd
                        .buildLoadAdConfig()
                        .withAdListener(NativeAdSampleFragment.this)
                        .build());
              }
            });

    // if we already have loaded ad, render it
    if (nativeAd != null) {
      onAdLoaded(nativeAd);
    }

    return view;
  }

  @Override
  public void onDestroyView() {
    adChoicesContainer = null;
    nativeAdLayout = null;
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
    showToast("Ad Clicked");
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    showToast("Native Impression");
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

    if (!nativeAd.isAdLoaded() || nativeAd.isAdInvalidated()) {
      if (nativeAdStatus != null) {
        nativeAdStatus.setText("Ad is not loaded or invalidated.");
      }
      return;
    }

    // Let the SDK place the AdOptions icon and the "Ads served by Meta" credit line in the
    // top-right corner of the registered view. The publisher no longer needs to construct an
    // AdOptionsView or carve out container space for it — see
    // NativeBannerAdFragment for a back-compat example that still uses AdOptionsView directly.
    if (adChoicesContainer != null) {
      adChoicesContainer.removeAllViews();
    }
    nativeAd.setPreferredAdOptionsViewPosition(NativeAdOptionsViewPosition.TOP_RIGHT);

    inflateAd(nativeAd, nativeAdLayout);

    // Registering a touch listener to log which ad component receives the touch event.
    // We always return false from onTouch so that we don't swallow the touch event (which
    // would prevent click events from reaching the NativeAd control).
    // The touch listener could be used to do animations.
    nativeAd.setOnTouchListener(
        new View.OnTouchListener() {
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
    Log.d(TAG, "Aspect ratio of ad: " + nativeAd.getAspectRatio());

    // Create native UI using the ad metadata.
    MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
    TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
    TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
    TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
    TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
    Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

    // NULLSAFE_FIXME[Field Not Nullable]
    nativeAdMedia = adView.findViewById(R.id.native_ad_media);
    nativeAdMedia.setListener(getMediaViewListener());

    // Setting the Text
    Preconditions.checkNotNull(nativeAdSocialContext).setText(nativeAd.getAdSocialContext());
    // NULLSAFE_FIXME[Nullable Dereference]
    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
    // NULLSAFE_FIXME[Nullable Dereference]
    nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
    // NULLSAFE_FIXME[Nullable Dereference]
    nativeAdTitle.setText(nativeAd.getAdvertiserName());
    // NULLSAFE_FIXME[Nullable Dereference]
    nativeAdBody.setText(nativeAd.getAdBodyText());
    // NULLSAFE_FIXME[Nullable Dereference]
    sponsoredLabel.setText(R.string.sponsored);

    // You can use the following to specify the clickable areas.
    List<View> clickableViews = new ArrayList<>();
    clickableViews.add(nativeAdIcon);
    clickableViews.add(nativeAdMedia);
    clickableViews.add(nativeAdCallToAction);
    nativeAd.registerViewForInteraction(
        // NULLSAFE_FIXME[Parameter Not Nullable]
        nativeAdLayout, nativeAdMedia, nativeAdIcon, clickableViews);

    if (nativeAdMedia.isVideoContent()) {
      showToast("This is video content!!!");
      showToast("Video duration: " + nativeAdMedia.getVideoDuration());
    }

    // Optional: tag views
    // NULLSAFE_FIXME[Parameter Not Nullable]
    NativeComponentTag.tagView(nativeAdIcon, NativeComponentTag.AD_ICON);
    // NULLSAFE_FIXME[Parameter Not Nullable]
    NativeComponentTag.tagView(nativeAdTitle, NativeComponentTag.AD_TITLE);
    // NULLSAFE_FIXME[Parameter Not Nullable]
    NativeComponentTag.tagView(nativeAdBody, NativeComponentTag.AD_BODY);
    NativeComponentTag.tagView(nativeAdSocialContext, NativeComponentTag.AD_SOCIAL_CONTEXT);
    // NULLSAFE_FIXME[Parameter Not Nullable]
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

  private void showToast(String message) {
    if (isAdded()) {
      DebugToast.show(requireActivity(), message, Toast.LENGTH_SHORT);
    }
  }
}
