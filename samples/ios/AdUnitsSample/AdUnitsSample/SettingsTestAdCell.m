#import "SettingsTestAdCell.h"

#import <FBAudienceNetwork/FBAudienceNetwork.h>

@interface SettingsTestAdCell () <UIPickerViewDelegate, UIPickerViewDataSource>

@property (nonatomic, strong) UIPickerView *pickerView;
@property (nonatomic, copy) NSDictionary<NSNumber *, NSString *> *testsAds;
@property (retain, nonatomic) IBOutlet UITextField *testAdTextField;


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
