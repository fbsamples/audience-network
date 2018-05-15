#import "SettingsTableViewController.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

#import "SettingsTestModeCell.h"

@implementation SettingsTableViewController

#pragma mark - Private

- (NSString *)reuseIdentifierForIndexPath:(NSIndexPath *)indexPath
{
    NSArray<NSString *> *reuseIdentifiers = @[@"SettingsSandboxCell",
                                              @"SettingsLogLevelCell",
                                              @"SettingsTestModeCell",
                                              @"SettingsTestAdCell"];

    return reuseIdentifiers[indexPath.row];
}

- (FBInterfaceOrientationMask)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [FBAdSettings isTestMode] ? 4 : 3;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
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
