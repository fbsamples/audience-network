/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.fragments;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.samples.AdUnitsSample.R;

public class NativeAdTemplateFragment extends Fragment implements NativeAdListener {

  private static final String TAG = NativeAdTemplateFragment.class.getSimpleName();

  private static final int COLOR_LIGHT_GRAY = 0xff90949c;
  private static final int COLOR_DARK_GRAY = 0xff4e5665;
  private static final int COLOR_CTA_BLUE_BG = 0xff4080ff;

  private static final int MIN_HEIGHT_DP = 200;
  private static final int MAX_HEIGHT_DP = 500;
  private static final int DEFAULT_HEIGHT_DP = 350;
  private static final int DEFAULT_PROGRESS_DP = 50;

  private @Nullable NativeAd mNativeAd;
  private int mLayoutHeightDp = DEFAULT_HEIGHT_DP;

  private int mAdBackgroundColor, mTitleColor, mCtaTextColor, mContentColor, mCtaBgColor;

  private TextView mStatusText;
  private ViewGroup mNativeAdContainer;
  private Spinner mBackgroundColorSpinner;
  private Button mShowCodeButton, mReloadButton;
  private SeekBar mSeekBar;
  private View mAdView;

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_native_ad_template, container, false);

    mStatusText = view.findViewById(R.id.status);
    mNativeAdContainer = view.findViewById(R.id.templateContainer);
    mShowCodeButton = view.findViewById(R.id.showCodeButton);
    mReloadButton = view.findViewById(R.id.reloadAdButton);
    mBackgroundColorSpinner = view.findViewById(R.id.backgroundColorSpinner);
    mSeekBar = view.findViewById(R.id.seekBar);

    setUpLayoutBuilders();
    setUpButtons();

    createAndLoadNativeAd();

    return view;
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (mNativeAd == null || mNativeAd != ad) {
      // Race condition, load() called again before last ad was displayed
      return;
    }

    mStatusText.setText(R.string.ad_loaded);
    reloadAdContainer();
  }

  @Override
  public void onError(Ad ad, AdError error) {
    String msg = getResources().getString(R.string.ad_load_failed, error.getErrorMessage());
    mStatusText.setText(msg);
  }

  @Override
  public void onAdClicked(Ad ad) {
    mStatusText.setText(R.string.ad_clicked);
  }

  @Override
  public void onLoggingImpression(Ad ad) {
    Log.d(TAG, "onLoggingImpression");
  }

  @Override
  public void onMediaDownloaded(Ad ad) {
    Log.d(TAG, "onMediaDownloaded");
  }

  @Override
  public void onDestroy() {
    mNativeAd = null;
    super.onDestroy();
  }

  private void createAndLoadNativeAd() {
    // Create a native ad request with a unique placement ID
    // (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    mNativeAd = new NativeAd(getActivity(), "YOUR_PLACEMENT_ID");

    // Initiate a request to load an ad.
    mNativeAd.loadAd(mNativeAd.buildLoadAdConfig().withAdListener(this).build());

    mStatusText.setText(R.string.ad_loading);
  }

  private void reloadAdContainer() {
    Activity activity = getActivity();
    if (activity != null
        && mNativeAd != null
        && mNativeAd.isAdLoaded()
        && !mNativeAd.isAdInvalidated()) {
      mNativeAdContainer.removeAllViews();

      // Create a NativeAdViewAttributes object and set the attributes
      NativeAdViewAttributes attributes =
          new NativeAdViewAttributes()
              .setBackgroundColor(mAdBackgroundColor)
              .setTitleTextColor(mTitleColor)
              .setDescriptionTextColor(mContentColor)
              .setButtonBorderColor(mCtaTextColor)
              .setButtonTextColor(mCtaTextColor)
              .setButtonColor(mCtaBgColor);

      // Use NativeAdView.render to generate the ad View
      mAdView = NativeAdView.render(activity, mNativeAd, attributes);

      mNativeAdContainer.addView(mAdView, new ViewGroup.LayoutParams(MATCH_PARENT, 0));
      updateAdViewParams();

      mShowCodeButton.setText(R.string.show_code);
    }
  }

  private void setUpLayoutBuilders() {
    ArrayAdapter<CharSequence> backgroundColorSpinnerAdapter =
        ArrayAdapter.createFromResource(
            getActivity(), R.array.background_color_array, android.R.layout.simple_spinner_item);
    backgroundColorSpinnerAdapter.setDropDownViewResource(
        android.R.layout.simple_spinner_dropdown_item);
    mBackgroundColorSpinner.setAdapter(backgroundColorSpinnerAdapter);

    mBackgroundColorSpinner.setOnItemSelectedListener(
        new android.widget.AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
            int item = mBackgroundColorSpinner.getSelectedItemPosition();
            switch (item) {
              case 0:
                mAdBackgroundColor = Color.WHITE;
                mTitleColor = COLOR_DARK_GRAY;
                mCtaTextColor = COLOR_CTA_BLUE_BG;
                mContentColor = COLOR_LIGHT_GRAY;
                mCtaBgColor = Color.WHITE;
                break;
              case 1:
                mAdBackgroundColor = Color.BLACK;
                mTitleColor = Color.WHITE;
                mContentColor = Color.LTGRAY;
                mCtaTextColor = Color.BLACK;
                mCtaBgColor = Color.WHITE;
                break;
            }
            reloadAdContainer();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });

    mSeekBar.setProgress(DEFAULT_PROGRESS_DP);
    mSeekBar.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mLayoutHeightDp = progress * ((MAX_HEIGHT_DP - MIN_HEIGHT_DP) / 100) + MIN_HEIGHT_DP;
            updateAdViewParams();
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {}
        });
  }

  private void updateAdViewParams() {
    if (mAdView == null) {
      return;
    }
    ViewGroup.LayoutParams params = mAdView.getLayoutParams();
    params.height = (int) (Resources.getSystem().getDisplayMetrics().density * mLayoutHeightDp);
    mAdView.setLayoutParams(params);
    mAdView.requestLayout();
  }

  private void setUpButtons() {
    mShowCodeButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (mShowCodeButton.getText() == getResources().getString(R.string.show_ad)) {
              reloadAdContainer();
            } else {
              showCodeInAdContainer();
            }
          }
        });

    mReloadButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            createAndLoadNativeAd();
          }
        });
  }

  private void showCodeInAdContainer() {
    String[] lines = getResources().getStringArray(R.array.code_snippet_mediumrect_template);
    StringBuilder codeSnippet = new StringBuilder();
    for (String line : lines) {
      codeSnippet.append(line).append("\r\n");
    }
    mNativeAdContainer.removeAllViews();
    TextView code = new TextView(getActivity());
    code.setText(codeSnippet);
    code.setBackgroundColor(Color.WHITE);
    code.setTextColor(Color.BLACK);
    mNativeAdContainer.addView(code, 0);

    mShowCodeButton.setText(R.string.show_ad);
  }
}
