// Copyright 2004-present Facebook. All Rights Reserved.

package com.facebook.audiencenetwork.adssample.adunit;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.facebook.audiencenetwork.adssample.R;

public class NativeBannerAdTemplateFragment extends Fragment implements NativeAdListener {

    private static final String TAG = NativeBannerAdTemplateFragment.class.getSimpleName();

    // Color set
    private int mBackgroundColor;
    private int mRowBackgroundColor;
    private int mTitleColor;
    private int mLinkColor;
    private int mContentColor;

    private NativeBannerAdView.Type mViewType = NativeBannerAdView.Type.HEIGHT_100;
    final private NativeAdViewAttributes mAdViewAttributes = new NativeAdViewAttributes();

    private TextView mStatusText;
    private @Nullable
    NativeBannerAd mNativeBannerAd;
    private LinearLayout mNativeAdContainer;
    private Spinner mBackgroundColorSpinner;
    private Spinner mAdViewTypeSpinner;
    private ScrollView mScrollView;
    private Button mShowCodeButton;

    private LinearLayout mRow1;
    private TextView mRow1Title;
    private TextView mRow1Content;
    private LinearLayout mRow2;
    private TextView mRow2Title;
    private TextView mRow2Subtitle1;
    private TextView mRow2Content1;
    private TextView mRow2Subtitle2;
    private TextView mRow2Content2;
    private LinearLayout mRow3;
    private TextView mRow3Title;
    private TextView mRow3Subtitle1;
    private TextView mRow3Content1;
    private TextView mRow3Subtitle2;
    private TextView mRow3Content2;
    private TextView mRow3Subtitle3;
    private TextView mRow3Content3;
    private TextView mRow3Subtitle4;
    private TextView mRow3Content4;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native_ad_template, container, false);

        // Get Views
        mRow1 = (LinearLayout) view.findViewById(R.id.row1);
        mRow1Title = (TextView) view.findViewById(R.id.row1_title);
        mRow1Content = (TextView) view.findViewById(R.id.row1_content);

        mRow2 = (LinearLayout) view.findViewById(R.id.row2);
        mRow2Title = (TextView) view.findViewById(R.id.row2_title);
        mRow2Subtitle1 = (TextView) view.findViewById(R.id.row2_subtitle1);
        mRow2Content1 = (TextView) view.findViewById(R.id.row2_content1);
        mRow2Subtitle2 = (TextView) view.findViewById(R.id.row2_subtitle2);
        mRow2Content2 = (TextView) view.findViewById(R.id.row2_content2);

        mRow3 = (LinearLayout) view.findViewById(R.id.row3);
        mRow3Title = (TextView) view.findViewById(R.id.row3_title);
        mRow3Subtitle1 = (TextView) view.findViewById(R.id.row3_subtitle1);
        mRow3Content1 = (TextView) view.findViewById(R.id.row3_content1);
        mRow3Subtitle2 = (TextView) view.findViewById(R.id.row3_subtitle2);
        mRow3Content2 = (TextView) view.findViewById(R.id.row3_content2);
        mRow3Subtitle3 = (TextView) view.findViewById(R.id.row3_subtitle3);
        mRow3Content3 = (TextView) view.findViewById(R.id.row3_content3);
        mRow3Subtitle4 = (TextView) view.findViewById(R.id.row3_subtitle4);
        mRow3Content4 = (TextView) view.findViewById(R.id.row3_content4);

        mStatusText = (TextView) view.findViewById(R.id.status);
        mNativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
        mScrollView = (ScrollView) view.findViewById(R.id.scrollView);
        mShowCodeButton = (Button) view.findViewById(R.id.showCodeButton);

        mBackgroundColorSpinner = (Spinner) view.findViewById(R.id.backgroundColorSpinner);
        ArrayAdapter<CharSequence> backgroundColorSpinnerAdapter =
                ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.background_color_array,
                        android.R.layout.simple_spinner_item);
        backgroundColorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mBackgroundColorSpinner.setAdapter(backgroundColorSpinnerAdapter);

        mAdViewTypeSpinner = (Spinner) view.findViewById(R.id.adViewTypeSpinner);
        ArrayAdapter<CharSequence> adViewTypeSpinnerAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.ad_bannerview_type_array, android.R.layout.simple_spinner_item);
        adViewTypeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        mAdViewTypeSpinner.setAdapter(adViewTypeSpinnerAdapter);

        setSpinnerListeners();
        setButtonListeners();

        createAndLoadNativeAd();

        return view;
    }

    protected void createAndLoadNativeAd() {
        // Create a native banner ad request with a unique placement ID
        // (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        mNativeBannerAd = new NativeBannerAd(getContext(), "YOUR_PLACEMENT_ID");

        // Set a listener to get notified when the ad was loaded.
        mNativeBannerAd.setAdListener(this);

        // Initiate a request to load an ad.
        mNativeBannerAd.loadAd();

        mStatusText.setText("Loading an ad...");
    }

    private void reloadAdContainer() {
        if (mNativeBannerAd != null && mNativeBannerAd.isAdLoaded()) {
            mNativeAdContainer.removeAllViews();

            // Create a NativeAdViewAttributes object (e.g. mAdViewAttributes)
            //   to render the native ads
            // Set the attributes
            mAdViewAttributes.setBackgroundColor(mRowBackgroundColor);
            mAdViewAttributes.setTitleTextColor(mTitleColor);
            mAdViewAttributes.setDescriptionTextColor(mContentColor);
            mAdViewAttributes.setButtonBorderColor(mLinkColor);
            mAdViewAttributes.setButtonTextColor(mLinkColor);

            // Use NativeAdView.render to generate the ad View
            // NativeAdViewType mViewType = NativeAdViewType.HEIGHT_100;
            View adView = NativeBannerAdView.render(
                    getActivity(),
                    mNativeBannerAd,
                    mViewType,
                    mAdViewAttributes);

            // Add adView to the container showing Ads
            mNativeAdContainer.addView(adView, 0);
            mNativeAdContainer.setBackgroundColor(Color.TRANSPARENT);

            mShowCodeButton.setText(R.string.show_code);
        }
    }

    private void setSpinnerListeners() {
        mBackgroundColorSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View view,
                                               int position,
                                               long id) {
                        int item = mBackgroundColorSpinner.getSelectedItemPosition();
                        switch (item) {
                            case 0:
                                mBackgroundColor = Color.LTGRAY;
                                mRowBackgroundColor = Color.WHITE;
                                mTitleColor = Color.argb(0xff, 0x4e, 0x56, 0x65);
                                mLinkColor = Color.argb(0xff, 0x3b, 0x59, 0x98);
                                mContentColor = Color.argb(0xff, 0x4e, 0x56, 0x65);
                                break;
                            case 1:
                                mBackgroundColor = Color.BLACK;
                                mRowBackgroundColor = Color.DKGRAY;
                                mTitleColor = Color.WHITE;
                                mLinkColor = Color.GREEN;
                                mContentColor = Color.WHITE;
                                break;
                            case 2:
                                mBackgroundColor = Color.BLUE;
                                mRowBackgroundColor = Color.TRANSPARENT;
                                mTitleColor = Color.WHITE;
                                mLinkColor = Color.CYAN;
                                mContentColor = Color.WHITE;
                                break;
                        }
                        resetAllColor();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        mAdViewTypeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0,
                                               View view,
                                               int position,
                                               long id) {
                        int item = mAdViewTypeSpinner.getSelectedItemPosition();
                        if (item == 0) {
                            mViewType = NativeBannerAdView.Type.HEIGHT_100;
                        } else if (item == 1) {
                            mViewType = NativeBannerAdView.Type.HEIGHT_120;
                        }
                        reloadAdContainer();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );
    }

    private void resetAllColor(){
        if (mBackgroundColor == Color.BLUE) {
            mScrollView.setBackgroundResource(R.drawable.background_blue);
        } else {
            mScrollView.setBackgroundColor(mBackgroundColor);
        }

        mRow1.setBackgroundColor(mRowBackgroundColor);
        mRow2.setBackgroundColor(mRowBackgroundColor);
        mRow3.setBackgroundColor(mRowBackgroundColor);

        mRow1Title.setTextColor(mTitleColor);
        mRow2Title.setTextColor(mTitleColor);
        mRow3Title.setTextColor(mTitleColor);

        mRow2Subtitle1.setTextColor(mLinkColor);
        mRow2Subtitle2.setTextColor(mLinkColor);
        mRow3Subtitle1.setTextColor(mLinkColor);
        mRow3Subtitle2.setTextColor(mLinkColor);
        mRow3Subtitle3.setTextColor(mLinkColor);
        mRow3Subtitle4.setTextColor(mLinkColor);

        mRow1Content.setTextColor(mContentColor);
        mRow2Content1.setTextColor(mContentColor);
        mRow2Content2.setTextColor(mContentColor);
        mRow3Content1.setTextColor(mContentColor);
        mRow3Content2.setTextColor(mContentColor);
        mRow3Content3.setTextColor(mContentColor);
        mRow3Content4.setTextColor(mContentColor);

        mScrollView.invalidate();

        reloadAdContainer();
    }

    private void setButtonListeners() {
        mShowCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowCodeButton.getText() == getResources().getString(R.string.show_ad)) {
                    reloadAdContainer();
                } else {
                    showCodeInAdContainer();
                }
            }
        });
    }

    private void showCodeInAdContainer() {
        String [] lines = getResources().getStringArray(R.array.code_snippet_banner);
        StringBuilder codeSnippet = new StringBuilder();
        for (int i=0; i < lines.length; i++) {
            codeSnippet.append(lines[i]).append("\r\n");
        }
        mNativeAdContainer.removeAllViews();
        TextView code = new TextView(getActivity());
        code.setText(codeSnippet);
        code.setBackgroundColor(Color.WHITE);
        code.setTextColor(Color.BLACK);
        mNativeAdContainer.addView(code, 0);

        mShowCodeButton.setText(R.string.show_ad);
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (mNativeBannerAd == null || mNativeBannerAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }

        mStatusText.setText("Ad loaded.");
        reloadAdContainer();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        mStatusText.setText("Ad failed to load: " + error.getErrorMessage());
    }

    @Override
    public void onAdClicked(Ad ad) {
        mStatusText.setText("Ad Clicked");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "onLoggingImpression");
    }

    @Override
    public void onMediaDownloaded(Ad ad) {
        Log.d(TAG, "onMediaDownloaded");
    }
}
