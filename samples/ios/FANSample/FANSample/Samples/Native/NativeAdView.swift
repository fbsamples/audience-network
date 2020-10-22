// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class NativeAdView: UIView {

    // MARK: - Properties

    @IBOutlet private var iconView: FBMediaView!
    @IBOutlet private var coverView: FBMediaView!
    @IBOutlet private var optionsView: FBAdOptionsView!
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var bodyLabel: UILabel!
    @IBOutlet private var sponsoredLabel: UILabel!
    @IBOutlet private var socialContextLabel: UILabel!
    @IBOutlet private var callToActionButton: UIButton!

    @IBOutlet private var bodyLabelTrailingToCTAButtonConstraint: NSLayoutConstraint!
    @IBOutlet private var bodyLabelTrailingToSuperviewConstraint: NSLayoutConstraint!

    private var ad: FBNativeAd?
    private let updateStatus: AdStatusUpdate
    private weak var ownerViewController: UIViewController?

    // MARK: - Setup

    init(ownerViewController: UIViewController, updateStatus: @escaping AdStatusUpdate) {
        self.ownerViewController = ownerViewController
        self.updateStatus = updateStatus
        super.init(frame: .zero)
        fromNib()

        coverView.delegate = self
        optionsView.isHidden = true
    }

    @available(*, unavailable)
    required init?(coder: NSCoder) { nil }

    func load() {

        updateStatus(.isLoading)

        /// Hide the view before loading a new one
        isHidden = true

        // Create a native ad request with a unique placement ID (generate your own on the Facebook app settings).
        // Use different ID for each ad placement in your app.
        let ad = FBNativeAd(placementID: "YOUR_PLACEMENT_ID")

        // Set the delegate of the ad object before loading the ad
        ad.delegate = self

        // Initiate a request to load an ad.
        ad.loadAd()
    }

    private func setupCallToActionButton(with title: String?) {
        guard let title = title else {
            callToActionButton.isHidden = true
            return
        }
        callToActionButton.isHidden = false
        callToActionButton.setTitle(title, for: .normal)
    }
}

// MARK - FBNativeAdDelegate

extension NativeAdView: FBNativeAdDelegate {
    func nativeAdDidLoad(_ nativeAd: FBNativeAd) {
        updateStatus(.didLoad)

        if let ad = ad {
            ad.unregisterView()
        }

        isHidden = false

        ad = nativeAd
        titleLabel.text = nativeAd.advertiserName
        bodyLabel.text = nativeAd.bodyText
        socialContextLabel.text = nativeAd.socialContext
        sponsoredLabel.text = nativeAd.sponsoredTranslation
        setupCallToActionButton(with: nativeAd.callToAction)

        if let callToAction = nativeAd.callToAction, !callToAction.isEmpty {
            bodyLabelTrailingToCTAButtonConstraint.priority = .defaultHigh
            bodyLabelTrailingToSuperviewConstraint.priority = .defaultLow
        } else {
            bodyLabelTrailingToSuperviewConstraint.priority = .defaultHigh
            bodyLabelTrailingToCTAButtonConstraint.priority = .defaultLow
        }

        print("Register UIView for impression and click...")

        // Set native ad view tags to declare roles of your views for better analysis in future
        // We will be able to provide you statistics how often these views were clicked by users
        // Views provided by Facebook already have appropriate tag set
        titleLabel.nativeAdViewTag = .title
        bodyLabel.nativeAdViewTag = .body
        socialContextLabel.nativeAdViewTag = .socialContext
        callToActionButton.nativeAdViewTag = .callToAction

        // Specify the clickable areas. Views you were using to set ad view tags should be clickable.
        let clickableViews: [UIView] = [
            iconView,
            titleLabel,
            bodyLabel,
            socialContextLabel,
            callToActionButton
        ]
        nativeAd.registerView(
            forInteraction: self,
            mediaView: coverView,
            iconView: iconView,
            viewController: ownerViewController,
            clickableViews: clickableViews
        )

        // You may want to use this method if you want to have all subviews clickable
//        nativeAd.registerView(
//            forInteraction: self,
//            mediaView: coverView,
//            iconView: iconView,
//            viewController: ownerViewController
//        )

        optionsView.isHidden = false
        optionsView.nativeAd = nativeAd

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

// MARK: - FBMediaViewDelegate

extension NativeAdView: FBMediaViewDelegate {
    func mediaViewDidLoad(_ mediaView: FBMediaView) {
        print("Media view did load")
    }
}
