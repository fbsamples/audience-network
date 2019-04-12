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
import com.facebook.samples.AdUnitsSample.R;

public class InstreamVideoFragment extends Fragment implements InstreamVideoAdListener {

  private static final String TAG = InstreamVideoFragment.class.getSimpleName();
  private static final String AD = "ad";

  private static final double DENSITY = Resources.getSystem().getDisplayMetrics().density;

  private TextView mInstreamVideoAdStatusLabel;
  private Button mShowInstreamVideoButton;
  private RelativeLayout mAdViewContainer;
  private InstreamVideoAdView mInstreamVideoAdView;

  private boolean mIsShowClicked = false;

  @Override
  public View onCreateView(
      LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_instream_video, container, false);

    mInstreamVideoAdStatusLabel = view.findViewById(R.id.instreamVideoAdStatusLabel);
    Button loadInstreamVideoButton = view.findViewById(R.id.loadInstreamVideoButton);
    mShowInstreamVideoButton = view.findViewById(R.id.showInstreamVideoButton);
    Button destroyInstreamVideoButton = view.findViewById(R.id.destroyInstreamVideoButton);
    mAdViewContainer = view.findViewById(R.id.adViewContainer);
    loadInstreamVideoButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        mIsShowClicked = false;
        if (mInstreamVideoAdView != null) {
          mInstreamVideoAdView.destroy();
          mAdViewContainer.removeAllViews();
        }
        mInstreamVideoAdView = new InstreamVideoAdView(
            InstreamVideoFragment.this.getActivity(),
            "YOUR_PLACEMENT_ID",
            new AdSize(
                (int) (mAdViewContainer.getMeasuredWidth() * DENSITY),
                (int) (mAdViewContainer.getMeasuredHeight() * DENSITY))
        );
        mInstreamVideoAdView.setAdListener(InstreamVideoFragment.this);
        mInstreamVideoAdView.loadAd();

        setStatusLabelText("Loading Instream video ad...");
      }
    });

    mShowInstreamVideoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mInstreamVideoAdView == null || !mInstreamVideoAdView.isAdLoaded()) {
          setStatusLabelText("Ad not loaded. Click load to request an ad.");
        } else {
          if (mInstreamVideoAdView.getParent() != mAdViewContainer) {
            mAdViewContainer.addView(mInstreamVideoAdView);
          }
          mInstreamVideoAdView.show();
          setStatusLabelText("Ad Showing");
          mIsShowClicked = true;
        }
      }
    });

    destroyInstreamVideoButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mInstreamVideoAdView == null) {
          return;
        }
        mInstreamVideoAdView.destroy();
        mInstreamVideoAdView = null;
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
      mInstreamVideoAdView = new InstreamVideoAdView(
          InstreamVideoFragment.this.getActivity(),
          adState
      );
      mInstreamVideoAdView.setAdListener(new InstreamVideoAdListener() {
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
          mShowInstreamVideoButton.callOnClick();
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
      mInstreamVideoAdView.loadAd();
    }

    return view;
  }

  @Override
  public void onError(Ad ad, AdError error) {
    if (ad == mInstreamVideoAdView) {
      setStatusLabelText("Instream video ad failed to load: " + error.getErrorMessage());
    }
  }

  @Override
  public void onAdLoaded(Ad ad) {
    if (ad == mInstreamVideoAdView) {
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
    if (mInstreamVideoAdStatusLabel != null) {
      mInstreamVideoAdStatusLabel.setText(label);
    }
  }

  @Override
  public void onAdVideoComplete(Ad ad) {
    Toast.makeText(this.getActivity(), "Instream Video Completed", Toast.LENGTH_SHORT).show();
    mAdViewContainer.removeView(mInstreamVideoAdView);
    mIsShowClicked = false;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (mInstreamVideoAdView == null) {
      return;
    }
    Bundle adState = mInstreamVideoAdView.getSaveInstanceState();
    if (adState != null) {
      outState.putBundle(AD, adState);
    }
  }

  @Override
  public void onStop() {
    if (mInstreamVideoAdView != null) {
      mAdViewContainer.removeView(mInstreamVideoAdView);
    }
    super.onStop();
  }

  @Override
  public void onResume() {
    if (mIsShowClicked &&
        mInstreamVideoAdView != null &&
        mInstreamVideoAdView.getParent() != mAdViewContainer) {
      mAdViewContainer.addView(mInstreamVideoAdView);
    }
    super.onResume();
  }
}
