// Copyright (c) 2014-present, Facebook, Inc. All rights reserved.
//
// You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
// copy, modify, and distribute this software in source code or binary form for use
// in connection with the web services and APIs provided by Facebook.
//
// As with any software that integrates with the Facebook platform, your use of
// this software is subject to the Facebook Developer Principles and Policies
// [http://developers.facebook.com/policy/]. This copyright notice shall be
// included in all copies or substantial portions of the software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
// FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
// COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
// IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
// CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

#import "NativeBannerAdTemplateViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface NativeBannerAdTemplateViewController () <FBNativeBannerAdDelegate>

@property (strong, nonatomic) IBOutlet UISegmentedControl *templateTypeControl;
@property (strong, nonatomic) IBOutlet UILabel *adStatusLabel;
@property (strong, nonatomic) IBOutlet UIView *adContainerView;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *adContainerHeightConstraint;
@property (strong, nonatomic) FBNativeBannerAd *nativeAd;
@property (strong, nonatomic) FBNativeAdViewAttributes *nativeAdAttributes;
@property (strong, nonatomic) FBNativeBannerAdView *adView;

@end

@implementation NativeBannerAdTemplateViewController

- (void)adjustAdContainerHeight
{
    switch ([self templateType]) {
        case FBNativeBannerAdViewTypeGenericHeight50:
            self.adContainerHeightConstraint.constant = 50.0f;
            break;
        case FBNativeBannerAdViewTypeGenericHeight100:
            self.adContainerHeightConstraint.constant = 100.f;
            break;
        case FBNativeBannerAdViewTypeGenericHeight120:
            self.adContainerHeightConstraint.constant = 120.f;
            break;
        default:
            break;
    }
}

- (FBNativeBannerAdViewType)templateType
{
    FBNativeBannerAdViewType type = FBNativeBannerAdViewTypeGenericHeight50;

    switch (self.templateTypeControl.selectedSegmentIndex) {
        case 0:
            type = FBNativeBannerAdViewTypeGenericHeight50;
            break;
        case 1:
            type = FBNativeBannerAdViewTypeGenericHeight100;
            break;
        case 2:
            type = FBNativeBannerAdViewTypeGenericHeight120;
            break;
    };

    return type;
}

#pragma mark - Actions

- (IBAction)loadNativeAd
{
    self.adStatusLabel.text = @"Requesting an ad...";

    // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    FBNativeBannerAd *nativeAd = [[FBNativeBannerAd alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"];

    // Set a delegate to get notified when the ad was loaded.
    nativeAd.delegate = self;

    // When testing on a device, add its hashed ID to force test ads.
    // The hash ID is printed to console when running on a device.
    // [FBAdSettings addTestDevice:@"THE HASHED ID AS PRINTED TO CONSOLE"];

    // Initiate a request to load an ad.
    [nativeAd loadAd];

    [self.adView removeFromSuperview];
    [self adjustAdContainerHeight];
}

#pragma mark - FBNativeAdDelegate

- (void)nativeBannerAdDidLoad:(FBNativeBannerAd *)nativeBannerAd
{
    if (self.nativeAd) {
        [self.nativeAd unregisterView];
    }

    self.nativeAd = nativeBannerAd;

    self.adStatusLabel.text = @"Ad loaded.";

    if (nativeBannerAd && nativeBannerAd.isAdValid) {
        FBNativeBannerAdView *adView = [FBNativeBannerAdView nativeBannerAdViewWithNativeBannerAd:nativeBannerAd
                                                                                         withType:[self templateType]];
        adView.frame = self.adContainerView.bounds;
        [self.adContainerView addSubview:adView];
        self.adView = adView;
    }
}

- (void)nativeBannerAd:(FBNativeBannerAd *)nativeBannerAd didFailWithError:(NSError *)error
{
    self.adStatusLabel.text = @"Ad failed to load. Check console for details.";
    NSLog(@"Native ad failed to load with error: %@", error);
}

- (void)nativeBannerAdDidClick:(FBNativeBannerAd *)nativeBannerAd
{
    NSLog(@"Native ad was clicked.");
}

- (void)nativeBannerAdDidFinishHandlingClick:(FBNativeBannerAd *)nativeBannerAd
{
    NSLog(@"Native ad did finish click handling.");
}

- (void)nativeBannerAdWillLogImpression:(FBNativeBannerAd *)nativeBannerAd
{
    NSLog(@"Native ad impression is being captured.");
}

#pragma mark - Orientation

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskAll;
}

@end
