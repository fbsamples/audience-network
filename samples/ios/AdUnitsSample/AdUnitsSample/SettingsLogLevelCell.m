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

#import "SettingsLogLevelCell.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsLogLevelCell ()

@property (nonatomic, strong) IBOutlet UIStepper *stepper;
@property (nonatomic, strong) IBOutlet UILabel *logLevelLabel;
@property (nonatomic, copy) void (^onLogLevelChange)(FBAdLogLevel logLevel);

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
