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

#import "NativeAdTemplateViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface NativeAdTemplateViewController () <FBNativeAdDelegate>

@property (strong, nonatomic) IBOutlet UILabel *adStatusLabel;
@property (strong, nonatomic) IBOutlet UIView *adContainerView;
@property (strong, nonatomic) IBOutlet NSLayoutConstraint *adContainerHeightConstraint;
@property (retain, nonatomic) IBOutlet NSLayoutConstraint *adContainerWidthConstraint;
@property (retain, nonatomic) IBOutlet UISlider *heightSlider;
@property (retain, nonatomic) IBOutlet UISlider *widthSlider;
@property (strong, nonatomic) FBNativeAd *nativeAd;
@property (strong, nonatomic) FBNativeAdViewAttributes *nativeAdAttributes;
@property (strong, nonatomic) FBNativeAdView *adView;

@end

@implementation NativeAdTemplateViewController

- (FBNativeAdViewAttributes *)nativeAdAttributes
{
    FBNativeAdViewAttributes *attributes = _nativeAdAttributes;

    if (!attributes) {
        attributes = [[FBNativeAdViewAttributes alloc] init];
        attributes.backgroundColor = [UIColor whiteColor];
        attributes.advertiserNameColor = [UIColor colorWithRed:0.11 green:0.12 blue:0.13 alpha:1.0];
        attributes.buttonColor = [UIColor colorWithRed:0.235 green:0.485 blue:1.000 alpha:1.000];
        attributes.buttonTitleColor = [UIColor whiteColor];
        attributes.titleColor = [UIColor darkGrayColor];
        attributes.descriptionColor = [UIColor grayColor];
        _nativeAdAttributes = attributes;
    }

    return attributes;
}

- (void)viewDidLayoutSubviews
{
    [super viewDidLayoutSubviews];

    self.heightSlider.value = self.adContainerHeightConstraint.constant;
    self.widthSlider.value = self.adContainerWidthConstraint.constant;
}

#pragma mark - Actions

- (IBAction)handleAdHeightChange:(id)sender
{
    CGFloat height = ((UISlider *)sender).value;
    self.adContainerHeightConstraint.constant = height;

}
- (IBAction)handleWidthChange:(id)sender
{
    CGFloat width = ((UISlider *)sender).value;
    self.adContainerWidthConstraint.constant = width;
}

- (IBAction)loadNativeAd
{
    self.adStatusLabel.text = @"Requesting an ad...";

    // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    FBNativeAd *nativeAd = [[FBNativeAd alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"];

    // Set a delegate to get notified when the ad was loaded.
    nativeAd.delegate = self;

    // When testing on a device, add its hashed ID to force test ads.
    // The hash ID is printed to console when running on a device.
    // [FBAdSettings addTestDevice:@"THE HASHED ID AS PRINTED TO CONSOLE"];

    // Initiate a request to load an ad.
    [nativeAd loadAd];

    [self.adView removeFromSuperview];
}

#pragma mark - FBNativeAdDelegate

- (void)nativeAdDidLoad:(FBNativeAd *)nativeAd
{
    if (self.nativeAd) {
        [self.nativeAd unregisterView];
    }

    self.nativeAd = nativeAd;

    self.adStatusLabel.text = @"Ad loaded.";

    if (nativeAd && nativeAd.isAdValid) {
        FBNativeAdView *adView = [FBNativeAdView nativeAdViewWithNativeAd:nativeAd
                                                           withAttributes:[self nativeAdAttributes]];
        adView.frame = self.adContainerView.bounds;
        adView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        [self.adContainerView addSubview:adView];
        self.adView = adView;
    }
}

- (void)nativeAd:(FBNativeAd *)nativeAd didFailWithError:(NSError *)error
{
    self.adStatusLabel.text = @"Ad failed to load. Check console for details.";
    NSLog(@"Native ad failed to load with error: %@", error);
}

- (void)nativeAdDidClick:(FBNativeAd *)nativeAd
{
    NSLog(@"Native ad was clicked.");
}

- (void)nativeAdDidFinishHandlingClick:(FBNativeAd *)nativeAd
{
    NSLog(@"Native ad did finish click handling.");
}

- (void)nativeAdWillLogImpression:(FBNativeAd *)nativeAd
{
    NSLog(@"Native ad impression is being captured.");
}

#pragma mark - Orientation

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

@end
