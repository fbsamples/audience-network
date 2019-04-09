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

#import "AppDelegate.h"

#import <FBAudienceNetwork/FBAudienceNetworkAds.h>

@implementation AppDelegate

void installUncaughtExceptionHandler(void);

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary<NSString *, id> *)launchOptions
{
    // Debug exception handler
    installUncaughtExceptionHandler();

    [FBAudienceNetworkAds initializeWithSettings:nil completionHandler:nil];

    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    self.viewController = [[UIStoryboard storyboardWithName:@"AdUnitsSampleStoryboard" bundle:nil] instantiateInitialViewController];
    self.window.rootViewController = self.viewController;
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

#pragma mark Exception Handling (for debugging purposes)

NSString * const FBAdSignalExceptionName = @"handlerExceptionName";
NSString * const FBAdStackTraceKey = @"handlerStackTraceKey";
NSString * const FBAdSignalKey = @"handlerSignalKey";

void FBAdExceptionHandler(NSException *exception);
void FBAdSignalHandler(int signal);
static BOOL _displayExceptionMessage = YES;

void installUncaughtExceptionHandler()
{
    NSSetUncaughtExceptionHandler(&FBAdExceptionHandler);
    signal(SIGABRT, FBAdSignalHandler);
    signal(SIGILL, FBAdSignalHandler);
    signal(SIGSEGV, FBAdSignalHandler);
    signal(SIGFPE, FBAdSignalHandler);
    signal(SIGBUS, FBAdSignalHandler);
    signal(SIGPIPE, FBAdSignalHandler);
}

void FBAdExceptionHandler(NSException *exception)
{
    if (!exception) {
        NSLog(@"Exception handler called with nil exception.");
        __builtin_trap();
    }
    NSArray<NSString *> *callStack = [exception callStackSymbols];
    NSLog(@"Stack trace: %@", callStack);
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSMutableDictionary<NSString *, id> *userInfo = [@{FBAdStackTraceKey: callStack} mutableCopy];
    [userInfo addEntriesFromDictionary:exception.userInfo];
    NSException *newException = [NSException exceptionWithName:exception.name reason:exception.reason userInfo:userInfo];
    [delegate performSelectorOnMainThread:@selector(handleException:) withObject:newException waitUntilDone:YES];
}

void FBAdSignalHandler(int signal)
{
    NSMutableDictionary<NSString*, id> *userInfo = [@{FBAdSignalKey: @(signal)} mutableCopy];
    NSMutableArray<NSString *> *callStack = [[NSThread callStackSymbols] mutableCopy];
    if (callStack) {
        NSIndexSet *indexSet = [NSIndexSet indexSetWithIndexesInRange:NSMakeRange(0, 2)];
        if ([callStack objectsAtIndexes:indexSet]) {
            [callStack removeObjectsAtIndexes:indexSet];
        }
        userInfo[FBAdStackTraceKey] = callStack;
        NSLog(@"Stack trace: %@", callStack);
    }
    NSString *reason = [NSString stringWithFormat:@"Signal %d was raised.", signal];
    NSException *exception = [NSException exceptionWithName:FBAdSignalExceptionName reason:reason userInfo:userInfo];
    AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    [delegate performSelectorOnMainThread:@selector(handleException:) withObject:exception waitUntilDone:YES];
}

- (void)handleException:(NSException *)exception
{
    BOOL displayExceptionMessage = _displayExceptionMessage;
    _displayExceptionMessage = NO;
    if (displayExceptionMessage) {
        NSString *message = [NSString stringWithFormat:@"The app has crashed due to an unhandled exception.\n\n"
                             @"Stack trace:\n%@\n%@", [exception reason], [[exception userInfo] objectForKey:FBAdStackTraceKey]];

        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Unhandled Exception"
                                                                                 message:message
                                                                          preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleCancel handler:nil];
        [alertController addAction:cancel];
        [self.viewController presentViewController:alertController animated:YES completion:nil];
    }

    CFRunLoopRef runLoop = CFRunLoopGetCurrent();
    CFArrayRef allModesCF = CFRunLoopCopyAllModes(runLoop);
    NSArray<NSString *> *allModes = CFBridgingRelease(allModesCF);

    while (true) {
        for (NSString *mode in allModes) {
            CFRunLoopRunInMode((CFStringRef)mode, 0.01, false);
        }
    }
}

@end
