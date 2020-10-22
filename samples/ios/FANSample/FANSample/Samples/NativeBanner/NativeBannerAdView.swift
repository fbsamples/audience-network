// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class NativeBannerAdView: UIView {
    
    // MARK: - Properties
    
    @IBOutlet private var iconView: FBMediaView!
    @IBOutlet private var optionsView: FBAdOptionsView!
    @IBOutlet private var titleLabel: UILabel!
    @IBOutlet private var callToActionButton: UIButton!
    @IBOutlet private var sponsoredLabel: UILabel!
    
    private var ad: FBNativeBannerAd?
    private let updateStatus: AdStatusUpdate
    
    private weak var ownerViewController: UIViewController?
    
    // MARK: - Setup
    
    init(ownerViewController: UIViewController, updateStatus: @escaping AdStatusUpdate) {
        self.ownerViewController = ownerViewController
        self.updateStatus = updateStatus
        super.init(frame: .zero)
        fromNib()
        
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
        let ad = FBNativeBannerAd(placementID: "YOUR_PLACEMENT_ID")
        
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

// MARK: - FBNativeBannerAdDelegate

extension NativeBannerAdView: FBNativeBannerAdDelegate {
        
    func nativeBannerAdDidLoad(_ nativeBannerAd: FBNativeBannerAd) {
        updateStatus(.didLoad)
        
        if let ad = ad {
            ad.unregisterView()
        }
        
        ad = nativeBannerAd

        isHidden = false

        titleLabel.text = nativeBannerAd.advertiserName
        sponsoredLabel.text = nativeBannerAd.sponsoredTranslation
        setupCallToActionButton(with: nativeBannerAd.callToAction)
        
        print("Register UIView for impression and click...")

        // Set native banner ad view tags to declare roles of your views for better analysis in future
        // We will be able to provide you statistics how often these views were clicked by users
        // Views provided by Facebook already have appropriate tag set
        titleLabel.nativeAdViewTag = .title
        callToActionButton.nativeAdViewTag = FBNativeAdViewTag.callToAction

        // Specify the clickable areas. View you were using to set ad view tags should be clickable.
        let clickableViews: [UIView] = [callToActionButton]
        nativeBannerAd.registerView(
            forInteraction: self,
            iconView: iconView,
            viewController: ownerViewController,
            clickableViews: clickableViews
        )
                
        // If you don't want to provide native ad view tags you can simply
        // wire up UIView with the native banner ad and the whole UIView will be clickable.
//        nativeBannerAd.registerView(
//            forInteraction: iconView,
//            iconView: iconView,
//            viewController: ownerViewController
//        )

        optionsView.nativeAd = nativeBannerAd
        optionsView.isHidden = false
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
