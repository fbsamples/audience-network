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

#import "DebugLogsViewController.h"

static NSString *const kDebugLogsCellIdentifier = @"kDebugLogsCellIdentifier";

@interface DebugLogsViewController ()

@property (nonatomic, strong, readonly) NSArray<NSString *> *tableViewCellLabels;

@end

@implementation DebugLogsViewController
@synthesize tableViewCellLabels = _tableViewCellLabels;

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (NSArray<NSString *> *)tableViewCellLabels
{
    if (nil == _tableViewCellLabels) {
        _tableViewCellLabels = @[@"Generic Error", @"Database Error", @"Exception App Crash", @"Signal App Crash", @"Exception SDK Crash"];
    }
    return _tableViewCellLabels;
}

- (void)generateGenericError
{
    Class class = NSClassFromString(@"FBAdDebugLogging");
    SEL selector = NSSelectorFromString(@"logDebugEventWithType:code:description:");
    NSMethodSignature *signature = [class methodSignatureForSelector:selector];

    NSUInteger errorCode = 0;
    NSString *eventType = @"generic";
    NSString *errorDescription = @"Generic error test message";

    NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:signature];
    [invocation setTarget:class];
    [invocation setSelector:selector];
    [invocation setArgument:&eventType atIndex:2];
    [invocation setArgument:&errorCode atIndex:3];
    [invocation setArgument:&errorDescription atIndex:4];
    [invocation retainArguments];

    [invocation invoke];
}

- (void)generateDatabaseError
{
    Class class = NSClassFromString(@"FBAdDebugLogging");
    SEL selector = NSSelectorFromString(@"logDebugEventWithType:code:description:");
    NSMethodSignature *signature = [class methodSignatureForSelector:selector];

    NSUInteger errorCode = 7;
    NSString *eventType = @"database";
    NSString *errorDescription = @"Database error cannot open database test message";

    NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:signature];
    [invocation setTarget:class];
    [invocation setSelector:selector];
    [invocation setArgument:&eventType atIndex:2];
    [invocation setArgument:&errorCode atIndex:3];
    [invocation setArgument:&errorDescription atIndex:4];
    [invocation retainArguments];

    [invocation invoke];
}

- (void)generateExceptionCrash
{
    [[self class] staticGenerateExceptionCrash];
}

+ (void)staticGenerateExceptionCrash
{
    NSString *string = nil;
    NSArray *array = @[string];
    array = nil;
}

- (void)generateSignalCrash
{
    __unsafe_unretained NSNumber *number = @4.4;
    [number description];
}

- (void)generateSDKCrash
{
    Class class = NSClassFromString(@"FBAdReportingItem");

    id reportingItem = [class alloc];

    SEL selector = NSSelectorFromString(@"initWithIdentifier:title:heading:subItems:");
    NSMethodSignature *signature = [reportingItem methodSignatureForSelector:selector];

    NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:signature];
    [invocation setTarget:reportingItem];
    [invocation setSelector:selector];
    [invocation setArgument:@"" atIndex:2];
    [invocation setArgument:@"" atIndex:3];
    [invocation setArgument:@"" atIndex:4];
    [invocation retainArguments];

    [invocation invoke];

    selector = NSSelectorFromString(@"addSubItem:");
    signature = [reportingItem methodSignatureForSelector:selector];

    invocation = [NSInvocation invocationWithMethodSignature:signature];
    [invocation setTarget:reportingItem];
    [invocation setSelector:selector];
    [invocation retainArguments];

    [invocation invoke];
}

#pragma mark - UITableViewDataSource implementation

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [@"Select one of the options below" uppercaseString];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.tableViewCellLabels.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:kDebugLogsCellIdentifier forIndexPath:indexPath];
    cell.textLabel.text = self.tableViewCellLabels[indexPath.row];

    return cell;
}

#pragma mark - UITableViewDelegate implementation

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];

    switch (indexPath.row) {
        case 0:
            [self generateGenericError];
            break;

        case 1:
            [self generateDatabaseError];
            break;

        case 2:
            [self generateExceptionCrash];
            break;

        case 3:
            [self generateSignalCrash];
            break;

        case 4:
            [self generateSDKCrash];
            break;
    }
}

@end
