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

#import "ViewController.h"

#import "CollectionViewController.h"
#import "ScrollViewController.h"
#import "TableViewController.h"

const NSUInteger LoadNativeAdCellRowIndex = 0;

@interface ViewController () <UITableViewDelegate, UITableViewDataSource>

@property (strong, nonatomic) FBNativeAd *_nativeAd;
@property (strong, nonatomic) IBOutlet UITableView *menuTableView;

@end

@implementation ViewController

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

    self.adChoicesView.hidden = YES;
    self.menuTableView.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

    // Configure native ad to wait to call nativeAdDidLoad: until all ad assets are loaded
    nativeAd.mediaCachePolicy = FBNativeAdsCachePolicyAll;

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

    if (self._nativeAd) {
        [self._nativeAd unregisterView];
    }

    self._nativeAd = nativeAd;

    // Create native UI using the ad metadata.
    [self.adCoverMediaView setNativeAd:nativeAd];
    self.adCoverMediaView.delegate = self;

    __weak typeof(self) weakSelf = self;
    [self._nativeAd.icon loadImageAsyncWithBlock:^(UIImage *image) {
        __strong typeof(self) strongSelf = weakSelf;
        strongSelf.adIconImageView.image = image;
    }];
    self.adStatusLabel.text = @"Ad loaded.";

    // Render native ads onto UIView
    self.adTitleLabel.text = self._nativeAd.title;
    self.adBodyLabel.text = self._nativeAd.body;
    self.adSocialContextLabel.text = self._nativeAd.socialContext;
    self.sponsoredLabel.text = @"Sponsored";

    [self setCallToActionButton:self._nativeAd.callToAction];

    // set the frame of the adBodyLabel depending on whether to show to call to action button
    CGFloat gapToBorder = 9.0f;
    CGFloat gapToCTAButton = 8.0f;
    CGRect adBodyLabelFrame = self.adBodyLabel.frame;
    if (!self._nativeAd.callToAction) {
        adBodyLabelFrame.size.width = self.adCoverMediaView.bounds.size.width - gapToBorder * 2;
    } else {
        adBodyLabelFrame.size.width = self.adCoverMediaView.bounds.size.width - gapToCTAButton - gapToBorder - (self.adCoverMediaView.bounds.size.width - self.adCallToActionButton.frame.origin.x);
    }
    self.adBodyLabel.frame = adBodyLabelFrame;

    NSLog(@"Register UIView for impression and click...");

    // Wire up UIView with the native ad; the whole UIView will be clickable.
    [nativeAd registerViewForInteraction:self.adUIView
                      withViewController:self];

//     Or you can replace above call with following function, so you can specify the clickable areas.
//     NSArray<UIView *> *clickableViews = @[self.adCallToActionButton, self.adCoverMediaView];
//     [nativeAd registerViewForInteraction:self.adUIView
//                       withViewController:self
//                       withClickableViews:clickableViews];

    // Update AdChoices view
    self.adChoicesView.nativeAd = nativeAd;
    self.adChoicesView.corner = UIRectCornerTopRight;
    self.adChoicesView.hidden = NO;
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

    NSArray<NSString *> *segues =  @[@"showTableView",
                                    @"showScrollView",
                                    @"showCollectionView"];
    return segues[indexPath.row - 1];
}

- (NSArray<NSString *> *)menuItems
{
    return @[@"Load Native Ad",
             @"Load Native Ad in TableView",
             @"Load Native Ad in ScrollView",
             @"Load Native Ad in CollectionView"];
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

- (FBInterfaceOrientationMask)supportedInterfaceOrientations
{
  return UIInterfaceOrientationMaskPortrait;
}

- (void)mediaViewDidLoad:(FBMediaView *)mediaView
{

}

@end
