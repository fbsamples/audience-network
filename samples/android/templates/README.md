##Quick Start with Native Ad Templates for Audience Network
<hr/>

#####What are prerequisites for using Audience Network Native Ad Templates?#####
<p/>
For new publishers who are interested in or are planning to join Facebook Audience Network, please refer to '<a href=https://www.facebook.com/audiencenetwork/get-started/android>Get Started with Android</a>' on <a href=https://www.facebook.com/audiencenetwork>Facebook Audience Network</a> web site. Once you obtained `Facebook App Id`, `Audience Network Placement Id` and `Audience Network SDK`, please download the `Audience Network Support Library` and sample code listed in section of 'Where can I get the Audience Network Support Library?' to start utilizing native ad templates.

For existing Facebook Audience Network publishers, please downlaod the `Audience Network Support Library` and sample code listed in section of 'Where can I get the Audience Network Support Library?' to start utilizing the native ad templates.


#####What are Audience Network Native Ads?#####

<p>
Native ads give you the control to design the perfect ad units for your app. With our Native Ad API, you can determine the look and feel, size and location of your ads. Because you decide how the ads are formatted, ads can fit seamlessly in your application. Further, many publishers have seen an increase in monetization performance by deploying our native ads. See more information in our case studies, 
<a href="https://developers.facebook.com/docs/audience-network/case-studies">https://developers.facebook.com/docs/audience-network/case-studies</a>
</p>

#####What are Audience Network Native Ad Templates?#####

<p>
Audience Network Native Ad Templates are native ad units with predefined layouts which can be customized to fit into the UI more easily. Currently 3 templates are included in this support library: FB_BIG_CIRCLE, FB_MENU_BAR, and FB_HALF_FRAME. These ad units are designed to fit in the normal controls of an app's UI  and then expand to deliver a native ad.
</p>

#####How do I add Audience Network Native Ad Template into my app?#####

<p>
Audience Network Native Ad Templates are built on top of <a href="https://developers.facebook.com/docs/android">Audience Network SDK</a> and can be deployed in one step by integrating the audience-network-support library. 
</p>

#####Where can I get the Audience Network Support Library?#####

<p>
To integrate Audience Network Native Ad Template, you need to include the support library in your project. The support library '<a href="https://github.com/fbsamples/audience-network-support/tree/master/samples/android/templates/libs/audience-network-support.jar">audience-network-support.jar</a>' can be downloaded from <a href="https://github.com/fbsamples/audience-network-support/tree/master/samples/android/templates/libs">Facebook Open Source project</a> on GitHub.
</p>

#####What are the detailed steps to setup native ad template in an Android app project?#####

<p>
If you are already familiar or using Audience Network SDK, then you just need to follow 2 additional steps: add the new support library and add template in the code. If you are new or want review the steps in details, please refer to the following:
</p>
<br/>
1,  Adding Audience Network SDK into your app gradle</br>

```
dependencies {
  ...
  compile 'com.facebook.android:audience-network-sdk:4.+'
}
```

<br/>
2, Adding Native Ad Template library into your libs folder
<br/><br/>
Download the library 'audience-network-support.jar' from Facebook Open Source project and copy it into your project's libs folder, and make sure your gradle contains following line:
<br/>

```
dependencies {
  compile fileTree(dir: 'libs', include: ['*.jar'])
  ...
}
```
<br/>
Once added above, please sync the project to reflect the changes.
<br/><br/>


3, Implement the code<br/><br/>
Add the following code at the top of your activity class in order to import the support library<br/>
import com.facebook.audiencenetwork.ads.audience_network_support.NativeAdTemplateView;
<br/>
Add the following line to create the ad from template
<br/>

```
  // create ad view
  NativeAdTemplateView adView = new NativeAdTemplateView(this,
    "808505382626354_823834164426809",
    NativeAdTemplateView.Type.FB_BIG_CIRCLE,
    getCustomizedAttributes());
```

<br/>
The complete code looks like below for examples,                
<br/>

```
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // create ad view
    NativeAdTemplateView adView = new NativeAdTemplateView(this,
      "808505382626354_823834164426809",
      NativeAdTemplateView.Type.FB_BIG_CIRCLE,
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
```    


#####Where can I find a sample project or tutorial for this?#####
<p>
The sample projects are available on GitHub from Facebook Open Source project: <a href="https://github.com/fbsamples/audience-network-support/tree/master/samples/android/templates">fbsamples/audience-network-support</a>.
</p>

#####How do I report an issue or ask a question regarding to Audience Network Native Ad Template?#####
<p>
Any issues or question can be submitted from Audience Network Direct Support on your Audience Network Developer Dashboard.
</p>
