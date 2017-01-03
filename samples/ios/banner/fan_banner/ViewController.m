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

static NSString *adPlacementId = @"893127754073705_909121259141021";

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self loadBanner];
}

- (void) loadBanner {

    FBAdView *adView = [[FBAdView alloc] initWithPlacementID:adPlacementId adSize:kFBAdSizeHeight50Banner rootViewController:self];
    adView.delegate = self;
    [adView loadAd];
    
    [self.view addSubview:adView];
    adView.frame = CGRectOffset(adView.frame, 0, 42);
}

- (void)adView:(FBAdView *)adView didFailWithError:(NSError *)error {
    NSLog(@"Ad failed to load: %i", (int)error.code);
}

- (void)adViewDidLoad:(FBAdView *)adView {
    NSLog(@"Ad was loaded and ready to be displayed");
}


-(BOOL)prefersStatusBarHidden {
    return YES;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
