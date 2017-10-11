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

#import "BannerBidController.h"

#import <UIKit/UIKit.h>
#import "BidUtility.h"
#import "ORTBManager.h"
#import "ORTBSource.h"
#import "FBORTBSource.h"
#import "DummyORTBSource.h"
#import "BannerORTBImpression.h"

static NSString *placementID = @"256699801203835_326140227593125";

@interface BannerBidController ()

@property (weak, nonatomic) IBOutlet UILabel *adStatusLabel;
@property (nonatomic, strong) FBAdView *adView;
@property (nonatomic, strong) NSNumber *anBiddingPrice;
@property (nonatomic, strong) NSString *anBiddingPayload;
@property (nonatomic, strong) NSNumber *dummyBiddingPrice;
@property (nonatomic, strong) FBORTBSource *fbBidSource;
@property (nonatomic, strong) DummyORTBSource *dummyORTBSource;

@end

@implementation BannerBidController


- (void)loadAd
{
    _anBiddingPrice = nil;
    _anBiddingPayload = nil;
    _dummyBiddingPrice = nil;
    self.adStatusLabel.text = @"";
    [self.adView removeFromSuperview];
    self.adView = nil;
    NSString *tagID = @"256699801203835_326140227593125";
    NSString *appID = @"256699801203835";
    self.fbBidSource = [[FBORTBSource alloc] initWith:appID
                            publisherID:appID
                                  tagID:tagID];
    self.dummyORTBSource = [[DummyORTBSource alloc] initWith:@"" publisherID:@"" tagID:@""];

    BannerORTBImpression *impression = [[BannerORTBImpression alloc] initWith:@"banner_test_bid_req_imp_id"
                                                                        tagID:tagID
                                                                        width:(int) kFBAdSizeHeight50Banner.size.width
                                                                       height:(int) kFBAdSizeHeight50Banner.size.height];

    BannerBidController * __weak weakSelf = self;
    [[ORTBManager sharedManager] requestBid:self.fbBidSource
                   impression:impression
                    onSuccess:^(NSString * __nullable payload, NSNumber * __nullable price) {
        weakSelf.anBiddingPrice = [price copy];
        weakSelf.anBiddingPayload = [payload copy];
        [weakSelf complete];
    }];

    [[ORTBManager sharedManager] requestBid:self.dummyORTBSource
                   impression:impression
                    onSuccess:^(NSString * __nullable payload, NSNumber * __nullable price) {
        weakSelf.dummyBiddingPrice = [price copy];
        [weakSelf complete];
    }];
}

- (void)complete {
    if (self.dummyBiddingPrice && self.anBiddingPrice && self.anBiddingPayload) {
        if ([self.dummyBiddingPrice doubleValue] > [self.anBiddingPrice doubleValue]) {
            self.adStatusLabel.text = [NSString stringWithFormat:@"AN bidding (%3.2f) lost \n dummy bidding (%3.2f)", self.anBiddingPrice.doubleValue, self.dummyBiddingPrice.doubleValue];
        } else {
            self.adStatusLabel.text = [NSString stringWithFormat:@"AN bidding (%3.2f) win \n dummy bidding (%3.2f)", self.anBiddingPrice.doubleValue, self.dummyBiddingPrice.doubleValue];
            FBAdSize adSize = kFBAdSizeHeight50Banner;
            FBAdView *adView = [[FBAdView alloc] initWithPlacementID:placementID
                                                    adSize:kFBAdSizeHeight50Banner
                                        rootViewController:(UIViewController *)[NSObject new]];
            CGSize viewSize = self.view.bounds.size;
            CGSize tabBarSize = self.tabBarController.tabBar.frame.size;
            viewSize = CGSizeMake(viewSize.width, viewSize.height - tabBarSize.height);
            CGFloat bottomAlignedY = viewSize.height - adSize.size.height;
            adView.frame = CGRectMake(0, bottomAlignedY, viewSize.width, adSize.size.height);

            self.adView = adView;
            adView.delegate = self;

            // Add adView to the view hierarchy.
            [self.view addSubview:self.adView];
            [self.adView loadAdWithBidPayload:self.anBiddingPayload];
        }
    }
}

- (IBAction)bidAdTapped:(id)sender {
    [self loadAd];
}

#pragma mark - FBAdViewDelegate implementation

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
    //self.adStatusLabel.text = @"Ad loaded.";
    NSLog(@"Ad was loaded.");
    // Now that the ad was loaded, show the view in case it was hidden before.
    self.adView.hidden = NO;
}

- (void)adView:(FBAdView *)adView didFailWithError:(NSError *)error
{
    self.adStatusLabel.text = @"Ad failed to load. Check console for details.";
    NSLog(@"Ad failed to load with error: %@", error);

    // Hide the unit since no ad is shown.
    self.adView.hidden = YES;
}

- (void)adViewWillLogImpression:(FBAdView *)adView
{
    NSLog(@"Ad impression is being captured.");
}


@end


