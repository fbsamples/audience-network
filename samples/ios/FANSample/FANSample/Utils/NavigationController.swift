// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class NavigationController: UINavigationController {
    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        topViewController?.supportedInterfaceOrientations ?? .all
    }

    override var shouldAutorotate: Bool {
        topViewController?.shouldAutorotate ?? false
    }
}
