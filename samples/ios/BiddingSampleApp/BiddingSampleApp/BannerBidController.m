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

#import <AdSupport/ASIdentifierManager.h>
#import <UIKit/UIKit.h>

@implementation NSNumberFormatter (FBAdUtility)

#ifndef FB_INITIALIZE_AND_RETURN_STATIC
#define FB_INITIALIZE_AND_RETURN_STATIC(...) ({ \
static __typeof__(__VA_ARGS__) static_storage__; \
void (^initialization_block__)(void) = ^{ static_storage__ = (__VA_ARGS__); }; \
static dispatch_once_t once_token__; \
dispatch_once(&once_token__, initialization_block__); \
static_storage__; \
})
#endif

#define weakify(arg) \
_Pragma("clang diagnostic push") \
_Pragma("clang diagnostic ignored \"-Wshadow\"") \
__typeof__(arg) __weak mn_weak_##arg = arg \
_Pragma("clang diagnostic pop")

#define strongify(arg) \
_Pragma("clang diagnostic push") \
_Pragma("clang diagnostic ignored \"-Wshadow\"") \
__typeof__(arg) arg = mn_weak_##arg \
_Pragma("clang diagnostic pop")

+ (instancetype)defaultFormatter
{
  return FB_INITIALIZE_AND_RETURN_STATIC([NSNumberFormatter new]);
}

- (nullable NSNumber *)safeNumberFromString:(nullable NSString *)string
{
  if (!string) {
    return nil;
  } else if ([string isKindOfClass:[NSString class]]) {
    return [self numberFromString:(NSString *)string];
  } else if ([string isKindOfClass:[NSNumber class]]) {
    return (NSNumber *)string;
  } else {
    return nil;
  }
}

@end

@implementation NSDictionary (FBAdUtility)

- (id)objectForKey:(id)key ofClass:(nullable Class)aClass orDefault:(id)object
{
  id obj = [self objectForKeyOrNil:key ofClass:aClass];
  return obj ? obj : object;
}


- (NSString *)stringForKey:(id)key orDefault:(NSString *)string
{
  return [self objectForKey:key ofClass:[NSString class] orDefault:string];
}

- (nullable NSString *)stringForKeyOrNil:(id)key
{
  return [self objectForKeyOrNil:key ofClass:[NSString class]];
}

- (NSArray *)arrayForKey:(id)key orDefault:(NSArray *)array
{
  return (id)[self objectForKeyOrNil:key ofClass:[NSArray class]] ?: array;
}

- (nullable id)objectForKeyOrNil:(id)key ofClass:(nullable Class)aClass
{
  id object = self[key];
  
  if (object == nil || object == [NSNull null]) {
    return nil;
  }
  if (aClass) {
    if (![object isKindOfClass:aClass]) {
#ifdef DEBUG
//      [FBAdUtility displayVerboseDebugMessage:@"Object %@ for key %@ did not match class. Expected: %@ Actual: %@.", object, key, aClass, [object class]];
#endif
      // In the case of simple NSString/NSNumber mismatches, try to fix
      id attemptedRecoveryObject = [self attemptRecoveryOfObject:object ofClass:aClass];
      if (attemptedRecoveryObject) {
        return attemptedRecoveryObject;
      }
      return nil;
    }
  }
  
  return object;
}

- (nullable id)attemptRecoveryOfObject:(id<NSObject>)object ofClass:(nullable Class)aClass
{
  if (!object) {
    return nil;
  } else if (aClass == [NSString class] && [object isKindOfClass:[NSNumber class]]) {
    NSNumber *number = (NSNumber *)object;
    NSString *string = number.stringValue;
    return string;
  } else if (aClass == [NSNumber class] && [object isKindOfClass:[NSString class]]) {
    NSString *string = (NSString *)object;
    NSNumber *number = [[NSNumberFormatter defaultFormatter] safeNumberFromString:string];
    return number;
  }
  return nil;
}

+ (nullable NSString *)getJSONStringFromObject:(nullable id)obj
{
  if (obj) {
    @try {
      NSData * __nullable jsonData = [NSJSONSerialization dataWithJSONObject:(id)obj ?: @""
                                                                     options:(NSJSONWritingOptions)kNilOptions
                                                                       error:nil];
      if (jsonData) {
        return [[NSString alloc] initWithData:(NSData *)jsonData encoding:NSUTF8StringEncoding];
      }
    }
    @catch (...) {
      NSLog(@"Attempted to convert invalid object %@ to JSON", obj);
    }
  }
  return nil;
}

+ (nullable id)getObjectFromJSONData:(nullable NSData *)jsonData
{
  if (jsonData) {
    @try {
      id obj = [NSJSONSerialization JSONObjectWithData:jsonData
                                               options:(NSJSONReadingOptions)(NSJSONReadingMutableContainers |
                                                                              NSJSONReadingMutableLeaves |
                                                                              NSJSONReadingAllowFragments)
                                                 error:nil];
      return obj;
    }
    @catch (...) {
      NSLog(@"Attempted to convert invalid JSON %@", [[NSString alloc] initWithData:jsonData
                                                                           encoding:NSUTF8StringEncoding]);
    }
  }
  return nil;
}
@end

@implementation NSArray (FBAdUtility)

- (id)objectAtIndexOrNil:(NSUInteger)index
{
  return (index < self.count) ? self[index] : nil;
}

@end

static NSString *placementID = @"256699801203835_326140227593125";

@interface BannerBidController ()

@property (weak, nonatomic) IBOutlet UILabel *adStatusLabel;
@property (nonatomic, strong) FBAdView *adView;
@property (nonatomic, strong) NSNumber *anBiddingPrice;
@property (nonatomic, strong) NSString *anBiddingPayload;
@property (nonatomic, strong) NSNumber *dummyBiddingPrice;

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
  weakify(self);
  [self requestBidWithAppID:@"256699801203835" withPlacementID:placementID
                  onSuccess:^(NSString * __nullable payload, NSNumber * __nullable price) {
    strongify(self);
    self.anBiddingPrice = [price copy];
    self.anBiddingPayload = [payload copy];
    [self complete];
  }];

  [self requestDummyBidOnSuccess:^(NSNumber * __nullable price) {
    strongify(self);
    self.dummyBiddingPrice = [price copy];
    [self complete];
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


//256699801203835_326140227593125
- (void)requestBidWithAppID:(NSString *)appID
         withPlacementID:(NSString *)placementID
               onSuccess:(void (^)(NSString *payload, NSNumber * __nullable price))onSuccess
{
  
    NSString *jsonReq = [NSDictionary getJSONStringFromObject:[self ortbRequestParameters:appID withPlacementID:placementID withUserAgent:@"Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Mobile/14E269 [FBAN/AudienceNetworkForiOS;FBDV/x86_64;FBMD/MacBookPro13,3;FBSN/iOS;FBSV/10.3;FBLC/en_US;FBVS/4.26.0;FBAB/com.facebook.audiencenetwork.FBAudienceNetworkTestHost.localDevelopment;FBAV/1.0;FBBV/1]"]];
    NSData *postData = [jsonReq dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%zu", (size_t) [postData length]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:@"https://an.facebook.com/placementbid.ortb"]];
    [request setHTTPMethod:@"POST"];
    [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:postData];
    
    [NSURLConnection sendAsynchronousRequest:request
                                       queue:[NSOperationQueue currentQueue]
                           completionHandler:^(NSURLResponse *urlResponse, NSData *data, NSError *error) {
                             NSDictionary *responseDict = [NSDictionary getObjectFromJSONData:data];
                             NSDictionary *bids = responseDict == nil ? nil : [[responseDict arrayForKey:@"seatbid" orDefault:[NSArray array]] objectAtIndexOrNil:0];
                             NSDictionary *bid = bids == nil ? nil : [[bids arrayForKey:@"bid" orDefault:[NSArray array]] objectAtIndexOrNil:0];
                             NSString *payload = bid == nil ? nil : [bid stringForKey:@"adm" orDefault:@""];
                             
                             if (error) {
                               NSString *errorName = [NSString stringWithFormat:@"Unexpected OpenRTB error: %@", error.localizedDescription];
                               NSLog(@"%@", errorName);
                               return;
                             }
                             
                             NSInteger statusCode = [(NSHTTPURLResponse *)urlResponse statusCode];
                             if (statusCode != 200) {
                               NSString *errorName = [NSString stringWithFormat:@"Unexpected OpenRTB response status code: %ld", (long)statusCode];
                               NSLog(@"%@", errorName);
                               return;
                             }
                             
                             if (!payload) {
                               NSLog(@"Unexpected empty OpenRTB response");
                               return;
                             }
                             onSuccess(payload, bid[@"price"]);
                           }];

}

- (void)requestDummyBidOnSuccess:(void (^)(NSNumber * __nullable price))onSuccess
{
  
  NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
  [request setURL:[NSURL URLWithString:@"https://stark-island-43990.herokuapp.com/buy"]];
  [request setHTTPMethod:@"GET"];
  [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
  
  [NSURLConnection sendAsynchronousRequest:request
                                     queue:[NSOperationQueue currentQueue]
                         completionHandler:^(NSURLResponse *urlResponse, NSData *data, NSError *error) {
                           
                           if (error) {
                             NSString *errorName = [NSString stringWithFormat:@"error: %@", error.localizedDescription];
                             NSLog(@"%@", errorName);
                             return;
                           }
                           
                           NSInteger statusCode = [(NSHTTPURLResponse *)urlResponse statusCode];
                           if (statusCode != 200) {
                             NSString *errorName = [NSString stringWithFormat:@"status code: %ld", (long)statusCode];
                             NSLog(@"%@", errorName);
                             return;
                           }
                           
                           NSString *priceString = [NSString stringWithUTF8String:[data bytes]];
                           NSNumber *price = [NSNumber numberWithDouble:priceString.doubleValue];
                           onSuccess(price);
                         }];
  
}

//@"256699801203835"
- (NSDictionary *)ortbRequestParameters:(NSString *)appID
                        withPlacementID:(NSString *)placementID
                          withUserAgent:(NSString *)userAgent
{

  
  // construct the app object
  NSMutableDictionary *appObject = [NSMutableDictionary dictionary];
  
  NSBundle *bundle = [NSBundle mainBundle];
  NSDictionary *bundleDictionary = [bundle infoDictionary];
  appObject[@"bundle"] = [bundle bundleIdentifier];
  appObject[@"ver"] = [bundleDictionary stringForKeyOrNil:@"CFBundleShortVersionString"];
  appObject[@"publisher"] = [NSDictionary dictionaryWithObject:appID forKey:@"id"];
  
  // construct the device object
  NSMutableDictionary *deviceObject = [NSMutableDictionary dictionary];
  deviceObject[@"ifa"] = [[ASIdentifierManager sharedManager] advertisingIdentifier].UUIDString;
  //deviceObject[@"dnt"] = [[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled] ? @"1" : @"0";
  deviceObject[@"dnt"] = @"0";
  deviceObject[@"ip"] = @"127.0.0.1";
  deviceObject[@"ua"] = userAgent;
  deviceObject[@"make"] = @"Apple";
  deviceObject[@"model"] = @"arm64";
  deviceObject[@"os"] = @"iOS";
  deviceObject[@"osv"] = [UIDevice currentDevice].systemVersion;
  
  CGSize size = [UIScreen mainScreen].bounds.size;
  deviceObject[@"w"] = @(size.height);
  deviceObject[@"h"] = @(size.width);
  
  NSMutableDictionary *regsObject = [NSMutableDictionary dictionary];
  regsObject[@"coppa"] = @((int)false);
  
  // construct the banner object
  NSMutableDictionary *bannerObject = [NSMutableDictionary dictionary];
  bannerObject[@"w"] = @((int) kFBAdSizeHeight50Banner.size.width);
  bannerObject[@"h"] = @((int) kFBAdSizeHeight50Banner.size.height);
  
  // construct the imp object
  NSMutableDictionary *impObject = [NSMutableDictionary dictionary];
  impObject[@"id"] = @"banner_test_bid_req_imp_id";
  impObject[@"tagid"] = placementID;
  impObject[@"banner"] = bannerObject;
  
  // construct the root bid request object
  NSMutableDictionary *bidRequestObject = [NSMutableDictionary dictionary];
  bidRequestObject[@"at"] = @1;
  bidRequestObject[@"tmax"] = @500;
  bidRequestObject[@"test"] = @1;
  bidRequestObject[@"id"] = @"banner_test_bid_req_id";
  bidRequestObject[@"app"] = appObject;
  bidRequestObject[@"device"] = deviceObject;
  bidRequestObject[@"regs"] = regsObject;
  bidRequestObject[@"imp"] = [NSArray arrayWithObject:impObject];
  bidRequestObject[@"user"] = [NSMutableDictionary dictionaryWithObject:[FBAdSettings bidderToken] forKey:@"buyeruid"];
  bidRequestObject[@"ext"] = [NSMutableDictionary dictionaryWithObject:appID forKey:@"platformid"];
  
  return bidRequestObject;
}

- (IBAction)bidAdTapped:(id)sender {
    [self loadAd];
}

#pragma mark - FBAdViewDelegate implementation

// Implement this function if you want to change the viewController after the FBAdView
// is created. The viewController will be used to present the modal view (such as the
// in-app browser that can appear when an ad is clicked).
// - (UIViewController *)viewControllerForPresentingModalView
// {
//   return self;
// }

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


