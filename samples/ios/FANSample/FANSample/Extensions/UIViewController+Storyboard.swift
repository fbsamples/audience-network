// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

extension UIViewController {
    static func create<ViewControllerType: UIViewController>() -> ViewControllerType {
        UIStoryboard(
            name: "Main",
            bundle: nil
        )
        .instantiateViewController(
            identifier: Self.className
        ) as! ViewControllerType
    }
}
