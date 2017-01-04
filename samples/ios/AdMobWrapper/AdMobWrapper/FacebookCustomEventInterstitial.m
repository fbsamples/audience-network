// Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
//
// You are hereby granted a non-exclusive, worldwide, royalty-free license to
// use, copy, modify, and distribute this software in source code or binary
// form for use in connection with the web services and APIs provided by
// Facebook.
//
// As with any software that integrates with the Facebook platform, your use of
// this software is subject to the Facebook Developer Principles and Policies
// [http://developers.facebook.com/policy/]. This copyright notice shall be
// included in all copies or substantial portions of the software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

#import "FacebookCustomEventInterstitial.h"
@import FBAudienceNetwork;

static NSString *const customEventErrorDomain = @"com.google.CustomEvent";

@interface FacebookCustomEventInterstitial () <FBInterstitialAdDelegate>
@property (nonatomic, strong) FBInterstitialAd *interstitialAd;
@end

@implementation FacebookCustomEventInterstitial
@synthesize delegate;

#pragma mark GADCustomEventInterstitial implementation

- (void)requestInterstitialAdWithParameter:(NSString *)serverParameter
                                     label:(NSString *)serverLabel
                                   request:(GADCustomEventRequest *)request {
    self.interstitialAd = [[FBInterstitialAd alloc] initWithPlacementID:serverParameter];
    self.interstitialAd.delegate = self;
    [self.interstitialAd loadAd];
}

- (void)presentFromRootViewController:(UIViewController *)rootViewController {
    if ([self.interstitialAd isAdValid]) {
        [self.interstitialAd showAdFromRootViewController:rootViewController];
    } else {
        NSLog(@"FacebookCustomEventInterstitial not ready yet!");
    }
}

#pragma mark FBInterstitialAdDelegate implementation

- (void)interstitialAdDidLoad:(FBInterstitialAd *)interstitialAd {
    NSLog(@"FacebookCustomEventInterstitial is loaded and ready to be displayed");
    [self.delegate customEventInterstitialDidReceiveAd:self];
}

- (void)interstitialAdWillLogImpression:(FBInterstitialAd *)interstitialAd {
    NSLog(@"The user sees the add");
    // Use this function as indication for a user's impression on the ad.
}

- (void)interstitialAdDidClick:(FBInterstitialAd *)interstitialAd {
    NSLog(@"The user clicked on the ad and will be taken to its destination");
    // Use this function as indication for a user's click on the ad.
    [self.delegate customEventInterstitialWasClicked:self];
    [self.delegate customEventInterstitialWillLeaveApplication:self];
}

- (void)interstitialAdWillClose:(FBInterstitialAd *)interstitialAd {
    NSLog(@"The user clicked on the close button, the ad is just about to close");
    // Consider to add code here to resume your app's flow
    [self.delegate customEventInterstitialWillDismiss:self];
}

- (void)interstitialAdDidClose:(FBInterstitialAd *)interstitialAd {
    NSLog(@"FacebookCustomEventInterstitial had been closed");
    // Consider to add code here to resume your app's flow
    [self.delegate customEventInterstitialDidDismiss:self];
}

- (void)interstitialAd:(FBInterstitialAd *)interstitialAd didFailWithError:(NSError *)error {
    NSLog(@"FacebookCustomEventInterstitial failed to load");
    
    // Handle error from Audience Network and forward to AdMob
    NSInteger errorCode = kGADErrorNoFill;
    switch (error.code) {
        case 1000:
            errorCode = kGADErrorNetworkError;
            break;
        case 1001:
            errorCode = kGADErrorNoFill;
            break;
        case 1002:
            errorCode = kGADErrorNoFill;
            break;
        case 2000:
            errorCode = kGADErrorServerError;
            break;
        case 2001:
            errorCode = kGADErrorInternalError;
            break;
        default:
            errorCode = kGADErrorNoFill;
            break;
    }

    NSError *gadError = [NSError errorWithDomain:customEventErrorDomain
                                            code:errorCode
                                        userInfo:nil];
    [self.delegate customEventInterstitial:self didFailAd:gadError];
}

@end
