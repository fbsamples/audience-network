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

#import "MediumRectViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface MediumRectViewController () <FBAdViewDelegate>

@property (nonatomic, strong) IBOutlet UIView *adContainer;
@property (nonatomic, strong) IBOutlet UILabel *adStatusLabel;
@property (nonatomic, strong) FBAdView *mediumRectAdView;

@end

@implementation MediumRectViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadAd];
}

- (IBAction)refreshAd:(id)sender
{
    self.mediumRectAdView.hidden = YES;
    [self loadAd];
}

- (void)loadAd
{
    if (nil != self.mediumRectAdView) {
        [self.mediumRectAdView removeFromSuperview];
    }

    // Create the medium rectangle unit with a placement ID (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    FBAdSize adSize = kFBAdSizeHeight250Rectangle;
    self.mediumRectAdView = [[FBAdView alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"
                                                           adSize:adSize
                                               rootViewController:self];
    self.mediumRectAdView.hidden = YES;

    // Set a delegate to get notified on changes or when the user interact with the ad.
    self.mediumRectAdView.delegate = self;

    // Reposition the adView
    self.mediumRectAdView.frame = CGRectMake(0, 0, 300, 250);

    [self.adContainer addSubview:self.mediumRectAdView];

    self.adStatusLabel.text = @"Loading ad...";
    [self.mediumRectAdView loadAd];
}

#pragma mark - FBAdViewDelegate implementation

// Implement this function if you want to change the viewController after the FBAdView
// is created. The viewController will be used to present the modal view (such as the
// in-app browser that can appear when an ad is clicked).
// - (UIViewController *)viewControllerForPresentingModalView
// {
//   return self;
// }

- (void)adViewDidClick:(FBAdView *)adView
{
    NSLog(@"Ad was clicked.");
}

- (void)adViewDidFinishHandlingClick:(FBAdView *)adView
{
    NSLog(@"Ad did finish click handling.");
}

- (void)adViewDidLoad:(FBAdView *)adView
{
    self.adStatusLabel.text = @"Ad loaded.";
    NSLog(@"Ad was loaded.");
    // Now that the ad was loaded, show the view in case it was hidden before.
    self.mediumRectAdView.hidden = NO;
}

- (void)adView:(FBAdView *)adView didFailWithError:(NSError *)error
{
    self.adStatusLabel.text = [NSString stringWithFormat:@"Ad failed to load. Check console for details. %@", error.localizedDescription];
    NSLog(@"Ad failed to load with error: %@", error);

    // Hide the unit since no ad is shown.
    self.mediumRectAdView.hidden = YES;
}

- (void)adViewWillLogImpression:(FBAdView *)adView
{
    NSLog(@"Ad impression is being captured.");
}

@end
