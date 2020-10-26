// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit

final class NativeBannerAdScreenViewController: UIViewController {

    // MARK: - Properties
    
    @IBOutlet private var bottomBarView: BottomBarView!
    private var adView: NativeBannerAdView!
    private let statusLabel = UILabel()
    
    // MARK: - Lifecycle
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        navigationItem.title = AdFormatInfo.nativeBanner.title
        setupStatusLabel()
        setupAdView()

        bottomBarView.tapHandler = { [weak adView] in
            adView?.load()
        }
    }
    
    // MARK: - UI Setup
    
    private func setupStatusLabel() {
        view.addSubview(statusLabel)
        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            statusLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            statusLabel.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            statusLabel.centerYAnchor.constraint(equalTo: view.safeAreaLayoutGuide.centerYAnchor)
        ])
        statusLabel.textAlignment = .center
        statusLabel.numberOfLines = 0
    }
    
    private func setupAdView() {
        adView = NativeBannerAdView(ownerViewController: self) { [weak self] status in
            self?.statusLabel.text = status.debugDescription
        }
        view.addSubview(adView)
        adView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            adView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            adView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            adView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
}

