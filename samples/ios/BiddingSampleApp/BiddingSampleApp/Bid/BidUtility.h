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

#import <UIKit/UIKit.h>
NS_ASSUME_NONNULL_BEGIN
@interface NSNumberFormatter (BidUtility)
+ (instancetype)defaultFormatter;
- (nullable NSNumber *)safeNumberFromString:(nullable NSString *)string;
@end

@interface NSDictionary (BidUtility)
- (id)objectForKey:(id)key ofClass:(nullable Class)aClass orDefault:(id)object;
- (NSString *)stringForKey:(id)key orDefault:(NSString *)string;
- (nullable NSString *)stringForKeyOrNil:(id)key;
- (NSArray *)arrayForKey:(id)key orDefault:(NSArray *)array;
- (nullable id)objectForKeyOrNil:(id)key ofClass:(nullable Class)aClass;
- (nullable id)attemptRecoveryOfObject:(id<NSObject>)object ofClass:(nullable Class)aClass;
+ (nullable NSString *)getJSONStringFromObject:(nullable id)obj;
+ (nullable id)getObjectFromJSONData:(nullable NSData *)jsonData;
@end

@interface NSArray (BidUtility)
- (id)objectAtIndexOrNil:(NSUInteger)index;
@end
NS_ASSUME_NONNULL_END
