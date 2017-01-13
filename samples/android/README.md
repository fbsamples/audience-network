Samples for Android Platform
============================

Facebook Audience Network allows you to monetize your Android apps with Facebook ads. Here we have a list of sample apps that implements different ad placements for your reference.

Please make sure that you have completed the [Audience Network Getting Started][1] and [Android Getting Started][2] guides before you proceed.

Sample list
-----------

### [Banner](./Banner)
This sample demonstrates the implementation of a banner ad placement. You can also refer to the [banner ad implementation guide][3] for more details about this placement.

### [Interstitial](./Interstitial)
This sample demonstrates the implementation of an interstitial ad unit. You can also refer to the [interstitial ad implementation guide][4] for more details about this placement.

### [NativeAd](./NativeAd)
This sample demonstrates the implementation of a native ad unit. You can also refer to the [native ad implementation guide][5] for more details about this placement.

### [native_ad_api](./native_ad_api)
This is another sample that demonstrates the implementation of a native ad unit. This sample also demonstrates how you can dynamically load and render native ads dynamically on `Button` clicks. You can also refer to the [native ad implementation guide][5] for more details about this placement.

### [scrollapp](./scrollapp)
This sample shows an example of integrating native ad unit as a native part of your app experience. The sample uses Android `RecyclerView` to display a list of stories, and render a native ad as one of the story. You can also refer to the [native ad implementation guide][5] for more details about the native ad placement.

### [AdMobWrapper](./AdMobWrapper)
When using mediation platforms, in order to get the most value out from Facebook Audience Network, it is recommended to use a placement ID optimized for CPM, as well as another placement ID optimized for fill rate in the same waterfall as fall back. When using AdMob as a mediation platform, you could only specify one placement ID from Audience Network for an AdMob ad unit. However by utilizing the [AdMob Custom Events][6] you can add a second Audience Network placement ID for one ad unit. This sample demonstrates how you can do that by adding a custom event and implement the wrapper yourself.  

### [NativeTransition](./NativeTransition)
This sample shows how you can use the Native Ad unit elements to make your own styled full screen ad at the app entrance, and use the Android shared element transition to shrink the same ad to some Native Ad unit on your application main screen, to add interest to the ad unit and get more attention from your user to improve the ad performance.

[1]: https://developers.facebook.com/docs/audience-network/getting-started
[2]: https://developers.facebook.com/docs/audience-network/android
[3]: https://developers.facebook.com/docs/audience-network/android-banners
[4]: https://developers.facebook.com/docs/audience-network/android-interstitial
[5]: https://developers.facebook.com/docs/audience-network/android-native
[6]: https://firebase.google.com/docs/admob/android/custom-events
