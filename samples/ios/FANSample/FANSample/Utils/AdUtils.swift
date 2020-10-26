// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import Foundation

typealias AdStatusUpdate = (AdStatus) -> Void

enum AdStatus: CustomDebugStringConvertible {
    case willLogImpression
    case failure(Error)
    case invalid
    case didClick
    case didFinishHandlingClick
    case isLoading
    case didLoad
    case didDownloadMedia

    var debugDescription: String {
        switch self {
        case .didClick:
            return "Ad was clicked"
        case .failure(let error):
            return "Ad failed to load with error: " + error.localizedDescription
        case .invalid:
            return "Ad is invalid"
        case .didLoad:
            return "Ad did load"
        case .isLoading:
            return "Ad is loading"
        case .didDownloadMedia:
            return "Ad did download media"
        case .willLogImpression:
            return "Ad impression is being captured"
        case .didFinishHandlingClick:
            return "Ad did finish handling click"
        }
    }
}

enum LoadingState {
    case initial
    case loading
    case loaded
    case error(message: String)
}

enum AdFormatInfo {
    case native
    case nativeTemplate
    case nativeBanner
    case nativeBannerTemplate
    case banner
    case mediumRect
    case interstitial
    case rewardedVideo

    var title: String {
        switch self {
        case .native: return "Native"
        case .nativeTemplate: return  "Native (Template)"
        case .nativeBanner: return "Native Banner"
        case .nativeBannerTemplate: return "Native Banner (Template)"
        case .banner: return "Banner"
        case .mediumRect: return "Medium Rectangle"
        case .interstitial: return "Interstitial"
        case .rewardedVideo: return "Rewarded Video"
        }
    }
}
