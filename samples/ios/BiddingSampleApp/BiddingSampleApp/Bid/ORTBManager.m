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

#import "ORTBManager.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>
#import <AdSupport/ASIdentifierManager.h>
#import <UIKit/UIKit.h>

#import "ORTBSource.h"
#import "BidUtility.h"

@interface ORTBManager ()

@end

@implementation ORTBManager
+ (ORTBManager*)sharedManager
{
    static ORTBManager *manager = nil;
    static dispatch_once_t onceSharedToken;
    dispatch_once(&onceSharedToken, ^{
        manager = [[ORTBManager alloc] init];
    });
    return manager;
}

- (void)requestBid:(id<ORTBSource>)ortbSource
        impression:(id<ORTBImpression>)ortbImpression
         onSuccess:(void (^)(NSString *payload, NSNumber * __nullable price))onSuccess
{
    NSDictionary *jsonDict = [ortbSource ortbRequestParametersForAdImpression:ortbImpression];
    NSString *jsonReq = [NSDictionary getJSONStringFromObject:jsonDict];
    NSData *postData = [jsonReq dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
    NSString *postLength = [NSString stringWithFormat:@"%zu", (size_t) [postData length]];
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    [request setURL:[NSURL URLWithString:[ortbSource endPoint]]];
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

@end


