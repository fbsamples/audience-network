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

#import "TableViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

static NSInteger const kRowStrideForAdCell = 9;
static NSString *const kDefaultCellIdentifier = @"kDefaultCellIdentifier";

@interface TableViewController () <UITableViewDataSource, UITableViewDelegate, FBNativeAdsManagerDelegate, FBNativeAdDelegate>

@property (nonatomic, strong) IBOutlet UITableView *tableView;
@property (nonatomic, strong) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (nonatomic, strong) FBNativeAdsManager *adsManager;
@property (nonatomic, strong) FBNativeAdTableViewCellProvider *cellProvider;
@property (nonatomic, strong) NSMutableArray<NSString *> *tableViewContentArray;
@property (nonatomic, assign) BOOL adCellsCreated;

@end

@implementation TableViewController

#pragma mark - Lazy Loading

- (NSMutableArray<NSString *> *)tableViewContentArray
{
    if (!_tableViewContentArray) {
        _tableViewContentArray = [NSMutableArray array];
        for (NSUInteger i = 0; i < 45; i++) {
            [_tableViewContentArray addObject:[NSString stringWithFormat:@"TableView Cell #%lu", (unsigned long)(i + 1)]];
        }
    }

    return _tableViewContentArray;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.tableView.delegate = self;
    self.tableView.dataSource = self;

    [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:kDefaultCellIdentifier];

    [self loadNativeAd];
}

- (IBAction)refresh:(id)sender
{
    [self loadNativeAd];
}

- (void)loadNativeAd
{
    [self.activityIndicator startAnimating];

    if (!self.adsManager) {
        // Create a native ad manager with a unique placement ID (generate your own on the Facebook app settings)
        // and how many ads you would like to create. Note that you may get fewer ads than you ask for.
        // Use different ID for each ad placement in your app.
        self.adsManager = [[FBNativeAdsManager alloc] initWithPlacementID:@"YOUR_PLACEMENT_ID"
                                                        forNumAdsRequested:5];
        // Set a delegate to get notified when the ads are loaded.
        self.adsManager.delegate = self;

        // Configure native ad manager to wait to call nativeAdsLoaded until all ad assets are loaded
        self.adsManager.mediaCachePolicy = FBNativeAdsCachePolicyAll;

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to console when running on a device.
        // [FBAdSettings addTestDevice:@"THE HASHED ID AS PRINTED TO CONSOLE"];
    }

    // Load some ads
    [self.adsManager loadAds];
}

#pragma mark FBNativeAdsManagerDelegate implementation

- (void)nativeAdsLoaded
{
    NSLog(@"Native ad was loaded, constructing native UI...");

    // After the native ads have loaded we create the native ad cell provider and let it take over
    FBNativeAdsManager *manager = self.adsManager;
    self.adsManager.delegate = nil;
    self.adsManager = nil;

    // The native ad cell provider operates over a loaded ads manager and can create table cells with native
    // ad templates in them as well as help with the math to have a consistent distribution of ads within a table.
    FBNativeAdTableViewCellProvider *cellProvider = [[FBNativeAdTableViewCellProvider alloc] initWithManager:manager forType:FBNativeAdViewTypeGenericHeight300];
    self.cellProvider = cellProvider;
    self.cellProvider.delegate = self;


    [self.tableView reloadData];
    [self.activityIndicator stopAnimating];
}

- (void)nativeAdsFailedToLoadWithError:(NSError *)error
{
    NSLog(@"Native ad failed to load with error: %@", error);
    [self.activityIndicator stopAnimating];

    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Native ad failed to load"
                                                                             message:@"Check console for more details"
                                                                      preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:nil];

    [alertController addAction:cancel];
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark FBNativeAdDelegate

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

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // In this example the ads are evenly distributed within the table every kRowStrideForAdCell-th cell.
    NSUInteger count = [self.tableViewContentArray count];
    count = [self.cellProvider adjustCount:count forStride:kRowStrideForAdCell] ?: count;
    return count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // For ad cells just as the ad cell provider, for normal cells do whatever you would do.
    if ([self.cellProvider isAdCellAtIndexPath:indexPath forStride:kRowStrideForAdCell]) {
        return [self.cellProvider tableView:tableView cellForRowAtIndexPath:indexPath];
    } else {
        UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kDefaultCellIdentifier forIndexPath:indexPath];
        // In this example we need to adjust the index back to the domain of the data.
        indexPath = [self.cellProvider adjustNonAdCellIndexPath:indexPath forStride:kRowStrideForAdCell] ?: indexPath;
        cell.textLabel.text = [self.tableViewContentArray objectAtIndex:indexPath.row];
        return cell;
    }
}

#pragma mark - UITableViewDelegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // The ad cell provider knows the height of ad cells based on its configuration
    if ([self.cellProvider isAdCellAtIndexPath:indexPath forStride:kRowStrideForAdCell]) {
        return [self.cellProvider tableView:tableView heightForRowAtIndexPath:indexPath];
    } else {
        return 80;
    }
}

#pragma mark - Orientation

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

@end
