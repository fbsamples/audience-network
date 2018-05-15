#import <UIKit/UIKit.h>

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsLogLevelCell : UITableViewCell

@property (nonatomic, copy) void (^onLogLevelChange)(FBAdLogLevel logLevel);

@end
