// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

enum BannerAdType {
    case banner, mediumRect

    var title: String {
        switch self {
        case .banner:
            return "Banner"
        case .mediumRect:
            return "Medium Rectangle"
        }
    }
}

final class BannerSampleViewController: UIViewController {

    // MARK: - Properties

    private var adType: BannerAdType
    private var adView: FBAdView?

    private var adSize: FBAdSize {
        switch adType {
        case .banner:
            let isRegularWidth = traitCollection.horizontalSizeClass == .regular
            return isRegularWidth ? kFBAdSizeHeight90Banner : kFBAdSizeHeight50Banner
        case .mediumRect:
            return kFBAdSizeHeight250Rectangle
        }
    }

    private let statusLabel = UILabel()
    @IBOutlet private weak var bottomBarView: BottomBarView!

    // MARK: - Setup and Lifecylce

    static func create(adType: BannerAdType) -> BannerSampleViewController {
        let vc: BannerSampleViewController = BannerSampleViewController.create()
        vc.adType = adType
        return vc
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        navigationItem.title = adType.title

        setupStatusLabel()
        loadAd()

        bottomBarView.tapHandler = { [weak self] in
            self?.loadAd()
        }
    }

    required init?(coder: NSCoder) {
        self.adType = .banner
        super.init(coder: coder)
    }

    private func setupStatusLabel() {
        view.addSubview(statusLabel)
        statusLabel.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            statusLabel.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor, constant: 16),
            statusLabel.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor, constant: -16),
            statusLabel.bottomAnchor.constraint(equalTo: bottomBarView.topAnchor, constant: -16)
        ])
        statusLabel.textAlignment = .center
        statusLabel.numberOfLines = 0
    }

    // MARK: Helpers

    private func loadAd() {
        adView?.removeFromSuperview()
        adView = createNewAd()
        adView?.loadAd()
        statusLabel.text = AdStatus.isLoading.debugDescription
    }

    private func createNewAd() -> FBAdView {
        let adView = FBAdView(placementID: "YOUR_PLACEMENT_ID", adSize: adSize, rootViewController: self)
        adView.delegate = self
        return adView
    }
}

// MARK: FBAdViewDelegate

extension BannerSampleViewController: FBAdViewDelegate {

    func adViewDidLoad(_ adView: FBAdView) {
        statusLabel.text = AdStatus.didLoad.debugDescription

        adView.backgroundColor = .clear
        adView.translatesAutoresizingMaskIntoConstraints = false

        view.addSubview(adView)

        NSLayoutConstraint.activate([
            adView.heightAnchor.constraint(equalToConstant: adSize.size.height),
            adView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            adView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 30)
        ])

        if adSize.size == kFBAdSizeHeight250Rectangle.size {
            NSLayoutConstraint.activate([adView.widthAnchor.constraint(equalToConstant: 300)])
        } else {
            NSLayoutConstraint.activate([
                adView.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
                adView.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor)
            ])
        }
    }

    func adViewDidClick(_ adView: FBAdView) {
        statusLabel.text = AdStatus.didClick.debugDescription
    }

    func adViewDidFinishHandlingClick(_ adView: FBAdView) {
        statusLabel.text = AdStatus.didFinishHandlingClick.debugDescription
    }

    func adView(_ adView: FBAdView, didFailWithError error: Error) {
        statusLabel.text = AdStatus.failure(error).debugDescription
    }

    func adViewWillLogImpression(_ adView: FBAdView) {
        statusLabel.text = AdStatus.willLogImpression.debugDescription
    }
}
