# AdMobWrapper Sample
This is a sample showing you how to write your own adapter for Audience Network banner and interstitial ads when using AdMob mediation.

 By default AdMob’s mediation tool only allow you to specify one ad unit ID from Facebook Audience Network, but in order to maximize the value you get from Facebook Audience Network, you are encouraged to try out the sandwich technique where you use two placement IDs from Facebook Audience Network, one optimized for CPM and the other optimized for fill rate.

## AdMob Setting
In your AdMob dashboard you should be able to add mediation network for your interstitial and banner placements. Add the first one for Audience Network as usual and another one as “Custom Event”.

In the Custom Event detailed setting you will need to specify:

* Class Name
    * You should fill in the full packaged class name of the custom event handler. For our sample this should be:
    * `com.sample.admobwrapper.FacebookCustomEventBanner` or `com.sample.admobwrapper.FacebookCustomEventInterstitial`
* Label
    * This is the name you give to this ad unit, which will appear in your mediation reports
* Parameter
    * This is where you should specify the full placement id you get from Facebook Audience Network

## Implementation
When the mediation is in action, AdMob will decide to use the platform according to the CPM levels that you set in the dashboard. When the custom event is being used, a call is sent from AdMob server to your app, requesting the **Class Name** that you specified to handle the request. After the ad loading request is handled, you are responsible to call the relevant call back functions to notify AdMob about the status of the ad, to log events like impression, click and error etc.

You can refer to the AdMob documents for more details:
* [Android Custom Events](https://firebase.google.com/docs/admob/android/custom-events)
* [iOS Custom Events](https://developers.google.com/admob/ios/custom-events)

### iOS

You will need to import both `FBAudienceNetwork.framework` and `GoogleMobileAds.framework`.
You can include `FacebookCustomEventBanner` and `FacebookCustomEventInterstitial` classes into your project and specify them in your AdMob mediation custom event to use them.

#### Banner
In the sample, `FacebookCustomEventBanner` handles the custom event banners.
The class implements `requestBannerAd` method and make a request to load the banner ad from Audience Network. It also implements `FBAdViewDelegate`so that it can set the delegate for the ad to itself to handle the call back functions.
In the callback functions, `GADCustomEventBannerDelegate`callback functions are invoked to notify AdMob about ad status.

#### Interstitial
Similar to the banner, `FacebookCustomEventInterstitial` handles the interstitials.
`requestInterstitialAdWithParameter`is implemented to handle the interstitial request, and `GADCustomEventInterstitialDelegate` callback functions are called to notify AdMob about the ad status.

### Android    

In the app gradle configuration, you will need to include both `com.google.firebase:firebase-ads:…` and `com.facebook.android:audience-network-sdk:…`
You can include `FacebookCustomEventBanner`, `FacebookCustomEventBannerForwarder`, `FacebookCustomEventInterstitial` and `FacebookCustomEventInterstitialForwarder` classes into your project and specify them in your AdMob mediation custom event to use them.

#### Banner
`FacebookCustomEventBanner` is the class that implements the `requestBannerAd` method to handle the request, and `FacebookCustomEventBannerForwarder` is the class that handles Audience Network banner ad callbacks and forward those to AdMob.

#### Interstitial
`FacebookCustomEventInterstitial` is the class that implements the `requestInterstitialAd` method to handle the request, and `FacebookCustomEventInterstitialForwarder` is the class that handles Audience Network banner ad callbacks and forward those to AdMob.

## Testing
After the above is done, expect some delay when AdMob applies the changes your made in the mediation setting to take effect. In order to test the ads from Audience Network, you can set all mediation CPMs manually, and use the geo location specific setting to disable ads from AdMob (by setting CPM from AdMob in your own country to 0)
