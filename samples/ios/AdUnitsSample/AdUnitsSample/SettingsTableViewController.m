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

#import "SettingsTableViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

#import "SettingsTestModeCell.h"

@interface SettingsTableViewController ()
@property (nonatomic, strong, readonly) NSArray<NSString *> *reuseIdentifiers;
@end

@implementation SettingsTableViewController
@synthesize reuseIdentifiers = _reuseIdentifiers;

#pragma mark - Private

- (NSArray<NSString *> *)reuseIdentifiers
{
    if (nil == _reuseIdentifiers) {
        NSMutableArray<NSString *> *identifiers = [@[@"SettingsSandboxCell",
                                                     @"SettingsLogLevelCell",
                                                     @"SettingsTestModeCell",
                                                     @"SettingsTestAdCell"] mutableCopy];

        if ([self isDebugLogsViewControllerAvailable]) {
            [identifiers insertObject:@"SettingsDebugEventCell" atIndex:2];
        }

        _reuseIdentifiers = [identifiers copy];
    }
    return _reuseIdentifiers;
}

- (NSString *)reuseIdentifierForIndexPath:(NSIndexPath *)indexPath
{
    return self.reuseIdentifiers[indexPath.row];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

- (BOOL)isDebugLogsViewControllerAvailable
{
    return NSClassFromString(@"DebugLogsViewController") != nil;
}

#pragma mark - UITableViewDataSource implementation

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSInteger numberOfRows = [FBAdSettings isTestMode] ? 4 : 3;
    if ([self isDebugLogsViewControllerAvailable]) {
        numberOfRows++;
    }
    return numberOfRows;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *reuseId = [self reuseIdentifierForIndexPath:indexPath];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:reuseId
                                                            forIndexPath:indexPath];
    if ([reuseId isEqualToString:@"SettingsTestModeCell"]) {
        ((SettingsTestModeCell *)cell).onTestModeChange = ^(BOOL enabled) {
            [self.tableView reloadData];
        };
    }

    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [NSString stringWithFormat:@"SDK Version %@", FB_AD_SDK_VERSION];
}

@end
