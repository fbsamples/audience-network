// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import Foundation
import FBAudienceNetwork

protocol PickerCellViewModelAPI {
    var title: String { get }
    var selectedRow: Int { get }
    var numberOfRows: Int { get }
    func name(by index: Int) -> String?
    func select(index: Int)
}

struct LogLevelCellViewModel: PickerCellViewModelAPI {

    let title = "Log Level"

    var numberOfRows: Int { nameByLevel.count }
    var selectedRow: Int { FBAdSettings.getLogLevel().rawValue }

    private let nameByLevel: [FBAdLogLevel: String] = [
        .none : "None",
        .notification : "Notification",
        .error : "Error",
        .warning : "Warning",
        .log : "Log",
        .debug : "Debug",
        .verbose : "Verbose"
    ]

    func name(by index: Int) -> String? {
        guard let type = FBAdLogLevel(rawValue: index) else {
            print("Error: unable to map index to `FBAdLogLevel`")
            return nil
        }
        return nameByLevel[type]
    }

    func select(index: Int) {
        guard let level = FBAdLogLevel(rawValue: index) else {
            return
        }
        FBAdSettings.setLogLevel(level)
    }
}

struct AdTypeCellViewModel: PickerCellViewModelAPI {

    let title = "Ad Type"

    var numberOfRows: Int { nameByType.count }
    var selectedRow: Int { FBAdSettings.testAdType.rawValue }

    private let nameByType: [FBAdTestAdType: String] = [
        .default : "default",
        .img_16_9_App_Install : "image_install",
        .img_16_9_Link : "image_link",
        .vid_HD_16_9_46s_App_Install : "video_16x9_46s_install",
        .vid_HD_16_9_46s_Link : "video_16x9_46s_link",
        .vid_HD_16_9_15s_App_Install : "video_16x9_15s_install",
        .vid_HD_16_9_15s_Link : "video_16x9_15s_link",
        .vid_HD_9_16_39s_App_Install : "video_9x16_39s_install",
        .vid_HD_9_16_39s_Link : "video_9x16_39s_link",
        .carousel_Img_Square_App_Install : "carousel_install",
        .carousel_Img_Square_Link : "carousel_link",
        .carousel_Vid_Square_Link : "carousel_video",
        .playable : "playable"
    ]

    func name(by index: Int) -> String? {
        guard let type = FBAdTestAdType(rawValue: index) else {
            print("Error: unable to map index to `FBAdTestAdType`")
            return nil
        }
        return nameByType[type]
    }

    func select(index: Int) {
        guard let type = FBAdTestAdType(rawValue: index) else {
            return
        }
        FBAdSettings.testAdType = type
    }
}
