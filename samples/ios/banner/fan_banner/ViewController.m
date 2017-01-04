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

- (void)viewDidLoad {
    [super viewDidLoad];
    
    //Replace YOUR_PLACEMENT_ID with your own placement id string.
    //If you don't have a placement id or don't know how to get one,
    //refer to the Getting Started Guide
    //https://developers.facebook.com/docs/audience-network/getting-started#placement_ids
    
    FBAdView *adView = [[FBAdView alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"
                                                      adSize:kFBAdSizeHeight50Banner
                                          rootViewController:self];
    adView.frame = CGRectMake(0, 20, adView.bounds.size.width, adView.bounds.size.height);
    adView.delegate = self;
    [adView loadAd];
    [self.view addSubview:adView];
}

- (void)adView:(FBAdView *)adView didFailWithError:(NSError *)error {
    NSLog(@"Ad failed to load: %i", (int)error.code);
}

- (void)adViewDidLoad:(FBAdView *)adView {
    NSLog(@"Ad was loaded and ready to be displayed");
}


@end
