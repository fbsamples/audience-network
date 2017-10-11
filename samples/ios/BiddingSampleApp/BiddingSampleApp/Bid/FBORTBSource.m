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
#import "FBORTBSource.h"

#import <Foundation/Foundation.h>
#import <FBAudienceNetwork/FBAudienceNetwork.h>
#import <AdSupport/ASIdentifierManager.h>

#import "BidUtility.h"

NS_ASSUME_NONNULL_BEGIN
  
@implementation FBORTBSource

- (instancetype)initWith:(NSString *)platformID
             publisherID:(NSString *)publisherID
                   tagID:(NSString *)tagID {
    self = [[FBORTBSource alloc] init];
    if (self) {
        _platformID = platformID;
        _publisherID = publisherID;
        _tagID = tagID;
    }
    return self;
}

- (NSString *)endPoint {
    return @"https://an.facebook.com/placementbid.ortb";
}

- (NSDictionary *)ortbRequestParametersForAdImpression:(id<ORTBImpression>)impression
{
    // construct the root bid request object
    NSMutableDictionary *bidRequestObject = [NSMutableDictionary dictionary];
    // construct the imp objects which are array of slots to bid on
    bidRequestObject[@"imp"] = [NSArray arrayWithObject:[impression impressionParameters]];
  
    // construct the app object
    NSMutableDictionary *appObject = [NSMutableDictionary dictionary];
    NSBundle *bundle = [NSBundle mainBundle];
    NSDictionary *bundleDictionary = [bundle infoDictionary];
    // app's appstore bundle name
    appObject[@"bundle"] = [bundle bundleIdentifier];
    // app version
    appObject[@"ver"] = [bundleDictionary stringForKeyOrNil:@"CFBundleShortVersionString"];
    // publisher App ID
    appObject[@"publisher"] = [NSDictionary dictionaryWithObject:self.publisherID forKey:@"id"];
    bidRequestObject[@"app"] = appObject;
    
    // construct the device object
    NSMutableDictionary *deviceObject = [NSMutableDictionary dictionary];
    // device ID sanctioned for advertiser use (IDFA on iOS), required
    deviceObject[@"ifa"] = [[ASIdentifierManager sharedManager] advertisingIdentifier].UUIDString;
    // 0: normal, 1: do-not-track
    deviceObject[@"dnt"] = [[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled] ? @"1" : @"0";
    // device IPv4 (optional, server-side bidding only) and ip should be empty for sending bidding request from client
    deviceObject[@"ip"] = @"";
    // device user-agent (optional)
    deviceObject[@"ua"] = @"";
    // device make
    deviceObject[@"make"] = @"Apple";
    // device model
    deviceObject[@"model"] = @"arm64";
    // device OS
    deviceObject[@"os"] = @"iOS";
    // device OS version
    deviceObject[@"osv"] = [UIDevice currentDevice].systemVersion;
    CGSize size = [UIScreen mainScreen].bounds.size;
    // device screen width in pixels
    deviceObject[@"w"] = @(size.height);
    // device screen height in pixels
    deviceObject[@"h"] = @(size.width);
    bidRequestObject[@"device"] = deviceObject;
    
    // optional regulations object
    NSMutableDictionary *regsObject = [NSMutableDictionary dictionary];
    // US FTC regulations for Children's Online Privacy Protection Act: 1=child-directed, 0=normal (default)
    regsObject[@"coppa"] = @((int)false);
    bidRequestObject[@"regs"] = regsObject;
  
    // auction type: 1: First Price, 2: Second Price
    bidRequestObject[@"at"] = @1;
    // auction timeout, ms
    bidRequestObject[@"tmax"] = @500;
    // 0: normal, 1: test mode (we bid $99.99, but don't pay out)  (optional)
    bidRequestObject[@"test"] = @1;
    // platform's request identifier
    bidRequestObject[@"id"] = @"banner_test_bid_req_id";
    // Audience Network Identity Token
    bidRequestObject[@"user"] = [NSMutableDictionary dictionaryWithObject:[FBAdSettings bidderToken] forKey:@"buyeruid"];
    // mediation partner Platform ID
    bidRequestObject[@"ext"] = [NSMutableDictionary dictionaryWithObject:self.platformID forKey:@"platformid"];
    
    return bidRequestObject;
}

@end

NS_ASSUME_NONNULL_END
