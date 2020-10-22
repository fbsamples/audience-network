// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

extension FBNativeBannerAdViewType {
    var heightInPoints: CGFloat {
        switch self {
        case .genericHeight50:
            return 50
        case .genericHeight100:
            return 100
        case .genericHeight120:
            return 120
        @unknown default:
            print("Unknown `FBNativeBannerAdViewType` type. Returning \(FBNativeBannerAdViewType.genericHeight50) instead.")
            return FBNativeBannerAdViewType.genericHeight50.heightInPoints
        }
    }
}

final class NativeBannerTemplateAdView: UIView {

    // MARK: - Properties

    var adViewType: FBNativeBannerAdViewType = .genericHeight50 {
        didSet {
            heightConstraint.constant = adViewType.heightInPoints
            load()
        }
    }

    private var adView: FBNativeBannerAdView!
    private var ad: FBNativeBannerAd?
    private let updateStatus: AdStatusUpdate

    private var heightConstraint: NSLayoutConstraint!

    // MARK: - Setup

    init(updateStatus: @escaping AdStatusUpdate) {
        self.updateStatus = updateStatus
        super.init(frame: .zero)

        translatesAutoresizingMaskIntoConstraints = false
        heightConstraint = heightAnchor.constraint(equalToConstant: adViewType.heightInPoints)
        heightConstraint.isActive = true
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) { nil }

    func load() {

        /// Hide the view before loading a new one
        adView?.removeFromSuperview()

        // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        let ad = FBNativeBannerAd(placementID: "YOUR_PLACEMENT_ID")

        // Set the delegate of the ad object before loading the ad
        ad.delegate = self

        // Initiate a request to load an ad.
        ad.loadAd()
    }
}

extension NativeBannerTemplateAdView: FBNativeBannerAdDelegate {

    func nativeBannerAdDidLoad(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.didLoad)

        if let ad = ad {
            ad.unregisterView()
            adView?.removeFromSuperview()
            adView = nil
        }

        guard nativeBannerAd.isAdValid else {
            updateStatus(.invalid)
            return
        }

        ad = nativeBannerAd

        adView = FBNativeBannerAdView(nativeBannerAd: nativeBannerAd, with: adViewType)
        adView.frame = bounds
        adView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(adView)

        layoutIfNeeded()
    }

    func nativeBannerAdDidDownloadMedia(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.didDownloadMedia)
    }

    func nativeBannerAdWillLogImpression(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.willLogImpression)
    }

    func nativeBannerAd(_ nativeBannerAd: FBNativeBannerAd, didFailWithError error: Error) {
        updateStatus(.failure(error))
    }

    func nativeBannerAdDidClick(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.didClick)
    }

    func nativeBannerAdDidFinishHandlingClick(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.didFinishHandlingClick)
    }
}
