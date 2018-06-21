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

#import "SettingsTestAdCell.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsTestAdCell () <UIPickerViewDelegate, UIPickerViewDataSource>

@property (nonatomic, strong) IBOutlet UITextField *testAdTextField;
@property (nonatomic, strong) UIPickerView *pickerView;
@property (nonatomic, copy) NSDictionary<NSNumber *, NSString *> *testsAds;

@end

@implementation SettingsTestAdCell

- (void)awakeFromNib
{
    [super awakeFromNib];

    self.pickerView.delegate = self;
    self.testsAds = @{
                      @(FBAdTestAdType_Default) : @"default",
                      @(FBAdTestAdType_Img_16_9_App_Install) : @"image_install",
                      @(FBAdTestAdType_Img_16_9_Link) : @"image_link",
                      @(FBAdTestAdType_Vid_HD_16_9_46s_App_Install) : @"video_16x9_46s_install",
                      @(FBAdTestAdType_Vid_HD_16_9_46s_Link) : @"video_16x9_46s_link",
                      @(FBAdTestAdType_Vid_HD_16_9_15s_App_Install) : @"video_16x9_15s_install",
                      @(FBAdTestAdType_Vid_HD_16_9_15s_Link) : @"video_16x9_15s_link",
                      @(FBAdTestAdType_Vid_HD_9_16_39s_App_Install) : @"video_9x16_39s_install",
                      @(FBAdTestAdType_Vid_HD_9_16_39s_Link) : @"video_9x16_39s_link",
                      @(FBAdTestAdType_Carousel_Img_Square_App_Install) : @"carousel_install",
                      @(FBAdTestAdType_Carousel_Img_Square_Link) : @"carousel_link"
                      };

    [self setupPickerView];
    [self setupAdTextField];
}

- (void)setupPickerView
{
    self.pickerView = [UIPickerView new];
    self.pickerView.delegate = self;
    self.pickerView.dataSource = self;

    [self.pickerView selectRow:FBAdSettings.testAdType inComponent:0 animated:NO];
}

- (UIToolbar *)toolbar
{
    UIToolbar* toolbar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, CGRectGetWidth([UIScreen mainScreen].bounds), 44)];
    UIBarButtonItem *flex = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace
                                                                          target:nil action:nil];
    UIBarButtonItem *done = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemDone target:self action:@selector(done:)];
    toolbar.items = @[flex, done];

    [toolbar sizeToFit];
    return toolbar;
}

- (void)setupAdTextField
{
    self.testAdTextField.inputView = self.pickerView;
    self.testAdTextField.inputAccessoryView = [self toolbar];
    self.testAdTextField.text = self.testsAds[@(FBAdSettings.testAdType)];
}

- (void)done:(id)sender
{
    [self.testAdTextField resignFirstResponder];
}

#pragma mark - Picker view delegate

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    NSString *title = self.testsAds[@(row)];
    if (!title) {
        title = @"None";
    }
    return title;
}

- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)thePickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)thePickerView numberOfRowsInComponent:(NSInteger)component
{
    return self.testsAds.count;
}

- (void)pickerView:(UIPickerView *)thePickerView
      didSelectRow:(NSInteger)row
       inComponent:(NSInteger)component
{
    self.testAdTextField.text = self.testsAds[@(row)] ? self.testsAds[@(row)] : @"None";
    FBAdSettings.testAdType = row;
}

@end
