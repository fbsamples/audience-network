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

#import "FacebookCustomEventBanner.h"

@import FBAudienceNetwork;

static NSString *const customEventErrorDomain = @"com.google.CustomEvent";

@interface FacebookCustomEventBanner () <FBAdViewDelegate>

@property (nonatomic, strong) FBAdView *bannerAd;

@end

@implementation FacebookCustomEventBanner

@synthesize delegate;

- (void)requestBannerAd:(GADAdSize)adSize
              parameter:(NSString *)serverParameter
                  label:(NSString *)serverLabel
                request:(GADCustomEventRequest *)request {
    
    UIViewController *rootViewController =
        [UIApplication sharedApplication].keyWindow.rootViewController;
    
    // Create the bannerView with the appropriate size.
    self.bannerAd = [[FBAdView alloc] initWithPlacementID:serverParameter
                                                   adSize:kFBAdSize320x50
                                       rootViewController:rootViewController];
    
    self.bannerAd.delegate = self;
    [self.bannerAd loadAd];
}

- (void)adViewDidClick:(FBAdView *)adView {
    NSLog(@"FacebookCustomEventBanner ad was clicked.");
    [self.delegate customEventBannerWasClicked:self];
}

- (void)adViewDidFinishHandlingClick:(FBAdView *)adView {
    NSLog(@"FacebookCustomEventBanner ad did finish click handling.");
    [self.delegate customEventBannerWillLeaveApplication:self];
}

- (void)adViewWillLogImpression:(FBAdView *)adView {
    NSLog(@"FacebookCustomEventBanner ad impression is being captured.");
}

- (void)adView:(FBAdView *)adView didFailWithError:(NSError *)error {
    NSLog(@"FacebookCustomEventBanner failed to load");
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
    [self.delegate customEventBanner:self didFailAd:gadError];
}

- (void)adViewDidLoad:(FBAdView *)adView {
    NSLog(@"FacebookCustomEventBanner was loaded and ready to be displayed");
    [self.delegate customEventBanner:self didReceiveAd:adView];
}

@end
