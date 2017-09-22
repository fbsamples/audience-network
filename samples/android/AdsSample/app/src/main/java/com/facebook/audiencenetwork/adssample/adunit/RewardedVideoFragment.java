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

package com.facebook.audiencenetwork.adssample.adunit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardData;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdListener;
import com.facebook.audiencenetwork.adssample.R;

public class RewardedVideoFragment extends Fragment implements RewardedVideoAdListener {

    private TextView rewardedVideoAdStatusLabel;
    private Button loadRewardedVideoButton;
    private Button showRewardedVideoButton;

    private RewardedVideoAd rewardedVideoAd;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rewarded_video, container, false);

        rewardedVideoAdStatusLabel = (TextView) view.findViewById(R.id.rewardedVideoAdStatusLabel);
        loadRewardedVideoButton = (Button) view.findViewById(R.id.loadRewardedVideoButton);
        showRewardedVideoButton = (Button) view.findViewById(R.id.showRewardedVideoButton);

        loadRewardedVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedVideoAd != null) {
                    rewardedVideoAd.destroy();
                    rewardedVideoAd = null;
                }
                rewardedVideoAd = new RewardedVideoAd(RewardedVideoFragment.this.getActivity(),
                        "YOUR_PLACEMENT_ID");
                rewardedVideoAd.setAdListener(RewardedVideoFragment.this);
                rewardedVideoAd.setRewardData(new RewardData("YOUR_USER_ID", "YOUR_REWARD"));
                rewardedVideoAd.loadAd();

                setStatusLabelText("Loading rewarded video ad...");
            }
        });

        showRewardedVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardedVideoAd == null || !rewardedVideoAd.isAdLoaded()) {
                    setStatusLabelText("Ad not loaded. Click load to request an ad.");
                } else {
                    rewardedVideoAd.show();
                    setStatusLabelText("");
                }
            }
        });

        return view;
    }

    @Override
    public void onError(Ad ad, AdError error) {
        if (ad == rewardedVideoAd) {
            setStatusLabelText("Rewarded video ad failed to load: " + error.getErrorMessage());
        }
    }

    @Override
    public void onAdLoaded(Ad ad) {
        if (ad == rewardedVideoAd) {
            setStatusLabelText("Ad loaded. Click show to present!");
        }
    }

    @Override
    public void onAdClicked(Ad ad) {
        showToast("Rewarded Video Clicked");
    }

    private void setStatusLabelText(String label) {
        if (rewardedVideoAdStatusLabel != null) {
            rewardedVideoAdStatusLabel.setText(label);
        }
    }

    private void showToast(String message) {
        if (isAdded()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {
        showToast("Rewarded Video View Complete");
    }

    @Override
    public void onLoggingImpression(Ad ad) {
        showToast("Rewarded Video Impression");
    }

    @Override
    public void onRewardedVideoClosed() {
        showToast("Rewarded Video Closed");
    }
}
