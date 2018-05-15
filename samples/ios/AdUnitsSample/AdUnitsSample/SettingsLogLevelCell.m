#import "SettingsLogLevelCell.h"

@interface SettingsLogLevelCell ()

@property (retain, nonatomic) IBOutlet UIStepper *stepper;
@property (retain, nonatomic) IBOutlet UILabel *logLevelLabel;

@end

@implementation SettingsLogLevelCell

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.logLevelLabel.text = [self stringFromLogLevel:[FBAdSettings getLogLevel]];
    self.stepper.value = [FBAdSettings getLogLevel];
}

- (IBAction)logValueDidChange:(UIStepper *)sender
{
    [FBAdSettings setLogLevel:sender.value];
    self.logLevelLabel.text = [self stringFromLogLevel:[FBAdSettings getLogLevel]];

    if (self.onLogLevelChange) {
        self.onLogLevelChange([FBAdSettings getLogLevel]);
    }
}

- (NSString *)stringFromLogLevel:(FBAdLogLevel)logLevel
{
    NSString *logLevelString = nil;
    switch (logLevel) {
        case FBAdLogLevelNone:
            logLevelString = @"None";
            break;
        case FBAdLogLevelNotification:
            logLevelString = @"Notification";
            break;
        case FBAdLogLevelError:
            logLevelString = @"Error";
            break;
        case FBAdLogLevelWarning:
            logLevelString = @"Warning";
            break;
        case FBAdLogLevelLog:
            logLevelString = @"Log";
            break;
        case FBAdLogLevelDebug:
            logLevelString = @"Debug";
            break;
        case FBAdLogLevelVerbose:
            logLevelString = @"Verbose";
            break;
        default:
            logLevelString = @"Unknown";
            break;
    }

    return logLevelString;
}

@end
