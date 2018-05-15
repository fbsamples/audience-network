#import "NavigationController.h"

#import "ViewController.h"

@interface NavigationController ()

@end

@implementation NavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations
{
    UIInterfaceOrientationMask mask = UIInterfaceOrientationMaskAll;
    NSArray<UIViewController *> *viewControllers = [self viewControllers];

    for (UIViewController *viewController in viewControllers) {
        if ([viewController isKindOfClass:[ViewController class]]) {
            mask = UIInterfaceOrientationMaskPortrait;
            break;
        }
    }

    return mask;
}

@end
