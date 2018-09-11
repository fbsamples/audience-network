/**
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

package com.facebook.audiencenetwork.adssample.adunit;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InstreamVideoAdListener;
import com.facebook.ads.InstreamVideoAdView;
import com.facebook.audiencenetwork.adssample.R;

public class InstreamVideoFragment extends Fragment implements InstreamVideoAdListener {

    private static final String TAG = InstreamVideoFragment.class.getSimpleName();
    private static final String AD = "ad";

    private TextView InstreamVideoAdStatusLabel;
    private Button loadInstreamVideoButton;
    private Button showInstreamVideoButton;
    private RelativeLayout mAdViewContainer;

    private InstreamVideoAdView instreamVideoAdView;
    private Button destroyInstreamVideoButton;

    private int pxToDP(int px) {
        return (int)(px / Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_instream_video, container, false);

        InstreamVideoAdStatusLabel = (TextView) view.findViewById(R.id.instreamVideoAdStatusLabel);
        loadInstreamVideoButton = (Button) view.findViewById(R.id.loadInstreamVideoButton);
        showInstreamVideoButton = (Button) view.findViewById(R.id.showInstreamVideoButton);
        destroyInstreamVideoButton= (Button) view.findViewById(R.id.destroyInstreamVideoButton);
        mAdViewContainer = (RelativeLayout)view.findViewById(R.id.adViewContainer);
        loadInstreamVideoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (instreamVideoAdView != null) {
                    instreamVideoAdView.destroy();
                    mAdViewContainer.removeAllViews();
                }
                instreamVideoAdView = new InstreamVideoAdView(
                        InstreamVideoFragment.this.getActivity(),
                        "YOUR_PLACEMENT_ID",
                        new AdSize(
                                pxToDP(mAdViewContainer.getMeasuredWidth()),
                                pxToDP(mAdViewContainer.getMeasuredHeight()))
                );
                instreamVideoAdView.setAdListener(InstreamVideoFragment.this);
                instreamVideoAdView.loadAd();

                setStatusLabelText("Loading Instream video ad...");
            }
        });

        showInstreamVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instreamVideoAdView == null || !instreamVideoAdView.isAdLoaded()) {
                    setStatusLabelText("Ad not loaded. Click load to request an ad.");
                } else {
                    if (instreamVideoAdView.getParent() != mAdViewContainer) {
                        mAdViewContainer.addView(instreamVideoAdView);
                    }
                    instreamVideoAdView.show();
                    setStatusLabelText("Ad Showing");
                }
            }
        });

        destroyInstreamVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instreamVideoAdView == null) {
                    return;
                }
                instreamVideoAdView.destroy();
                instreamVideoAdView = null;
                mAdViewContainer.removeAllViews();
                setStatusLabelText("Ad destroyed");
            }
        });

        // Restore state
        if (savedInstanceState == null) {
            return view;
        }
        Bundle adState = savedInstanceState.getBundle(AD);
        if (adState != null) {
            instreamVideoAdView = new InstreamVideoAdView(
                    InstreamVideoFragment.this.getActivity(),
                    adState
            );
            instreamVideoAdView.setAdListener(new InstreamVideoAdListener() {
                @Override
                public void onAdVideoComplete(Ad ad) {
                    InstreamVideoFragment.this.onAdVideoComplete(ad);
                }

                @Override
                public void onError(Ad ad, AdError error) {
                    InstreamVideoFragment.this.onError(ad, error);
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    InstreamVideoFragment.this.onAdLoaded(ad);
                    showInstreamVideoButton.callOnClick();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    InstreamVideoFragment.this.onAdClicked(ad);
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    InstreamVideoFragment.this.onLoggingImpression(ad);
                }
            });
            instreamVideoAdView.loadAd();
        }

        return view;
    }

    @Override
    public void onError(Ad ad, AdError error) {
        if (ad == instreamVideoAdView) {
            setStatusLabelText("Instream video ad failed to load: " + error.getErrorMessage());
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (ad == instreamVideoAdView) {
            setStatusLabelText("Ad loaded. Click show to present!");
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(this.getActivity(), "Instream Video Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        Log.d(TAG, "onLoggingImpression");
    }

    private void setStatusLabelText(String label) {
        if (InstreamVideoAdStatusLabel != null) {
            InstreamVideoAdStatusLabel.setText(label);
        }
    }

    @Override
    public void onAdVideoComplete(Ad ad) {
        Toast.makeText(this.getActivity(), "Instream Video Completed", Toast.LENGTH_SHORT).show();
        mAdViewContainer.removeView(instreamVideoAdView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (instreamVideoAdView == null) {
            return;
        }
        Bundle adState = instreamVideoAdView.getSaveInstanceState();
        if (adState != null) {
            outState.putBundle(AD, adState);
        }
    }

    @Override
    public void onStop() {
        if (instreamVideoAdView != null) {
            mAdViewContainer.removeView(instreamVideoAdView);
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        if (instreamVideoAdView != null && instreamVideoAdView.getParent() != mAdViewContainer) {
            mAdViewContainer.addView(instreamVideoAdView);
        }
        super.onResume();
    }
}
