// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class NativeBannerTemplateAdScreenViewController: UIViewController {

    // MARK: - Properties

    @IBOutlet private var bottomBarView: BottomBarView!

    private var adView: NativeBannerTemplateAdView!
    private let statusLabel = UILabel()

    // MARK: - Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = AdFormatInfo.nativeBannerTemplate.title
        setupSubviews()

        bottomBarView.tapHandler = { [weak adView] in
            adView?.load()
        }
    }

    private func setupSubviews() {
        adView = NativeBannerTemplateAdView { [weak self] status in
            self?.statusLabel.text = status.debugDescription
        }
        view.addSubview(adView)
        view.addSubview(statusLabel)

        statusLabel.textAlignment = .center
        statusLabel.numberOfLines = 0

        adView.adViewType = .genericHeight50

        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            statusLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            statusLabel.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            statusLabel.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16)
        ])

        NSLayoutConstraint.activate([
            adView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            adView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            adView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }

    // MARK: - IBActions

    @IBAction private func segmentedControlValueChanged(sender: UISegmentedControl) {
        switch sender.selectedSegmentIndex {
        case 0:
            adView.adViewType = .genericHeight50
        case 1:
            adView.adViewType = .genericHeight100
        default:
            adView.adViewType = .genericHeight120
        }
    }
}
