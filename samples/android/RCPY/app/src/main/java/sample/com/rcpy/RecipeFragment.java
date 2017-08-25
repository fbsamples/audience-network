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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.NativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecipeFragment extends Fragment {

    private List<Object> recipeList;
    RecipeListAdapter recipeListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        try {
            recipeList = getRecipeListFromJSON(loadJSONFromAsset(view.getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(view
                .getContext(),
                layoutManager.getOrientation());
        rvRecipes.addItemDecoration(dividerItemDecoration);
        recipeListAdapter = new RecipeListAdapter(view.getContext(), recipeList);
        rvRecipes.setAdapter(recipeListAdapter);
        rvRecipes.setLayoutManager(layoutManager);

        NativeAd nativeAd = new NativeAd(this.getContext(), "YOUR_PLACEMENT_ID");
        nativeAd.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                recipeList.add(4, ad);
                recipeListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        nativeAd.loadAd();

        return view;
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public List<Object> getRecipeListFromJSON(String json) throws JSONException {
        JSONArray recipes = new JSONArray(json);
        List<Object> recipeList = new ArrayList<>();
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipeObj = (JSONObject) recipes.get(i);
            Recipe recipe = new Recipe();
            recipe.setImagePath(recipeObj.getString("image_path"));
            recipe.setRecipeName(recipeObj.getString("name"));
            recipe.setDescription(recipeObj.getString("description"));

            JSONArray jsonCategories = recipeObj.getJSONArray("categories");
            ArrayList<String> categories = new ArrayList<>();
            for (int j = 0; j < jsonCategories.length(); j++) {
                categories.add((String) jsonCategories.get(j));
            }
            recipe.setCategories(categories);
            recipeList.add(recipe);
        }
        return recipeList;
    }
}