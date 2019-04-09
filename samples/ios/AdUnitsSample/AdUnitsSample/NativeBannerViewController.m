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

#import "NativeBannerViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface NativeBannerViewController () <FBNativeBannerAdDelegate>

@property (nonatomic, strong) IBOutlet UILabel *adStatusLabel;
@property (nonatomic, strong) IBOutlet FBAdIconView *adIconView;
@property (nonatomic, strong) IBOutlet UILabel *adTitleLabel;
@property (nonatomic, strong) IBOutlet UIButton *adCallToActionButton;
@property (nonatomic, strong) IBOutlet UILabel *adSponsoredLabel;
@property (nonatomic, strong) IBOutlet FBAdOptionsView *adOptionsView;
@property (nonatomic, strong) IBOutlet UIView *adUIView;
@property (nonatomic, strong) FBNativeBannerAd *nativeBannerAd;

@end

@implementation NativeBannerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.adOptionsView.hidden = YES;
}

#pragma mark - IB Actions

- (IBAction)loadAd:(id)sender
{
    self.adStatusLabel.text = @"Requesting an ad...";

    // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    FBNativeBannerAd *nativeBannerAd = [[FBNativeBannerAd alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"];

    // Set a delegate to get notified when the ad was loaded.
    nativeBannerAd.delegate = self;

    // When testing on a device, add its hashed ID to force test ads.
    // The hash ID is printed to console when running on a device.
    // [FBAdSettings addTestDevice:@"THE HASHED ID AS PRINTED TO CONSOLE"];

    // Initiate a request to load an ad.
    [nativeBannerAd loadAd];
}

#pragma mark - FBNativeBannerAdDelegate implementation

- (void)nativeBannerAdDidLoad:(FBNativeBannerAd *)nativeBannerAd
{
    NSLog(@"Native banner ad was loaded, constructing native UI...");

    if (self.nativeBannerAd) {
        [self.nativeBannerAd unregisterView];
    }

    self.nativeBannerAd = nativeBannerAd;

    self.adStatusLabel.text = @"Ad loaded.";

    // Render native ads onto UIView
    self.adTitleLabel.text = self.nativeBannerAd.advertiserName;
    self.adSponsoredLabel.text = self.nativeBannerAd.sponsoredTranslation;

    [self setCallToActionButton:self.nativeBannerAd.callToAction];

    NSLog(@"Register UIView for impression and click...");

    // Set native banner ad view tags to declare roles of your views for better analysis in future
    // We will be able to provide you statistics how often these views were clicked by users
    // Views provided by Facebook already have appropriate tag set
    self.adTitleLabel.nativeAdViewTag = FBNativeAdViewTagTitle;
    self.adCallToActionButton.nativeAdViewTag = FBNativeAdViewTagCallToAction;

    // Specify the clickable areas. View you were using to set ad view tags should be clickable.
    NSArray<UIView *> *clickableViews = @[self.adCallToActionButton];
    [nativeBannerAd registerViewForInteraction:self.adUIView
                                      iconView:self.adIconView
                                viewController:self
                                clickableViews:clickableViews];

    //    In you don't want to provide native ad view tags you can simply
    //    Wire up UIView with the native banner ad; the whole UIView will be clickable.
    //    [nativeBannerAd registerViewForInteraction:self.adUIView
    //                                      iconView:self.adIconView
    //                                viewController:self];

    self.adOptionsView.nativeAd = nativeBannerAd;
    self.adOptionsView.hidden = NO;
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

#pragma mark - Private Methods

- (void)setCallToActionButton:(NSString *)callToAction
{
    if (callToAction) {
        [self.adCallToActionButton setHidden:NO];
        [self.adCallToActionButton setTitle:callToAction
                                   forState:UIControlStateNormal];
    } else {
        [self.adCallToActionButton setHidden:YES];
    }
}

#pragma mark - Orientation

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

@end
