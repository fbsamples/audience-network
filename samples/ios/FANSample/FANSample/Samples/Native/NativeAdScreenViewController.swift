// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class NativeAdScreenViewController: UIViewController {

    // MARK: - Properties

    override var shouldAutorotate: Bool {
        false
    }

    override var supportedInterfaceOrientations: UIInterfaceOrientationMask {
        [.portrait, .portraitUpsideDown]
    }

    @IBOutlet private var bottomBarView: BottomBarView!

    private var adView: NativeAdView!
    private let statusLabel = UILabel()

    // MARK: - Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = AdFormatInfo.native.title
        setupSubviews()

        bottomBarView.tapHandler = { [weak adView] in
            adView?.load()
        }
    }

    // MARK: - UI Setup

    private func setupSubviews() {

        // Configure a status label that reports ad load status

        view.addSubview(statusLabel)
        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        statusLabel.textAlignment = .center
        statusLabel.numberOfLines = 0

        // Configure an Ad view that shows the layout of the ad

        adView = NativeAdView(ownerViewController: self) { [weak self] status in
            self?.statusLabel.text = status.debugDescription
        }
        view.addSubview(adView)
        adView.translatesAutoresizingMaskIntoConstraints = false

        // Setup constraints

        NSLayoutConstraint.activate([
            statusLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            statusLabel.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            statusLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16)
        ])

        NSLayoutConstraint.activate([
            adView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            adView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            adView.bottomAnchor.constraint(equalTo: bottomBarView.safeAreaLayoutGuide.topAnchor, constant: -32),
            adView.heightAnchor.constraint(equalToConstant: 300)
        ])
    }
}
