#import <UIKit/UIKit.h>

@interface SettingsTestModeCell : UITableViewCell

@property (nonatomic, copy) void (^onTestModeChange)(BOOL enabled);

@end
