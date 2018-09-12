Samples for iOS
===============

Facebook Audience Network allows you to monetize your iOS apps with Facebook ads. Here we have a list of sample apps that implements different ad placements for your reference.

Please make sure that you have completed the [Audience Network Getting Started][1] and [iOS Getting Started][2] guides before you proceed.

The sample projects are setup with [Cocoa Pods][7].  You would need to run `pod install` in the project folder from command line to download the latest Audience Network SDK.

Sample list
-----------

### [AdMobWrapper](./AdMobWrapper)
When using mediation platforms, in order to get the most value out from Facebook Audience Network, it is recommended to use a placement ID optimized for CPM, as well as another placement ID optimized for fill rate in the same waterfall as fall back. When using AdMob as a mediation platform, you could only specify one placement ID from Audience Network for an AdMob ad unit. However by utilizing the [AdMob Custom Events][6] you can add a second Audience Network placement ID for one ad unit. This sample demonstrates how you can do that by adding a custom event and implement the wrapper yourself.  

### [AdUnitsSample](./AdUnitsSample)
This sample includes demonstration for different placement formats including [Banner][3], [Interstitial][4], [Rewarded Video][9], [In-stream Video][8] [Native][5] and [Native Banner Ads][10].


[1]: https://developers.facebook.com/docs/audience-network/getting-started
[2]: https://developers.facebook.com/docs/audience-network/ios
[3]: https://developers.facebook.com/docs/audience-network/ios-banners
[4]: https://developers.facebook.com/docs/audience-network/ios-interstitial
[5]: https://developers.facebook.com/docs/audience-network/ios-native
[6]: https://firebase.google.com/docs/admob/ios/custom-events
[7]: https://cocoapods.org/
[8]: https://developers.facebook.com/docs/audience-network/ios/instream-video
[9]: https://developers.facebook.com/docs/audience-network/ios/rewarded-video
[10]: https://developers.facebook.com/docs/audience-network/ios-native-banner
