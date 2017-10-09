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

- (NSDictionary *)ortbRequestParameters {
    // construct the app object
    NSMutableDictionary *appObject = [NSMutableDictionary dictionary];
    
    NSBundle *bundle = [NSBundle mainBundle];
    NSDictionary *bundleDictionary = [bundle infoDictionary];
    appObject[@"bundle"] = [bundle bundleIdentifier];
    appObject[@"ver"] = [bundleDictionary stringForKeyOrNil:@"CFBundleShortVersionString"];
    appObject[@"publisher"] = [NSDictionary dictionaryWithObject:self.publisherID forKey:@"id"];
    
    // construct the device object
    NSMutableDictionary *deviceObject = [NSMutableDictionary dictionary];
    deviceObject[@"ifa"] = [[ASIdentifierManager sharedManager] advertisingIdentifier].UUIDString;
    deviceObject[@"dnt"] = [[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled] ? @"1" : @"0";
    deviceObject[@"ip"] = @"127.0.0.1";
    deviceObject[@"ua"] = @"";
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
    impObject[@"tagid"] = self.tagID;
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
    bidRequestObject[@"ext"] = [NSMutableDictionary dictionaryWithObject:self.platformID forKey:@"platformid"];
    
    return bidRequestObject;
}

@end
