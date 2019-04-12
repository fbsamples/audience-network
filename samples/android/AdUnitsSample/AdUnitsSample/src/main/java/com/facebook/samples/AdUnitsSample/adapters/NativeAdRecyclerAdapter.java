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

package com.facebook.samples.AdUnitsSample.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.facebook.samples.AdUnitsSample.R;
import com.facebook.samples.AdUnitsSample.models.RecyclerPostItem;

import java.util.ArrayList;
import java.util.List;

public class NativeAdRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecyclerPostItem> mPostItems;
    private List<NativeAd> mAdItems;
    private NativeAdsManager mNativeAdsManager;
    private Activity mActivity;

    private static final int AD_DISPLAY_FREQUENCY = 5;
    private static final int POST_TYPE = 0;
    private static final int AD_TYPE = 1;

    public NativeAdRecyclerAdapter(Activity activity,
                                   List<RecyclerPostItem> postItems, NativeAdsManager
            nativeAdsManager) {
        mNativeAdsManager = nativeAdsManager;
        mPostItems = postItems;
        mAdItems = new ArrayList<>();
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            NativeAdLayout inflatedView = (NativeAdLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.native_ad_unit, parent, false);
            return new AdHolder(inflatedView);
        } else {
            View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout
                    .recycler_post_item, parent, false);
            return new PostHolder(inflatedView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position % AD_DISPLAY_FREQUENCY == 0 ? AD_TYPE : POST_TYPE;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == AD_TYPE) {
            NativeAd ad;

            if (mAdItems.size() > position / AD_DISPLAY_FREQUENCY) {
                ad = mAdItems.get(position / AD_DISPLAY_FREQUENCY);
            } else {
                ad = mNativeAdsManager.nextNativeAd();
                if (!ad.isAdInvalidated()) {
                    mAdItems.add(ad);
                } else {
                    Log.w(NativeAdRecyclerAdapter.class.getSimpleName(), "Ad is invalidated!");
                }
            }

            AdHolder adHolder = (AdHolder) holder;
            adHolder.adChoicesContainer.removeAllViews();

            if (ad != null) {

                adHolder.tvAdTitle.setText(ad.getAdvertiserName());
                adHolder.tvAdBody.setText(ad.getAdBodyText());
                adHolder.tvAdSocialContext.setText(ad.getAdSocialContext());
                adHolder.tvAdSponsoredLabel.setText(R.string.sponsored);
                adHolder.btnAdCallToAction.setText(ad.getAdCallToAction());
                adHolder.btnAdCallToAction.setVisibility(
                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                AdOptionsView adOptionsView =
                    new AdOptionsView(mActivity, ad, adHolder.nativeAdLayout);
                adHolder.adChoicesContainer.addView(adOptionsView, 0);

                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(adHolder.ivAdIcon);
                clickableViews.add(adHolder.mvAdMedia);
                clickableViews.add(adHolder.btnAdCallToAction);
                ad.registerViewForInteraction(
                        adHolder.nativeAdLayout,
                        adHolder.mvAdMedia,
                        adHolder.ivAdIcon,
                        clickableViews);
            }
        } else {
            PostHolder postHolder = (PostHolder) holder;

            //Calculate where the next postItem index is by subtracting ads we've shown.
            int index = position - (position / AD_DISPLAY_FREQUENCY) - 1;

            RecyclerPostItem postItem = mPostItems.get(index);
            postHolder.tvPostContent.setText(postItem.getPostContent());
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems.size() + mAdItems.size();
    }

    private static class PostHolder extends RecyclerView.ViewHolder {

        TextView tvPostContent;

        PostHolder(View view) {
            super(view);
            tvPostContent = view.findViewById(R.id.tvPostContent);
        }
    }

    private static class AdHolder extends RecyclerView.ViewHolder {

        NativeAdLayout nativeAdLayout;
        MediaView mvAdMedia;
        MediaView ivAdIcon;
        TextView tvAdTitle;
        TextView tvAdBody;
        TextView tvAdSocialContext;
        TextView tvAdSponsoredLabel;
        Button btnAdCallToAction;
        LinearLayout adChoicesContainer;

        AdHolder(NativeAdLayout adLayout) {
            super(adLayout);

            nativeAdLayout = adLayout;
            mvAdMedia = adLayout.findViewById(R.id.native_ad_media);
            tvAdTitle = adLayout.findViewById(R.id.native_ad_title);
            tvAdBody = adLayout.findViewById(R.id.native_ad_body);
            tvAdSocialContext = adLayout.findViewById(R.id.native_ad_social_context);
            tvAdSponsoredLabel = adLayout.findViewById(R.id.native_ad_sponsored_label);
            btnAdCallToAction = adLayout.findViewById(R.id.native_ad_call_to_action);
            ivAdIcon = adLayout.findViewById(R.id.native_ad_icon);
            adChoicesContainer = adLayout.findViewById(R.id.ad_choices_container);
        }
    }
}
