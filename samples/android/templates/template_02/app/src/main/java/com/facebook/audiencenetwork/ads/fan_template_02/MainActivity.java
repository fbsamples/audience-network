package com.facebook.audiencenetwork.ads.fan_template_02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.facebook.audiencenetwork.ads.audience_network_support.NativeAdTemplateView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static final boolean CUSTOMIZE_TEMPLATE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create ad view
        NativeAdTemplateView adView = new NativeAdTemplateView(this,
                "YOUR_PLACEMENT_ID",
                NativeAdTemplateView.Type.FB_MENU_BAR,
                getCustomizedAttributes());

        // place ad view in main UI
        placeAdInView(adView);
    }

    protected void placeAdInView(NativeAdTemplateView adView) {
        RelativeLayout iconContainer = (RelativeLayout) findViewById(R.id.ad_container);
        iconContainer.addView(adView);
    }

    protected JSONObject getCustomizedAttributes() {
        JSONObject prefers = new JSONObject();
        if (CUSTOMIZE_TEMPLATE) {
            // customize icon and title
            try {
                prefers.put("stock_image", true);
                prefers.put("stock_resource", getResources().getIdentifier("icon_purple", "drawable", getPackageName()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return prefers;
    }
}
