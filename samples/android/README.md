Samples for Android Platform
============================

Facebook Audience Network allows you to monetize your Android apps with Facebook ads. Here we have a list of sample apps that implements different ad placements for your reference.

Please make sure that you have completed the [Audience Network Getting Started][1] and [Android Getting Started][2] guides before you proceed.

Sample list
-----------

### [Ads Sample](./AdsSample)
This sample app demonstrates the implementation of<br/>
[Banners][3], [Rectangle Ad][3], [Interstitials][4] and [Rewarded Videos][8].<br/>
[Native Ads][5], [Native Ads Template][9], [Native Banner Ads][10] and [Native Banner Ads Template][9].<br/>
[Native Ad Horizontal Scroll][7] and [Native Ads in Recycler View][7].<br/>

### [AdMobWrapper](./AdMobWrapper)
When using mediation platforms, in order to get the most value out from Facebook Audience Network, it is recommended to use a placement ID optimized for CPM, as well as another placement ID optimized for fill rate in the same waterfall as fall back. When using AdMob as a mediation platform, you could only specify one placement ID from Audience Network for an AdMob ad unit. However by utilizing the [AdMob Custom Events][6] you can add a second Audience Network placement ID for one ad unit. This sample demonstrates how you can do that by adding a custom event and implement the wrapper yourself.  

### [RCPY](./RCPY)
A tasty sample app for using native ads in `RecyclerView`


[1]: https://developers.facebook.com/docs/audience-network/getting-started
[2]: https://developers.facebook.com/docs/audience-network/android
[3]: https://developers.facebook.com/docs/audience-network/android-banners
[4]: https://developers.facebook.com/docs/audience-network/android-interstitial
[5]: https://developers.facebook.com/docs/audience-network/android-native
[6]: https://firebase.google.com/docs/admob/android/custom-events
[7]: https://developers.facebook.com/docs/audience-network/android/nativeadsmanager
[8]: https://developers.facebook.com/docs/audience-network/android/rewarded-video
[9]: https://developers.facebook.com/docs/audience-network/android/nativeadtemplate
[10]: https://developers.facebook.com/docs/audience-network/android-native-banner
