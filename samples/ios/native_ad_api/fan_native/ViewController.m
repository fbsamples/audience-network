//
// Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
//
// You are hereby granted a non-exclusive, worldwide, royalty-free license to  
// use, copy, modify, and distribute this software in source code or binary form 
// for use in connection with the web services and APIs provided by Facebook.
//
// As with any software that integrates with the Facebook platform, your use of 
// this software is subject to the Facebook Developer Principles and Policies
// [http://developers.facebook.com/policy/], Your use of this software is also
// subject to the Audience Network Terms 
// [https://www.facebook.com/ads/manage/audience_network/publisher_tos]. 
// This copyright notice shall be included in all copies or substantial portions 
// of the software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
// PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
// HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION 
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
// SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

// Testing placement id
static NSString *adPlacementId = @"893127754073705_909205119132635";

FBNativeAd *nativeAd;
FBAdChoicesView *adChoicesView;

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view, typically from a nib.
    [self loadNativeAd];
}

- (void)loadNativeAd {
    nativeAd = [[FBNativeAd alloc] initWithPlacementID:adPlacementId];
    nativeAd.delegate = self;
    [nativeAd loadAd];
}

- (void)nativeAdDidLoad:(FBNativeAd *)nativeAd{
    
    [self.adTitleLabel setText:nativeAd.title];
    [self.adBodyLabel setText:nativeAd.body];
    [self.adSocialContextLabel setText:nativeAd.socialContext];
    [self.adCallToActionButton setTitle:nativeAd.callToAction forState:UIControlStateNormal];
    
    [nativeAd.icon loadImageAsyncWithBlock:^(UIImage *image) {
        [self.adIconImageView setImage:image];
    }];
    
    // Allocate a FBMediaView to contain the cover image or native video asset
    [self.adCoverMediaView setNativeAd:nativeAd];
    
    // Add adChoicesView
    adChoicesView = [[FBAdChoicesView alloc] initWithNativeAd:nativeAd];
    [self.adView addSubview:adChoicesView];
    [adChoicesView updateFrameFromSuperview];
    
    // Register the native ad view and its view controller with the native ad instance
    [nativeAd registerViewForInteraction:self.adView withViewController:self];
    
}

- (void)nativeAd:(FBNativeAd *)nativeAd didFailWithError:(NSError *)error{
    NSLog(@"Ad failed to load with error: %@", error);
}

- (BOOL) prefersStatusBarHidden {
    return YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
