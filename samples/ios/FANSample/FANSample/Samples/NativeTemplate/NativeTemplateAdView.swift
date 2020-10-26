// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class NativeTemplateAdView: UIView {

    // MARK: - Properties

    private lazy var adAttributes: FBNativeAdViewAttributes = {
        let attributes = FBNativeAdViewAttributes()
        attributes.backgroundColor = .white
        attributes.advertiserNameColor = UIColor(red: 0.11, green: 0.12, blue: 0.13, alpha: 1)
        attributes.buttonColor = UIColor(red: 0.235, green: 0.485, blue: 1, alpha: 1)
        attributes.buttonTitleColor = .white
        attributes.titleColor = .darkGray
        attributes.descriptionColor = .gray
        return attributes
    }()
    private var ad: FBNativeAd?
    private var adView: FBNativeAdView!

    private let updateStatus: AdStatusUpdate

    // MARK: - Setup

    init(updateStatus: @escaping AdStatusUpdate) {
        self.updateStatus = updateStatus
        super.init(frame: .zero)
        backgroundColor = .white
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) { nil }

    func load() {

        updateStatus(.isLoading)

        // Remove the previous view before loading a new one
        adView?.removeFromSuperview()

        // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        let ad = FBNativeAd(placementID: "YOUR_PLACEMENT_ID")

        // Set the delegate of the ad object before loading the ad
        ad.delegate = self

        // Initiate a request to load an ad.
        ad.loadAd()
    }
}

// MARK - FBNativeAdDelegate

extension NativeTemplateAdView: FBNativeAdDelegate {
    func nativeAdDidLoad(_ nativeAd: FBNativeAd) {
        updateStatus(.didLoad)

        if let ad = ad {
            ad.unregisterView()
            adView?.removeFromSuperview()
            adView = nil
        }

        guard nativeAd.isAdValid else {
            updateStatus(.invalid)
            return
        }

        ad = nativeAd

        adView = FBNativeAdView(nativeAd: nativeAd, with: adAttributes)
        adView.frame = bounds
        adView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        addSubview(adView)

        layoutIfNeeded()
    }

    func nativeAdDidClick(_ nativeAd: FBNativeAd) {
        updateStatus(.didClick)
    }

    func nativeAdDidDownloadMedia(_ nativeAd: FBNativeAd) {
        updateStatus(.didDownloadMedia)
    }

    func nativeAdWillLogImpression(_ nativeAd: FBNativeAd) {
        updateStatus(.willLogImpression)
    }

    func nativeAdDidFinishHandlingClick(_ nativeAd: FBNativeAd) {
        updateStatus(.didFinishHandlingClick)
    }

    func nativeAd(_ nativeAd: FBNativeAd, didFailWithError error: Error) {
        updateStatus(.failure(error))
    }
}
