package com.fb.audiencenetwork.scrollapp;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.content.Context;

import com.facebook.ads.NativeAd;
import android.widget.Button;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdsManager;
import com.squareup.picasso.Picasso;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<NasaPost> mPosts;
    private Context mContext;
    private NativeAdsManager mAds;
    private NativeAd mAd = null;

    private int POST_TYPE = 1;
    private int AD_TYPE = 2;
    public static int AD_FORM = 2;

    public static class PostHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImage;
        private TextView mItemDate;
        private TextView mItemDescription;
        private NasaPost mPost;

        public PostHolder(View view) {
            super(view);

            mItemImage = (ImageView) view.findViewById(R.id.item_image);
            mItemDate = (TextView) view.findViewById(R.id.item_date);
            mItemDescription = (TextView) view.findViewById(R.id.item_description);
        }

        public void bindView(NasaPost post) {
            mPost = post;
            Picasso.with(mItemImage.getContext()).load(post.getUrl()).into(mItemImage);
            mItemDate.setText(post.getHumanDate());
            mItemDescription.setText(post.getExplanation());
        }
    }

    public static class AdHolder extends RecyclerView.ViewHolder {
        private MediaView mAdMedia;
        private ImageView mAdIcon;
        private TextView mAdTitle;
        private TextView mAdBody;
        private TextView mAdSocialContext;
        private Button mAdCallToAction;

        public AdHolder(View view) {
            super(view);

            if (AD_FORM == 2) {
                mAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
                mAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
                mAdCallToAction = (Button)view.findViewById(R.id.native_ad_call_to_action);
            }
            else {
                mAdMedia = (MediaView) view.findViewById(R.id.native_ad_media);
                mAdTitle = (TextView) view.findViewById(R.id.native_ad_title);
                mAdBody = (TextView) view.findViewById(R.id.native_ad_body);
                mAdSocialContext = (TextView) view.findViewById(R.id.native_ad_social_context);
                mAdCallToAction = (Button)view.findViewById(R.id.native_ad_call_to_action);
                mAdIcon = (ImageView)view.findViewById(R.id.native_ad_icon);
            }
        }

        public void bindView(NativeAd ad) {
            if (ad == null) {
                if (AD_FORM == 2) {
                    mAdSocialContext.setText("No Ad");
                }
                else {
                    mAdTitle.setText("No Ad");
                    mAdBody.setText("Ad is not loaded.");
                }
            }
            else {
                if (AD_FORM == 2) {
                    mAdSocialContext.setText(ad.getAdSocialContext());
                    mAdCallToAction.setText(ad.getAdCallToAction());
                    mAdMedia.setNativeAd(ad);
                }
                else {
                    mAdTitle.setText(ad.getAdTitle());
                    mAdBody.setText(ad.getAdBody());
                    mAdSocialContext.setText(ad.getAdSocialContext());
                    mAdCallToAction.setText(ad.getAdCallToAction());
                    mAdMedia.setNativeAd(ad);
                    NativeAd.Image adIcon = ad.getAdIcon();
                    NativeAd.downloadAndDisplayImage(adIcon, mAdIcon);
                }
            }
        }
    }

    public RecyclerAdapter(ArrayList<NasaPost> posts, Context context, NativeAdsManager ads) {
        mPosts = posts;
        mContext = context;
        mAds = ads;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1) {
            return AD_TYPE;
        }
        else {
            return POST_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == AD_TYPE) {
            View inflatedView;
            if (AD_FORM == 2) {
                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_unit2, parent, false);
            }
            else {
                inflatedView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_unit, parent, false);
            }
            return new AdHolder(inflatedView);
        }
        else {
            View inflatedView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_item, parent, false);
            return new PostHolder(inflatedView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == AD_TYPE) {
            if (mAd != null) {
                ((AdHolder)holder).bindView(mAd);
            }
            else if (mAds != null && mAds.isLoaded()) {
                mAd = mAds.nextNativeAd();
                ((AdHolder)holder).bindView(mAd);
            }
            else {
                ((AdHolder)holder).bindView(null);
            }
        }
        else {
            int index = position;
            if (index != 0) {
                index--;
            }
            NasaPost post = mPosts.get(index);
            ((PostHolder)holder).bindView(post);
        }
    }

    @Override
    public int getItemCount() {
        if (mPosts.size() == 0) {
            return mPosts.size();
        }
        else {
            return mPosts.size()+1;
        }
    }
}
