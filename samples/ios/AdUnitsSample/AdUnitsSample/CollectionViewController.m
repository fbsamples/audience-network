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

#import "CollectionViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

static NSInteger const kRowStrideForAdCell = 3;
static NSString *const kDefaultCellIdentifier = @"kDefaultCellIdentifier";

@interface CollectionViewCell : UICollectionViewCell

@property (nonatomic, strong, nullable) UILabel *textLabel;

@end

@implementation CollectionViewCell

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        UILabel *label = [UILabel new];
        label.textColor = [UIColor blackColor];
        label.numberOfLines = 2;
        self.textLabel = label;
        [self.contentView addSubview:label];
    }
    return self;
}

@end

@interface CollectionViewController () <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, FBNativeAdsManagerDelegate, FBNativeAdDelegate>

@property (strong, nonatomic) IBOutlet UICollectionView *collectionView;
@property (strong, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (strong, nonatomic) FBNativeAdsManager *adsManager;
@property (strong, nonatomic) FBNativeAdCollectionViewCellProvider *cellProvider;
@property (strong, nonatomic) NSMutableArray<NSString *> *collectionViewContentArray;
@property (assign, nonatomic) BOOL adCellsCreated;

@end

@implementation CollectionViewController

#pragma mark - Lazy Loading
- (NSMutableArray<NSString *> *)collectionViewContentArray
{
    if (!_collectionViewContentArray) {
        _collectionViewContentArray = [NSMutableArray array];
        for (NSUInteger i = 0; i < 10; i++) {
            [_collectionViewContentArray addObject:[NSString stringWithFormat:@"CollectionView\n Cell #%lu", (unsigned long)(i + 1)]];
        }
    }

    return _collectionViewContentArray;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;

    [self.collectionView registerClass:[CollectionViewCell class] forCellWithReuseIdentifier:kDefaultCellIdentifier];

    [self loadNativeAd];
}

- (IBAction)dismissViewController:(id)sender
{
    [self dismissViewControllerAnimated:YES completion:nil];
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
    FBNativeAdCollectionViewCellProvider *cellProvider = [[FBNativeAdCollectionViewCellProvider alloc] initWithManager:manager forType:FBNativeAdViewTypeGenericHeight300];
    self.cellProvider = cellProvider;
    self.cellProvider.delegate = self;

    [self.collectionView reloadData];
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

#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView
{
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    // In this example the ads are evenly distributed within the table every kRowStrideForAdCell-th cell.
    NSUInteger count = [self.collectionViewContentArray count];
    count = [self.cellProvider adjustCount:count forStride:kRowStrideForAdCell] ?: count;
    return count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if ([self.cellProvider isAdCellAtIndexPath:indexPath forStride:kRowStrideForAdCell]) {
        return [self.cellProvider collectionView:collectionView cellForItemAtIndexPath:indexPath];
    } else {
        indexPath = [self.cellProvider adjustNonAdCellIndexPath:indexPath forStride:kRowStrideForAdCell] ?: indexPath;
        CollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:kDefaultCellIdentifier forIndexPath:indexPath];
        cell.textLabel.text = [self.collectionViewContentArray objectAtIndex:indexPath.row];
        [cell.textLabel sizeToFit];
        cell.textLabel.center = cell.contentView.center;
        return cell;
    }
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    if ([self.cellProvider isAdCellAtIndexPath:indexPath forStride:kRowStrideForAdCell]) {
        return (CGSize){300.0, [self.cellProvider collectionView:collectionView heightForRowAtIndexPath:indexPath]};
    } else {
        return (CGSize){300.0, 100.0};
    }
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section
{
    return 20.0;
}

@end
