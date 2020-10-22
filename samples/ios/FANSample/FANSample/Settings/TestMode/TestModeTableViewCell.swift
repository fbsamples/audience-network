// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class TestModeTableViewCell: UITableViewCell {

    @IBOutlet private var testModeSwitch: UISwitch!

    override func awakeFromNib() {
        super.awakeFromNib()
        testModeSwitch.isOn = FBAdSettings.isTestMode()
    }

    @IBAction private func testModeSwitchValueChanged(sender: UISwitch) {
        let testDeviceHash = FBAdSettings.testDeviceHash()
        if sender.isOn {
            FBAdSettings.addTestDevice(testDeviceHash)
        } else {
            FBAdSettings.clearTestDevice(testDeviceHash)
        }
    }
}
