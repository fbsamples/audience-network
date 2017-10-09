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

#import "BidUtility.h"
#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
@implementation NSNumberFormatter (BidUtility)

+ (instancetype)defaultFormatter
{
    return [NSNumberFormatter new];
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

@implementation NSDictionary (BidUtility)

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

@implementation NSArray (BidUtility)

- (id)objectAtIndexOrNil:(NSUInteger)index
{
    return (index < self.count) ? self[index] : nil;
}
@end
NS_ASSUME_NONNULL_END
