// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class NativeTemplateScreenViewController: UIViewController {

    // MARK: - Constants

    private enum Constants {
        enum Width {
            static let min: Float = 120
            static let `default`: Float = 250
            static let max: Float = 320
        }

        enum Height {
            static let min: Float = 150
            static let `default`: Float = 250
            static let max: Float = 400
        }
    }

    override var shouldAutorotate: Bool {
        false
    }

    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        [.portrait, .portraitUpsideDown]
    }

    @IBOutlet private var widthSlider: UISlider!
    @IBOutlet private var heightSlider: UISlider!
    @IBOutlet private var slidersStackView: UIStackView!
    @IBOutlet private var bottomBarView: BottomBarView!

    private var heightConstraint: NSLayoutConstraint!
    private var widthConstraint: NSLayoutConstraint!

    private var adView: NativeTemplateAdView!
    private let statusLabel = UILabel()

    // MARK: - Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = AdFormatInfo.nativeTemplate.title
        setupSubviews()

        bottomBarView.tapHandler = { [weak adView] in
            adView?.load()
        }
    }

    // MARK: - UI Setup

    private func setupSubviews() {
        adView = NativeTemplateAdView { [weak self] status in
            self?.statusLabel.text = status.debugDescription
        }
        view.addSubview(adView)
        view.addSubview(statusLabel)

        statusLabel.textAlignment = .center
        statusLabel.numberOfLines = 0

        widthSlider.minimumValue = Constants.Width.min
        widthSlider.maximumValue = Constants.Width.max
        widthSlider.value = Constants.Width.default

        heightSlider.minimumValue = Constants.Height.min
        heightSlider.maximumValue = Constants.Height.max
        heightSlider.value = Constants.Height.default

        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            statusLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            statusLabel.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            statusLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16)
        ])

        adView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            adView.centerXAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerXAnchor),
            adView.bottomAnchor.constraint(equalTo: slidersStackView.topAnchor, constant: -32)
        ])

        heightConstraint = adView.heightAnchor.constraint(equalToConstant: CGFloat(heightSlider.value))
        heightConstraint.isActive = true

        widthConstraint = adView.widthAnchor.constraint(equalToConstant: CGFloat(widthSlider.value))
        widthConstraint.isActive = true
    }

    // MARK: - IBActions

    @IBAction private func widthSliderValueChanged(sender: UISlider) {
        widthConstraint.constant = CGFloat(sender.value)
    }

    @IBAction private func heightSliderValueChanged(sender: UISlider) {
        heightConstraint.constant = CGFloat(sender.value)
    }
}
