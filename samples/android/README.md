Samples for Android Platform
============================

Facebook Audience Network allows you to monetize your Android apps with Facebook ads. Here we have a list of sample apps that implements different ad placements for your reference.

Please make sure that you have completed the [Audience Network Getting Started][1] and [Android Getting Started][2] guides before you proceed.

Sample list
-----------

### [Ads Sample](./AdsSample)
This sample app demonstrates the implementation of [Banners][3], [Interstitials][4], [Native Ads][5], [Rewarded Videos][8] and [Horizontal Scroll][7].

### [scrollapp](./scrollapp)
This sample shows an example of integrating native ad unit as a native part of your app experience. The sample uses Android `RecyclerView` to display a list of stories, and render a native ad as one of the story. You can also refer to the [Native Ads implementation guide][5] for more details about the native ad placement.

### [AdMobWrapper](./AdMobWrapper)
When using mediation platforms, in order to get the most value out from Facebook Audience Network, it is recommended to use a placement ID optimized for CPM, as well as another placement ID optimized for fill rate in the same waterfall as fall back. When using AdMob as a mediation platform, you could only specify one placement ID from Audience Network for an AdMob ad unit. However by utilizing the [AdMob Custom Events][6] you can add a second Audience Network placement ID for one ad unit. This sample demonstrates how you can do that by adding a custom event and implement the wrapper yourself.  

### [NativeTransition](./NativeTransition)
This sample shows how you can use the Native Ad unit elements to make your own styled full screen ad at the app entrance, and use the Android shared element transition to shrink the same ad to some Native Ad unit on your application main screen, to add interest to the ad unit and get more attention from your user to improve the ad performance.

### [FlipAnimation](./FlipAnimation)
This is another sample that shows how you can use the Native Ad unit elements to make your own styled full screen ad at the app entrance. After the brand image launch screen, the brand image will transit into a full screen native ad with a flipping animation, in order to keep the users' attention at the center of the screen and improve the performance of the placement.

[1]: https://developers.facebook.com/docs/audience-network/getting-started
[2]: https://developers.facebook.com/docs/audience-network/android
[3]: https://developers.facebook.com/docs/audience-network/android-banners
[4]: https://developers.facebook.com/docs/audience-network/android-interstitial
[5]: https://developers.facebook.com/docs/audience-network/android-native
[6]: https://firebase.google.com/docs/admob/android/custom-events
[7]: https://developers.facebook.com/docs/audience-network/android/nativeadsmanager
[8]: https://developers.facebook.com/docs/audience-network/android/rewarded-video
