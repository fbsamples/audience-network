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

#import "ViewController.h"

@interface ViewController ()
@property (nonatomic, strong) GADInterstitial *interstitial;
@end

// AdMob ad unit IDs, in the format of @"ca-app-pub-xxxx/yyyy", you should replace with your own
static NSString *const MyBannerAdUnitID = @"ca-app-pub-xxxx/yyyy";
static NSString *const MyInterstitialAdUnitID = @"ca-app-pub-xxxx/yyyy";

static NSString *const ClickToLoadInterstital = @"Click to load interstitial";
static NSString *const InterstitalLoading = @"Interstitial loading...";
static NSString *const InterstitalLoaded = @"Interstitial loaded! Click to show!";


@implementation ViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Load the banner
    self.bannerView.adUnitID = MyBannerAdUnitID;
    self.bannerView.rootViewController = self;
    [self refreshBanner];
    
    // Interstital
    self.interstitialStatus.text = ClickToLoadInterstital;
}

- (IBAction)onShowInterstitial:(id)sender {
    if (self.interstitial.isReady) {
        [self.interstitial presentFromRootViewController:self];
    } else {
        NSLog(@"Interstitial wasn't ready!");
    }
}

- (IBAction)onLoadInterstitial:(id)sender {
    [self requestInterstitial];
}

- (IBAction)onRefreshBanner:(id)sender {
    [self refreshBanner];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)refreshBanner {
    GADRequest *request = [GADRequest request];
    [self.bannerView loadRequest:request];
}

- (void)requestInterstitial {
    self.interstitial =
    [[GADInterstitial alloc] initWithAdUnitID:MyInterstitialAdUnitID];
    self.interstitial.delegate = self;
    
    GADRequest *request = [GADRequest request];
    [self.interstitial loadRequest:request];

    self.interstitialStatus.text = InterstitalLoading;
}

- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
    self.interstitialStatus.text = InterstitalLoaded;
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
    self.interstitialStatus.text = ClickToLoadInterstital;
}

- (void)interstitial:(GADInterstitial *)ad didFailToReceiveAdWithError:(GADRequestError *)error {
    self.interstitialStatus.text = ClickToLoadInterstital;
    NSLog(@"%ld", (long)error.code);
}
@end
