package com.facebook.audiencenetwork.ads.fan_template_03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.facebook.audiencenetwork.ads.audience_network_support.NativeAdTemplateView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create ad view
        NativeAdTemplateView adView = new NativeAdTemplateView(this,
                "YOUR_PLACEMENT_ID",
                NativeAdTemplateView.Type.FB_HALF_FRAME,
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
        // no customization
        return prefers;
    }

}
