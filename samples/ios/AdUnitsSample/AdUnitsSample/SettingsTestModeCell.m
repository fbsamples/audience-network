#import "SettingsTestModeCell.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsTestModeCell ()

@property (retain, nonatomic) IBOutlet UISwitch *switcher;

@end

@implementation SettingsTestModeCell

- (void)awakeFromNib {
    [super awakeFromNib];

    [self.switcher setOn:[FBAdSettings isTestMode]];
}

- (IBAction)switcherValueChanged:(UISwitch *)sender
{
    if ([sender isOn]) {
        [FBAdSettings addTestDevice:[FBAdSettings testDeviceHash]];
    } else {
        [FBAdSettings clearTestDevice:[FBAdSettings testDeviceHash]];
    }

    if (self.onTestModeChange) {
        self.onTestModeChange([FBAdSettings isTestMode]);
    }
}


@end
