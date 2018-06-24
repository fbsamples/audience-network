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
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.audiencenetwork.adssample.R;

public class NativeAdTemplateFragment extends Fragment implements NativeAdListener {

    private static final String TAG = NativeAdTemplateFragment.class.getSimpleName();

    // Color set
    private int backgroundColor;
    private int rowBackgroundColor;
    private int titleColor;
    private int linkColor;
    private int contentColor;

    private NativeAdView.Type viewType = NativeAdView.Type.HEIGHT_300;
    final private NativeAdViewAttributes adViewAttributes = new NativeAdViewAttributes();

    private TextView statusText;
    private @Nullable
    NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private Spinner backgroundColorSpinner;
    private Spinner adViewTypeSpinner;
    private ScrollView scrollView;
    private Button showCodeButton;

    private LinearLayout row1;
    private TextView row1Title;
    private TextView row1Content;
    private LinearLayout row2;
    private TextView row2Title;
    private TextView row2Subtitle1;
    private TextView row2Content1;
    private TextView row2Subtitle2;
    private TextView row2Content2;
    private LinearLayout row3;
    private TextView row3Title;
    private TextView row3Subtitle1;
    private TextView row3Content1;
    private TextView row3Subtitle2;
    private TextView row3Content2;
    private TextView row3Subtitle3;
    private TextView row3Content3;
    private TextView row3Subtitle4;
    private TextView row3Content4;


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_native_ad_template, container, false);

        // Get Views
        row1 = (LinearLayout) view.findViewById(R.id.row1);
        row1Title = (TextView) view.findViewById(R.id.row1_title);
        row1Content = (TextView) view.findViewById(R.id.row1_content);

        row2 = (LinearLayout) view.findViewById(R.id.row2);
        row2Title = (TextView) view.findViewById(R.id.row2_title);
        row2Subtitle1 = (TextView) view.findViewById(R.id.row2_subtitle1);
        row2Content1 = (TextView) view.findViewById(R.id.row2_content1);
        row2Subtitle2 = (TextView) view.findViewById(R.id.row2_subtitle2);
        row2Content2 = (TextView) view.findViewById(R.id.row2_content2);

        row3 = (LinearLayout) view.findViewById(R.id.row3);
        row3Title = (TextView) view.findViewById(R.id.row3_title);
        row3Subtitle1 = (TextView) view.findViewById(R.id.row3_subtitle1);
        row3Content1 = (TextView) view.findViewById(R.id.row3_content1);
        row3Subtitle2 = (TextView) view.findViewById(R.id.row3_subtitle2);
        row3Content2 = (TextView) view.findViewById(R.id.row3_content2);
        row3Subtitle3 = (TextView) view.findViewById(R.id.row3_subtitle3);
        row3Content3 = (TextView) view.findViewById(R.id.row3_content3);
        row3Subtitle4 = (TextView) view.findViewById(R.id.row3_subtitle4);
        row3Content4 = (TextView) view.findViewById(R.id.row3_content4);

        statusText = (TextView) view.findViewById(R.id.status);
        nativeAdContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        showCodeButton = (Button) view.findViewById(R.id.showCodeButton);

        backgroundColorSpinner = (Spinner) view.findViewById(R.id.backgroundColorSpinner);
        ArrayAdapter<CharSequence> backgroundColorSpinnerAdapter =
                ArrayAdapter.createFromResource(
                        getActivity(),
                        R.array.background_color_array,
                        android.R.layout.simple_spinner_item);
        backgroundColorSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        backgroundColorSpinner.setAdapter(backgroundColorSpinnerAdapter);

        adViewTypeSpinner = (Spinner) view.findViewById(R.id.adViewTypeSpinner);
        ArrayAdapter<CharSequence> adViewTypeSpinnerAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.adview_type_array, android.R.layout.simple_spinner_item);
        adViewTypeSpinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        adViewTypeSpinner.setAdapter(adViewTypeSpinnerAdapter);

        setSpinnerListeners();
        setButtonListeners();

        createAndLoadNativeAd();

        return view;
    }

    protected void createAndLoadNativeAd() {
        // Create a native ad request with a unique placement ID
        // (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        nativeAd = new NativeAd(getActivity(), "YOUR_PLACEMENT_ID");

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(this);

        // Initiate a request to load an ad.
        nativeAd.loadAd();

        statusText.setText("Loading an ad...");
    }

    private void reloadAdContainer() {
        if (nativeAd != null && nativeAd.isAdLoaded()) {
            nativeAdContainer.removeAllViews();

            // Create a NativeAdViewAttributes object (e.g. adViewAttributes)
            //   to render the native ads
            // Set the attributes
            adViewAttributes.setBackgroundColor(rowBackgroundColor);
            adViewAttributes.setTitleTextColor(titleColor);
            adViewAttributes.setDescriptionTextColor(contentColor);
            adViewAttributes.setButtonBorderColor(linkColor);
            adViewAttributes.setButtonTextColor(linkColor);

            // Use NativeAdView.render to generate the ad View
            // NativeAdViewType viewType = NativeAdViewType.HEIGHT_100;
            View adView = NativeAdView.render(getActivity(), nativeAd, viewType, adViewAttributes);

            // Add adView to the container showing Ads
            nativeAdContainer.addView(adView, 0);
            nativeAdContainer.setBackgroundColor(Color.TRANSPARENT);

            showCodeButton.setText(R.string.show_code);
        }
    }

    private void setSpinnerListeners() {
        backgroundColorSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0,
                                               View view,
                                               int position,
                                               long id) {
                        int item = backgroundColorSpinner.getSelectedItemPosition();
                        switch (item) {
                            case 0:
                                backgroundColor = Color.LTGRAY;
                                rowBackgroundColor = Color.WHITE;
                                titleColor = Color.argb(0xff, 0x4e, 0x56, 0x65);
                                linkColor = Color.argb(0xff, 0x3b, 0x59, 0x98);
                                contentColor = Color.argb(0xff, 0x4e, 0x56, 0x65);
                                break;
                            case 1:
                                backgroundColor = Color.BLACK;
                                rowBackgroundColor = Color.DKGRAY;
                                titleColor = Color.WHITE;
                                linkColor = Color.GREEN;
                                contentColor = Color.WHITE;
                                break;
                            case 2:
                                backgroundColor = Color.BLUE;
                                rowBackgroundColor = Color.TRANSPARENT;
                                titleColor = Color.WHITE;
                                linkColor = Color.CYAN;
                                contentColor = Color.WHITE;
                                break;
                        }
                        resetAllColor();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );

        adViewTypeSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> arg0,
                                               View view,
                                               int position,
                                               long id) {
                        int item = adViewTypeSpinner.getSelectedItemPosition();
                        if (item == 0) {
                            viewType = NativeAdView.Type.HEIGHT_300;
                        } else if (item == 1) {
                            viewType = NativeAdView.Type.HEIGHT_400;
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
        if (backgroundColor == Color.BLUE) {
            scrollView.setBackgroundResource(R.drawable.background_blue);
        } else {
            scrollView.setBackgroundColor(backgroundColor);
        }

        row1.setBackgroundColor(rowBackgroundColor);
        row2.setBackgroundColor(rowBackgroundColor);
        row3.setBackgroundColor(rowBackgroundColor);

        row1Title.setTextColor(titleColor);
        row2Title.setTextColor(titleColor);
        row3Title.setTextColor(titleColor);

        row2Subtitle1.setTextColor(linkColor);
        row2Subtitle2.setTextColor(linkColor);
        row3Subtitle1.setTextColor(linkColor);
        row3Subtitle2.setTextColor(linkColor);
        row3Subtitle3.setTextColor(linkColor);
        row3Subtitle4.setTextColor(linkColor);

        row1Content.setTextColor(contentColor);
        row2Content1.setTextColor(contentColor);
        row2Content2.setTextColor(contentColor);
        row3Content1.setTextColor(contentColor);
        row3Content2.setTextColor(contentColor);
        row3Content3.setTextColor(contentColor);
        row3Content4.setTextColor(contentColor);

        scrollView.invalidate();

        reloadAdContainer();
    }

    private void setButtonListeners() {
        showCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showCodeButton.getText() == getResources().getString(R.string.show_ad)) {
                    reloadAdContainer();
                } else {
                    showCodeInAdContainer();
                }
            }
        });
    }

    private void showCodeInAdContainer() {
        String [] lines = getResources().getStringArray(R.array.code_snippet);
        StringBuilder codeSnippet = new StringBuilder();
        for (int i=0; i < lines.length; i++) {
            codeSnippet.append(lines[i]).append("\r\n");
        }
        nativeAdContainer.removeAllViews();
        TextView code = new TextView(getActivity());
        code.setText(codeSnippet);
        code.setBackgroundColor(Color.WHITE);
        code.setTextColor(Color.BLACK);
        nativeAdContainer.addView(code, 0);

        showCodeButton.setText(R.string.show_ad);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void onAdLoaded(Ad ad) {
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            return;
        }

        statusText.setText("Ad loaded.");
        reloadAdContainer();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        statusText.setText("Ad failed to load: " + error.getErrorMessage());
    }

    @Override
    public void onAdClicked(Ad ad) {
        statusText.setText("Ad Clicked");
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
