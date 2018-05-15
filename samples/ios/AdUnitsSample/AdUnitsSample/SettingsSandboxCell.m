#import "SettingsSandboxCell.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsSandboxCell () <UITextFieldDelegate>

@property (strong, nonatomic) IBOutlet UITextField *sandboxTextField;

@end

@implementation SettingsSandboxCell

- (void)awakeFromNib
{
    [super awakeFromNib];

    self.sandboxTextField.text = [FBAdSettings urlPrefix];
    self.sandboxTextField.delegate = self;
}

#pragma mark - Text field delegate

- (void)textFieldDidEndEditing:(UITextField *)textField
{
    [FBAdSettings setUrlPrefix:textField.text];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

@end
