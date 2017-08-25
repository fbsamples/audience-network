/*
 * Copyright (c) 2017-present, Facebook, Inc. All rights reserved.
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

package sample.com.rcpy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> recipeList;
    private Context context;
    private static final int RECIPE = 0;
    private static final int NATIVE_AD = 1;

    RecipeListAdapter(Context context, List<Object> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == RECIPE) {
            View recipeItem = inflater.inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(recipeItem);
        } else if (viewType == NATIVE_AD) {
            View nativeAdItem = inflater.inflate(R.layout.item_native_ad, parent, false);
            return new NativeAdViewHolder(nativeAdItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == RECIPE) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            Recipe recipe = (Recipe) recipeList.get(position);
            SimpleDraweeView ivFood = recipeViewHolder.ivFood;
            TextView tvDescription = recipeViewHolder.tvDescription;
            TextView tvRecipeName = recipeViewHolder.tvRecipeName;
            LinearLayout llCategories = recipeViewHolder.llCategories;
            llCategories.removeAllViews(); // Clear the categories.

            ivFood.setImageURI("asset:///" + recipe.getImagePath());
            tvRecipeName.setText(recipe.getRecipeName());
            tvDescription.setText(recipe.getDescription());
            addFoodCategories(llCategories, recipe.getCategories());
        } else if (itemType == NATIVE_AD) {
            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) holder;
            NativeAd nativeAd = (NativeAd) recipeList.get(position);

            ImageView adImage = nativeAdViewHolder.adImage;
            TextView tvAdTitle = nativeAdViewHolder.tvAdTitle;
            TextView tvAdBody = nativeAdViewHolder.tvAdBody;
            Button btnCTA = nativeAdViewHolder.btnCTA;
            LinearLayout adChoicesContainer = nativeAdViewHolder.adChoicesContainer;
            MediaView mediaView = nativeAdViewHolder.mediaView;

            tvAdTitle.setText(nativeAd.getAdTitle());
            tvAdBody.setText(nativeAd.getAdBody());
            NativeAd.downloadAndDisplayImage(nativeAd.getAdIcon(), adImage);
            btnCTA.setText(nativeAd.getAdCallToAction());
            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
            adChoicesContainer.addView(adChoicesView);
            mediaView.setNativeAd(nativeAd);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(adImage);
            clickableViews.add(btnCTA);
            clickableViews.add(mediaView);
            nativeAd.registerViewForInteraction(nativeAdViewHolder.container, clickableViews);
        }
    }

    private void addFoodCategories(LinearLayout categoriesContainer, List<String> categories) {
        for (String category : categories) {
            ImageView iv = new ImageView(context);
            switch (category) {
                case "Meat":
                    iv.setImageResource(R.drawable.meat);
                    break;
                case "Carbs":
                    iv.setImageResource(R.drawable.carbs);
                    break;
                case "Veggie":
                    iv.setImageResource(R.drawable.veggie);
                    break;
                default:
                    continue;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70, 70);
            iv.setLayoutParams(layoutParams);
            categoriesContainer.addView(iv);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = recipeList.get(position);
        if (item instanceof Recipe) {
            return RECIPE;
        } else if (item instanceof Ad) {
            return NATIVE_AD;
        } else {
            return -1;
        }
    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvRecipeName;
        LinearLayout llCategories;
        SimpleDraweeView ivFood;

        RecipeViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tvRecipeName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            llCategories = (LinearLayout) itemView.findViewById(R.id.categories_container);
            ivFood = (SimpleDraweeView) itemView.findViewById(R.id.ivFood);
        }
    }

    private static class NativeAdViewHolder extends RecyclerView.ViewHolder {
        ImageView adImage;
        TextView tvAdTitle;
        TextView tvAdBody;
        Button btnCTA;
        View container;
        LinearLayout adChoicesContainer;
        MediaView mediaView;

        NativeAdViewHolder(View itemView) {
            super(itemView);
            this.container = itemView;
            adImage = (ImageView) itemView.findViewById(R.id.adImage);
            tvAdTitle = (TextView) itemView.findViewById(R.id.tvAdTitle);
            tvAdBody = (TextView) itemView.findViewById(R.id.tvAdBody);
            btnCTA = (Button) itemView.findViewById(R.id.btnCTA);
            adChoicesContainer = (LinearLayout) itemView.findViewById(R.id.adChoicesContainer);
            mediaView = (MediaView) itemView.findViewById(R.id.mediaView);
        }
    }
}