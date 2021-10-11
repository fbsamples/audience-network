// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

import UIKit
import FBAudienceNetwork

final class SamplesViewController: UITableViewController {

    // MARK: - Types

    private struct Section {
        let title: String
        let objects: [AdFormatInfo]
    }

    // MARK: - Properties

    private let sections: [Section] = {
        let nativeSection = Section(
            title: "Native",
            objects: [.native, .nativeTemplate, .nativeBanner, .nativeBannerTemplate]
        )
        let bannerSection = Section(
            title: "Banner",
            objects: [.banner, .mediumRect]
        )
        let fullscreenSection = Section(
            title: "Fullscreen",
            objects: [.interstitial, .rewardedVideo, .rewardedInterstitial]
        )
        return [nativeSection, bannerSection, fullscreenSection]
    }()

    private let sdkInfoLabel: UILabel = {
        let sdkInfoLabel = UILabel(frame: CGRect(x: 0, y: 0, width: 0, height: 50))
        sdkInfoLabel.textAlignment = .center
        sdkInfoLabel.text = "AN SDK \(FB_AD_SDK_VERSION)"
        return sdkInfoLabel
    }()

    // MARK: - Lifecycle

    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = sdkInfoLabel
    }

    private func createViewController(for adFormat: AdFormatInfo) -> UIViewController? {
      let vc: UIViewController?
      switch adFormat {
        case .interstitial:
          vc = FullscreenAdSampleViewController.create(adType: .interstitial)
        case .rewardedVideo:
          vc = FullscreenAdSampleViewController.create(adType: .rewardedVideo)
        case .rewardedInterstitial:
          vc = FullscreenAdSampleViewController.create(adType: .rewardedInterstitial)
        case .banner:
          vc = BannerSampleViewController.create(adType: .banner)
        case .native:
          vc = NativeAdScreenViewController.create()
        case .nativeBanner:
          vc = NativeBannerAdScreenViewController.create()
        case .nativeTemplate:
          vc = NativeTemplateScreenViewController.create()
        case .mediumRect:
          vc = BannerSampleViewController.create(adType: .mediumRect)
        case .nativeBannerTemplate:
          vc = NativeBannerTemplateAdScreenViewController.create()
      }
      return vc
    }

    // MARK: - Settings

    @IBAction private func settingsButtonTapped() {
        let navigationController = NavigationController(nibName: nil, bundle: nil)
        navigationController.viewControllers = [SettingScreenViewController.create()]
        self.navigationController?.present(navigationController, animated: true, completion: nil)
    }

    // MARK: - Table View

    override func numberOfSections(in tableView: UITableView) -> Int {
        sections.count
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        sections[section].objects.count
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let currentObject = sections[indexPath.section].objects[indexPath.row]
        guard let vc = createViewController(for: currentObject) else { return }
        navigationController?.pushViewController(vc, animated: true)
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let currentSection = sections[indexPath.section]
        let cell = tableView.dequeueReusableCell(withIdentifier: "AdFormatCell",
                                                 for: indexPath)
        cell.textLabel?.text = currentSection.objects[indexPath.row].title
        return cell
    }

    override func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        sections[section].title
    }
}
