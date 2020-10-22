// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class BottomBarView: UIView {

    // MARK: - Types

    typealias TapHandler = () -> Void

    // MARK: - Properties

    var tapHandler: TapHandler!

    @IBOutlet private var button: UIButton!
    @IBOutlet private var testModeSwitch: UISwitch!

    // MARK: - Setup

    override init(frame: CGRect) {
        super.init(frame: frame)
        setup()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setup()
    }

    private func setup() {
        fromNib()
        testModeSwitch.isOn = FBAdSettings.isTestMode()
    }

    // MARK: - Actions

    @IBAction private func buttonTapped() {
        tapHandler()
    }

    @IBAction private func testModeSwitchValueChanged(sender: UISwitch) {
        let testDeviceHash = FBAdSettings.testDeviceHash()
        if sender.isOn {
            FBAdSettings.addTestDevice(testDeviceHash)
        } else {
            FBAdSettings.clearTestDevice(testDeviceHash)
        }
    }

    func set(buttonTitle: String, isEnabled: Bool) {
        button.setTitle(buttonTitle, for: .normal)
        button.isEnabled = isEnabled
    }
}
