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
#import "BannerORTBImpression.h"
#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
@implementation BannerORTBImpression

- (instancetype)initWith:(NSString *)impressionID
                   tagID:(NSString *)tagID
                   width:(int)width
                  height:(int)height {
    self = [[BannerORTBImpression alloc] init];
    if (self) {
        _impressionID = impressionID;
        _tagID = tagID;
        _width = width;
        _height = height;
    }
    return self;
}

- (NSDictionary *)impressionParameters {
    // construct the imp objects which are array of slots to bid on
    NSMutableDictionary *impObject = [NSMutableDictionary dictionary];
    // construct the banner object
    NSMutableDictionary *bannerObject = [NSMutableDictionary dictionary];
    // width
    bannerObject[@"w"] = @(self.width);
    // height
    bannerObject[@"h"] = @(self.height);
    impObject[@"banner"] = bannerObject;
    // platform's identifier for this impression within the request
    impObject[@"id"] = self.impressionID;
    // Placement ID or Placement Name
    impObject[@"tagid"] = self.tagID;
    
    return impObject;
}

@end
NS_ASSUME_NONNULL_END
