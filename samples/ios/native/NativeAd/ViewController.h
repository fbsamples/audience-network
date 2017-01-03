//
//  ViewController.h
//  NativeAd
//
//  Created by Yun Peng Wang on 11/7/16.
//  Copyright © 2016 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>

@import FBAudienceNetwork;

@interface ViewController : UIViewController  <FBNativeAdDelegate>

@property (strong, nonatomic) IBOutlet UILabel *adStatusLabel;

@property (strong, nonatomic) IBOutlet UIImageView *adIconImageView;
@property (weak, nonatomic) IBOutlet FBMediaView *adCoverMediaView;
@property (strong, nonatomic) IBOutlet UILabel *adTitleLabel;
@property (strong, nonatomic) IBOutlet UILabel *adBodyLabel;
@property (strong, nonatomic) IBOutlet UIButton *adCallToActionButton;
@property (strong, nonatomic) IBOutlet UILabel *adSocialContextLabel;
@property (strong, nonatomic) IBOutlet UILabel *sponsoredLabel;
@property (weak, nonatomic) IBOutlet FBAdChoicesView *adChoicesView;

@property (strong, nonatomic) IBOutlet UIView *adUIView;


@end

