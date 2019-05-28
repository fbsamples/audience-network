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

#import "NativeViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

const NSUInteger LoadNativeAdCellRowIndex = 0;

@interface NativeViewController () <UITableViewDelegate, UITableViewDataSource, FBNativeAdDelegate, FBMediaViewDelegate>

@property (strong, nonatomic) IBOutlet UITableView *menuTableView;
@property (strong, nonatomic) IBOutlet UILabel *adStatusLabel;
@property (strong, nonatomic) IBOutlet FBMediaView *adIconView;
@property (strong, nonatomic) IBOutlet FBMediaView *adCoverMediaView;
@property (strong, nonatomic) IBOutlet UILabel *adTitleLabel;
@property (strong, nonatomic) IBOutlet UILabel *adBodyLabel;
@property (strong, nonatomic) IBOutlet UIButton *adCallToActionButton;
@property (strong, nonatomic) IBOutlet UILabel *adSocialContextLabel;
@property (strong, nonatomic) IBOutlet UILabel *sponsoredLabel;
@property (strong, nonatomic) IBOutlet FBAdOptionsView *adOptionsView;
@property (strong, nonatomic) IBOutlet UIView *adUIView;
@property (strong, nonatomic) FBNativeAd *nativeAd;

@end

@implementation NativeViewController

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    // Used in xib must be referenced here so linker won't eliminate it.
    [FBMediaView class];
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.adOptionsView.hidden = YES;
    self.menuTableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
}

#pragma mark - IB Actions

- (void)loadNativeAd
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
}

#pragma mark - FBNativeAdDelegate implementation

- (void)nativeAdDidLoad:(FBNativeAd *)nativeAd
{
    NSLog(@"Native ad was loaded, constructing native UI...");
    
    if (self.nativeAd) {
        [self.nativeAd unregisterView];
    }
    
    self.nativeAd = nativeAd;
    
    // Create native UI using the ad metadata.
    self.adCoverMediaView.delegate = self;
    
    self.adStatusLabel.text = @"Ad loaded.";
    
    // Render native ads onto UIView
    self.adTitleLabel.text = self.nativeAd.advertiserName;
    self.adBodyLabel.text = self.nativeAd.bodyText;
    self.adSocialContextLabel.text = self.nativeAd.socialContext;
    self.sponsoredLabel.text = self.nativeAd.sponsoredTranslation;
    
    [self setCallToActionButton:self.nativeAd.callToAction];
    
    // set the frame of the adBodyLabel depending on whether to show to call to action button
    CGFloat gapToBorder = 9.0f;
    CGFloat gapToCTAButton = 8.0f;
    CGRect adBodyLabelFrame = self.adBodyLabel.frame;
    if (!self.nativeAd.callToAction) {
        adBodyLabelFrame.size.width = self.adCoverMediaView.bounds.size.width - gapToBorder * 2;
    } else {
        adBodyLabelFrame.size.width = self.adCoverMediaView.bounds.size.width - gapToCTAButton - gapToBorder - (self.adCoverMediaView.bounds.size.width - self.adCallToActionButton.frame.origin.x);
    }
    self.adBodyLabel.frame = adBodyLabelFrame;
    
    NSLog(@"Register UIView for impression and click...");
    
    // Set native ad view tags to declare roles of your views for better analysis in future
    // We will be able to provide you statistics how often these views were clicked by users
    // Views provided by Facebook already have appropriate tag set
    self.adTitleLabel.nativeAdViewTag = FBNativeAdViewTagTitle;
    self.adBodyLabel.nativeAdViewTag = FBNativeAdViewTagBody;
    self.adSocialContextLabel.nativeAdViewTag = FBNativeAdViewTagSocialContext;
    self.adCallToActionButton.nativeAdViewTag = FBNativeAdViewTagCallToAction;
    
    // Specify the clickable areas. Views you were using to set ad view tags should be clickable.
    NSArray<UIView *> *clickableViews = @[self.adIconView, self.adTitleLabel, self.adBodyLabel, self.adSocialContextLabel, self.adCallToActionButton];
    [nativeAd registerViewForInteraction:self.adUIView
                               mediaView:self.adCoverMediaView
                                iconView:self.adIconView
                          viewController:self
                          clickableViews:clickableViews];
    
    //    // You may use this method if you want to have all adUIView's subviews clickable
    //    [nativeAd registerViewForInteraction:self.adUIView
    //                               mediaView:self.adCoverMediaView
    //                                iconView:self.adIconView
    //                          viewController:self];
    
    self.adOptionsView.hidden = NO;
    self.adOptionsView.nativeAd = nativeAd;
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

#pragma mark - Menu Table View

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [self menuItems].count;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == LoadNativeAdCellRowIndex) {
        [self loadNativeAd];
        return;
    }
    
    NSString *segue = [self segueIdentifierForIndexPath:indexPath];
    [self performSegueWithIdentifier:segue sender:nil];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"menuCell" forIndexPath:indexPath];
    cell.textLabel.text = [self menuItems][indexPath.row];
    cell.accessoryType = [self accessoryTypeForIndexPath:indexPath];
    return cell;
}

#pragma mark - Private Methods

- (UITableViewCellAccessoryType)accessoryTypeForIndexPath:(NSIndexPath *)indexPath
{
    return indexPath.row == LoadNativeAdCellRowIndex ? UITableViewCellAccessoryNone : UITableViewCellAccessoryDisclosureIndicator;
}

- (NSString *)segueIdentifierForIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.row == LoadNativeAdCellRowIndex) { return nil; }
    
    NSArray<NSString *> *segues =  @[@"showTemplate",
                                     @"showTableView",
                                     @"showScrollView",
                                     @"showCollectionView",
                                     @"showNativeBanner",
                                     @"showNativeBannerTemplate"];
    return segues[indexPath.row - 1];
}

- (NSArray<NSString *> *)menuItems
{
    return @[@"Load Native Ad",
             @"Load Native Ad using Template",
             @"Load Native Ad in TableView",
             @"Load Native Ad in ScrollView",
             @"Load Native Ad in CollectionView",
             @"Load Native Banner Ad",
             @"Load Native Banner Ad using Template"];
}

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

- (void)mediaViewDidLoad:(FBMediaView *)mediaView
{
    
}

@end
