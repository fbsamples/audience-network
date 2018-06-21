//
//  ViewController.m
//  NativeAd
//
//  Created by Yun Peng Wang on 11/7/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@property (strong, nonatomic) FBNativeAd *nativeAd;

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
    // Use different ID for each ad placement in your app.
    FBNativeAd *nativeAd = [[FBNativeAd alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"];
    
    // Set a delegate to get notified when the ad was loaded.
    nativeAd.delegate = self;
    
    [nativeAd loadAd];
}


- (void)nativeAdDidLoad:(FBNativeAd *)nativeAd
{
    self.nativeAd = nativeAd;
    [self showNativeAd];
}

- (void)showNativeAd
{
    if (self.nativeAd && self.nativeAd.isAdValid) {
        [self.nativeAd unregisterView];
        
        // Wire up UIView with the native ad; only call to action button and media view will be clickable.
        [self.nativeAd registerViewForInteraction:self.adUIView
                                        mediaView:self.adCoverMediaView
                                         iconView:self.adIconImageView
                                   viewController:self
                                   clickableViews:@[self.adCallToActionButton, self.adCoverMediaView]];

        // Render native ads onto UIView
        self.adTitleLabel.text = self.nativeAd.advertiserName;
        self.adBodyLabel.text = self.nativeAd.bodyText;
        self.adSocialContextLabel.text = self.nativeAd.socialContext;
        self.sponsoredLabel.text = self.nativeAd.sponsoredTranslation;
        [self.adCallToActionButton setTitle:self.nativeAd.callToAction
                                   forState:UIControlStateNormal];
        self.adChoicesView.nativeAd = self.nativeAd;
        self.adChoicesView.corner = UIRectCornerTopRight;
    }
}

- (void)nativeAd:(FBNativeAd *)nativeAd didFailWithError:(NSError *)error
{
    NSLog(@"Native ad failed to load with error: %@", error.localizedDescription);
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

@end
