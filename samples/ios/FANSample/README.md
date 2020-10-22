# FANSample <img align="left" height=42 src="https://facebookbrand.com/wp-content/uploads/2016/10/Audience_Network_Alvin_Logo_Grape_rgb1.png">

This Swift sample project demonstrates how to use various ad units supported by our Facebook Ads SDK.
Native ads will give you the flexibility to render the ad in a more native format that blends into your app. 
We also show how to wire up the delegate to get notified when the ad status changes over time or when the user interacts with the ad.

### Brief usage example

`FBNativeAd` can be used to show a native ad with a few simple steps:

```Swift
// Instantiate an ad with placement ID
let ad = FBNativeAd(placementID: "YOUR_PLACEMENT_ID")

// Wire up the FBNativeAdDelegate delegate (See documentation for more information).
// It is a good practice to set the delegate of the ad and implement the callback methods.
ad.delegate = self

// Load the ad
ad.loadAd()
```

More information regarding API usage can be found in the sample project.

### Samples shown in this project

- Interstitial ad (a full screen ad)
- Rewarded video ad (a full screen ad that shows a video).
- Native banner ad composed of native views.
- Native ad from template (layout that can be shown in various sizes)
- Native banner ad from template (layout that can be shown in various sizes).

### How to setup the project

1. Make sure [CocoPods](https://cocoapods.org/) is installed on your machine.
2. Go to the project directory and run `pod install`.
3. A workspace file named `FANSample.xcworkspace` will be generated. Open it using Xcode.
4. You can find the samples under `FANSamples/Samples`. Replace "YOUR_PLACEMENT_ID" with the Placement ID you created through [developer.facebook.com](https://developers.facebook.com/).
5. Run the project either on a simulator or a device.

### Test ads

When running the project on a simulator, test ads are served by default. However, on a device real ads will be served unless you specify otherwise.
If you test your app on a device, please make sure to call the following API to enable test ads:

`FBAdSettings.addTestDevice("<your device hash printed out on console>")`

### Additional resources

- [Audience Network Docs](https://developers.facebook.com/docs/audience-network)
- [Getting Started](https://developers.facebook.com/docs/audience-network/get-started/ios)
- [SDK Reference Documentation](https://developers.facebook.com/docs/audience-network/reference/)
